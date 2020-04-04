package ru.iopump.qa.cucumber.processor;

import javax.annotation.Nullable;
import ru.iopump.qa.annotation.PumpApi;

@PumpApi
public interface Processor<RESULT, EXCEPTION extends RuntimeException> {

    ProcessResult<RESULT, EXCEPTION> process(@Nullable String rawGherkinArgument);
}
