package ru.iopump.qa.cucumber.transformer;

import groovy.lang.GroovyRuntimeException;
import java.lang.reflect.Type;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.iopump.qa.cucumber.processor.ProcessResult;
import ru.iopump.qa.exception.PumpException;

@Component
public final class ObjectTransformer extends AbstractGroovyTransformer<Object> {

    @Override
    public Type targetType() {
        return Object.class;
    }

    @Override
    public Object transform(@NonNull ProcessResult<Object, GroovyRuntimeException> gherkinAfterProcessing) {
        return gherkinAfterProcessing.getResult().orElseThrow(() ->
            PumpException.of("Object transformer error, because Object result is empty '{}'", gherkinAfterProcessing)
                .withCause(gherkinAfterProcessing.getProcessException().orElse(null))
        );
    }
}
