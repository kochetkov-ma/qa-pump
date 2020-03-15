package ru.iopump.qa.cucumber.transformer;

import java.lang.reflect.Type;
import java.util.function.Supplier;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.iopump.qa.component.groovy.GroovyScript;
import ru.iopump.qa.cucumber.processor.ProcessResult;
import ru.iopump.qa.exception.PumpException;

public final class ObjectTransformer extends AbstractGroovyTransformer<Object> {

    @Override
    public Type targetType() {
        return Object.class;
    }

    @Override
    public Supplier<GroovyScript> helperSupplier() {
        return GroovyScript::create;
    }

    @Override
    public Object transform(@NonNull ProcessResult gherkinAfterProcessing) {
        return gherkinAfterProcessing.getResult().orElseThrow(() ->
            PumpException.of("Object transformer error, because Object result is empty '{}'", gherkinAfterProcessing)
                .withCause(gherkinAfterProcessing.getProcessException().orElse(null))
        );
    }
}
