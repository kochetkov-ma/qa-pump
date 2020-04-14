package ru.iopump.qa.component.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyRuntimeException;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import groovy.util.DelegatingScript;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.springframework.util.CollectionUtils;

@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GroovyScript implements GroovyEvaluator {

    private final EvaluatingMode mode;
    private final boolean tryAsMethod;
    private final Map<String, Object> bindingMap;
    private final Object delegatedObject;
    private final Imports imports;

    public static GroovyScript create() {
        return new GroovyScript(EvaluatingMode.CLOSURE, true, null, null, null);
    }

    public GroovyScript withMode(EvaluatingMode mode) {
        return new GroovyScript(mode, tryAsMethod, bindingMap, delegatedObject, imports);
    }

    public GroovyScript withTryAsMethod(boolean tryAsMethod) {
        return new GroovyScript(mode, tryAsMethod, bindingMap, delegatedObject, imports);
    }

    public GroovyScript withBindingMap(@Nullable Map<String, Object> bindingMap) {
        return new GroovyScript(mode, tryAsMethod, bindingMap, delegatedObject, imports);
    }

    public GroovyScript withDelegatedObject(@Nullable Object delegatedObject) {
        return new GroovyScript(mode, tryAsMethod, bindingMap, delegatedObject, imports);
    }

    public GroovyScript withStaticImports(@Nullable List<Imports> staticImports) {
        return new GroovyScript(mode, tryAsMethod, bindingMap, delegatedObject, imports);
    }

    @Override
    public Object evaluate(@NonNull String groovyScript) {
        final CompilerConfiguration compiler = new CompilerConfiguration();
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
            groovyScript = "delegatedObject." + groovyScript;
        }
        final GroovyShell shell = new GroovyShell(new Binding(bindingMap), compiler);

        final Script script;
        if (delegatedObject != null && mode == EvaluatingMode.CLOSURE) {
            script = shell.parse(groovyScript);
            ((DelegatingScript) script).setDelegate(delegatedObject);
        } else {
            script = shell.parse(groovyScript);
        }

        Object result;
        if (tryAsMethod && !groovyScript.endsWith(")")) {
            try {
                result = script.run();
            } catch (GroovyRuntimeException e) {
                if (delegatedObject != null) {
                    final Script s = shell.parse(groovyScript);
                    ((DelegatingScript) s).setDelegate(delegatedObject);
                    result = s.run();
                } else {
                    result = shell.parse(groovyScript).run();
                }
            }
        } else {
            result = script.run();
        }
        return result;
    }
}