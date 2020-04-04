package ru.iopump.qa.component.groovy;

import lombok.NonNull;
import ru.iopump.qa.annotation.PumpApi;

@PumpApi
public interface GroovyEvaluator {

    /**
     * Execute groovy script.
     * <p>Examples:
     * <pre>
     * evaluateScript("'string1' + ':' + 'string1'") // out: "string1:string1"
     * evaluateScript("1 + 1") // out: 1
     * evaluateScript("\"${1+1}\"") // out: "2" !!!GString.class (not String.class)!!!
     * </pre>
     *
     * @param groovyScript groovy script or GString
     * @return The result as Object.
     */
    Object evaluateScript(@NonNull String groovyScript);
}
