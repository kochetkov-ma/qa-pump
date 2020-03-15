package ru.iopump.qa.cucumber.processor;

import javax.annotation.Nullable;
import lombok.NonNull;
import ru.iopump.qa.annotation.PumpApi;

@PumpApi
public interface Processor<HELPER> {

    ProcessResult process(@NonNull String rawGherkinArgument, @Nullable HELPER helper);
}
