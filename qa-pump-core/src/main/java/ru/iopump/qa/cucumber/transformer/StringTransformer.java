package ru.iopump.qa.cucumber.transformer;

import java.lang.reflect.Type;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import lombok.NonNull;
import ru.iopump.qa.component.groovy.EvaluatingMode;
import ru.iopump.qa.component.groovy.GroovyScript;
import ru.iopump.qa.cucumber.processor.ProcessResult;
import ru.iopump.qa.cucumber.transformer.api.GroovyTransformer;

public final class StringTransformer extends GroovyTransformer<String> {

    @Override
    public Type targetType() {
        return String.class;
    }

    @Override
    public @NonNull Supplier<GroovyScript> helperSupplier() {
        return () -> GroovyScript.create().withMode(EvaluatingMode.G_STRING);
    }

    @Nullable
    @Override
    public String transform(@NonNull ProcessResult gherkinArgumentAfterProcessing, @NonNull Type toValueType) {
        return gherkinArgumentAfterProcessing.getResultAsString();
    }
}
