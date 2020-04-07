package ru.iopump.qa.cucumber.transformer;

import java.lang.reflect.Type;
import java.util.function.Supplier;
import lombok.NonNull;
import ru.iopump.qa.component.groovy.GroovyScript;
import ru.iopump.qa.cucumber.processor.ProcessResult;

public final class StringTransformer extends AbstractGroovyTransformer<String> {

    @Override
    public Type targetType() {
        return String.class;
    }

    @Override
    public Supplier<GroovyScript> helperSupplier() {
        return GroovyScript::create;
    }

    @Override
    public String transform(@NonNull ProcessResult gherkinAfterProcessing) {
        return gherkinAfterProcessing.getResultAsString();
    }
}
