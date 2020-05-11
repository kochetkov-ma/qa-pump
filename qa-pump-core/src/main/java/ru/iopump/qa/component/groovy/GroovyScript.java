package ru.iopump.qa.component.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.util.DelegatingScript;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.apache.commons.lang3.tuple.Pair;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GroovyScript implements GroovyEvaluator {

    private final EvaluatingMode mode;
    private final boolean tryAsMethod;
    private final Map<String, Object> bindingMap;
    private final Object delegatedObject;
    private final Imports imports;
    private final Pair<String, String> bingPrefixInScript;

    public static GroovyScript create() {
        return new GroovyScript(
            EvaluatingMode.CLOSURE, true, null, null, null, Pair.of("$", "")
        );
    }

    public GroovyScript withMode(EvaluatingMode mode) {
        return new GroovyScript(mode, tryAsMethod, bindingMap, delegatedObject, imports, bingPrefixInScript);
    }

    public GroovyScript withTryAsMethod(boolean tryAsMethod) {
        return new GroovyScript(mode, tryAsMethod, bindingMap, delegatedObject, imports, bingPrefixInScript);
    }

    public GroovyScript withBindingMap(@Nullable Map<String, Object> bindingMap) {
        return new GroovyScript(mode, tryAsMethod, bindingMap, delegatedObject, imports, bingPrefixInScript);
    }

    public GroovyScript withDelegatedObject(@Nullable Object delegatedObject) {
        return new GroovyScript(mode, tryAsMethod, bindingMap, delegatedObject, imports, bingPrefixInScript);
    }

    public GroovyScript withImports(@Nullable Imports imports) {
        return new GroovyScript(mode, tryAsMethod, bindingMap, delegatedObject, imports, bingPrefixInScript);
    }

    public GroovyScript withBindPrefixInScript(@Nullable Pair<String, String> bingPrefixInScript) {
        return new GroovyScript(mode, tryAsMethod, bindingMap, delegatedObject, imports, bingPrefixInScript);
    }

    @Override
    public Object evaluate(@NonNull String groovyScript) { //NOPMD
        String gScript = groovyScript;
        final CompilerConfiguration compiler = new CompilerConfiguration();
        if (mode == EvaluatingMode.G_STRING) {
            gScript = GroovyUtil.asGString(gScript);
        }
        if (delegatedObject != null && mode == EvaluatingMode.CLOSURE) {
            compiler.setScriptBaseClass(DelegatingScript.class.getName());
        }

        final ImportCustomizer customizer = new ImportCustomizer();
        if (imports != null) {
            customizer.addImports(imports.simpleImports());
            customizer.addStarImports(imports.starImports());
            imports.staticImports().forEach(i -> customizer.addStaticImport(i.getKey(), i.getValue()));
            customizer.addStaticStars(imports.staticStarImports());
        }
        compiler.addCompilationCustomizers(customizer);
        if (delegatedObject != null && mode == EvaluatingMode.SCRIPT) {
            bindingMap.put("delegatedObject", delegatedObject);
            gScript = "delegatedObject." + gScript; //NOPMD
        }
        Map<String, Object> bindingMapWithPrefix = bindingMap;
        if (bingPrefixInScript != null && bindingMap != null) {
            if (GroovyUtil.isGString(gScript)) {
                // Use right prefix for Script by default ''
                bindingMapWithPrefix = bindingMap.entrySet().stream()
                    .collect(Collectors.toMap(e -> bingPrefixInScript.getRight() + e.getKey(), Map.Entry::getValue));
            } else {
                // Use left prefix for Script by default '$'
                bindingMapWithPrefix = bindingMap.entrySet().stream()
                    .collect(Collectors.toMap(e -> bingPrefixInScript.getLeft() + e.getKey(), Map.Entry::getValue));
            }
        }
        final GroovyShell shell = new GroovyShell(new Binding(bindingMapWithPrefix), compiler);

        final Script script;
        if (delegatedObject != null && mode == EvaluatingMode.CLOSURE) {
            script = shell.parse(gScript);
            ((DelegatingScript) script).setDelegate(delegatedObject);
        } else {
            script = shell.parse(gScript);
        }

        Object result;
        if (tryAsMethod && !gScript.endsWith(")")) {
            try {
                result = script.run();
            } catch (GroovyRuntimeException e) {
                if (delegatedObject != null) {
                    final Script s = shell.parse(gScript);
                    ((DelegatingScript) s).setDelegate(delegatedObject);
                    result = s.run();
                } else {
                    result = shell.parse(gScript).run();
                }
            }
        } else {
            result = script.run();
        }
        return result;
    }
}