package ru.iopump.qa.component.groovy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.iopump.qa.component.TestContext;

@Component
@RequiredArgsConstructor
public class GroovyEvaluatorImpl implements GroovyEvaluator {

    private final TestContext context;
    private final GroovyScriptEvaluator groovyScriptEvaluator = new GroovyScriptEvaluator();

    public Object evaluateScript(String groovyScript) {
        return groovyScriptEvaluator.evaluate(groovyScript, context.snapshot());
    }
}
