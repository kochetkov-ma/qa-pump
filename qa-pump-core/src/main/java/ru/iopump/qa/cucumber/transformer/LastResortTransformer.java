package ru.iopump.qa.cucumber.transformer;

import java.lang.reflect.Type;
import java.util.function.Supplier;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.iopump.qa.component.groovy.GroovyScript;
import ru.iopump.qa.cucumber.processor.ProcessResult;
import ru.iopump.qa.exception.PumpException;

@RequiredArgsConstructor
public final class LastResortTransformer extends AbstractGroovyTransformer<Object> {

    @Override
    public int priority() {
        return Integer.MIN_VALUE;
    }

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
        return gherkinAfterProcessing.getResult().orElse(gherkinAfterProcessing.getResultAsString());
    }
}
