package ru.iopump.qa.cucumber.transformer;

import groovy.lang.GroovyRuntimeException;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import ru.iopump.qa.cucumber.processor.ProcessResult;

@Component
public class StringTransformer extends AbstractTransformer<String> {

    @Override
    public String transform(@NonNull ProcessResult<Object, GroovyRuntimeException> gherkinAfterProcessing) {
        return gherkinAfterProcessing.getResultAsString();
    }
}
