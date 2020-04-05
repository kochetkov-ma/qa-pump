package ru.iopump.qa.cucumber.transformer;

import groovy.lang.GroovyRuntimeException;
import java.lang.reflect.Type;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.iopump.qa.cucumber.processor.ProcessResult;

@Component
public final class StringTransformer extends AbstractGroovyTransformer<String> {

    @Override
    public Type targetType() {
        return String.class;
    }

    @Override
    public String transform(@NonNull ProcessResult<Object, GroovyRuntimeException> gherkinAfterProcessing) {
        return gherkinAfterProcessing.getResultAsString();
    }
}
