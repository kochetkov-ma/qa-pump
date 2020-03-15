package ru.iopump.qa.cucumber.transformer;

import java.lang.reflect.Type;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import lombok.NonNull;
import ru.iopump.qa.annotation.PumpApi;
import ru.iopump.qa.cucumber.processor.ProcessResult;
import ru.iopump.qa.cucumber.processor.Processor;

@PumpApi
public interface Transformer<TARGET, HELPER, PROCESSOR extends Processor<HELPER>> {

    int priority();

    @NonNull
    Type targetType();

    @Nullable
    Supplier<HELPER> helperSupplier();

    @NonNull
    Class<PROCESSOR> processorClass();

    @Nullable
    TARGET transform(@NonNull ProcessResult gherkinArgumentAfterProcessing);

    String toString();
}
