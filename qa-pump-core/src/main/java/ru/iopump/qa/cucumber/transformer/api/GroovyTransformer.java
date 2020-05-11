package ru.iopump.qa.cucumber.transformer.api;

import java.util.function.Supplier;
import lombok.NonNull;
import ru.iopump.qa.annotation.PumpApi;
import ru.iopump.qa.component.groovy.GroovyScript;
import ru.iopump.qa.cucumber.processor.GroovyProcessor;
import ru.iopump.qa.util.Str;

@PumpApi
public abstract class GroovyTransformer<TARGET> implements Transformer<TARGET, GroovyScript, GroovyProcessor> {

    @Override
    public int priority() {
        return 0;
    }

    public Class<GroovyProcessor> preProcessorClass() {
        return GroovyProcessor.class;
    }

    @Override
    public RelativeType relativeType() {
        return RelativeType.CHILD;
    }

    /**
     * Need documentation.
     *
     * @return How to create {@link GroovyScript} instance.
     */
    @NonNull
    @Override
    public Supplier<GroovyScript> helperSupplier() {
        return GroovyScript::create;
    }

    @Override
    public Class<GroovyProcessor> processorClass() {
        return GroovyProcessor.class;
    }

    @Override
    public String toString() {
        return Str.frm("{}(target={}, processor={}, priority={})",
            getClass().getSimpleName(),
            targetType(),
            preProcessorClass().getName(),
            priority()
        );
    }
}
