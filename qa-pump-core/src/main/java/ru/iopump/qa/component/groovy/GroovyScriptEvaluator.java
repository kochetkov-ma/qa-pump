package ru.iopump.qa.component.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import java.util.Map;
import javax.annotation.Nullable;
import lombok.NonNull;

class GroovyScriptEvaluator {
    private final GroovyShell shell = new GroovyShell();

    @Nullable
    public Object evaluate(@NonNull String groovyScript) {
                return shell.evaluate(groovyScript);
    }

    @Nullable
    public Object evaluate(@NonNull String groovyScript, @Nullable Map<String, Object> bindingMap) {
        final Script script = shell.parse(groovyScript);
        script.setBinding(new Binding(bindingMap));
        return script.run();
    }
}
