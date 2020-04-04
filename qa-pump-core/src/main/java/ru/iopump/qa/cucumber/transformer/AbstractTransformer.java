package ru.iopump.qa.cucumber.transformer;

import groovy.lang.GroovyRuntimeException;
import lombok.NonNull;
import ru.iopump.qa.annotation.PumpApi;
import ru.iopump.qa.cucumber.processor.GroovyScriptProcessor;
import ru.iopump.qa.cucumber.processor.ProcessResult;

@PumpApi
public abstract class AbstractTransformer<TARGET> implements Transformer<TARGET, Object, GroovyRuntimeException, GroovyScriptProcessor> {

    public Class<GroovyScriptProcessor> preProcessorClass() {
        return GroovyScriptProcessor.class;
    }

    @Override
    public abstract TARGET transform(@NonNull ProcessResult<Object, GroovyRuntimeException> gherkinAfterProcessing);
}
