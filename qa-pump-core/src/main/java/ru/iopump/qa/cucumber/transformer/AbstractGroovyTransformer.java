package ru.iopump.qa.cucumber.transformer;

import groovy.lang.GroovyRuntimeException;
import lombok.NonNull;
import ru.iopump.qa.annotation.PumpApi;
import ru.iopump.qa.cucumber.processor.GroovyScriptProcessor;
import ru.iopump.qa.cucumber.processor.ProcessResult;
import ru.iopump.qa.util.Str;

@PumpApi
public abstract class AbstractGroovyTransformer<TARGET> implements Transformer<TARGET, Object, GroovyRuntimeException, GroovyScriptProcessor> {

    @Override
    public int priority() {
        return 0;
    }

    public Class<GroovyScriptProcessor> preProcessorClass() {
        return GroovyScriptProcessor.class;
    }

    @Override
    public abstract TARGET transform(@NonNull ProcessResult<Object, GroovyRuntimeException> gherkinAfterProcessing);

    @Override
    public String toString() {
        return Str.format("AbstractGroovyTransformer(class={}, target={}, processor={}, priority={})",
            getClass().getName(),
            targetType(),
            preProcessorClass().getName(),
            priority()
        );
    }
}
