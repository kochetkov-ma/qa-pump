package ru.iopump.qa.cucumber.transformer;

import lombok.NonNull;
import ru.iopump.qa.annotation.PumpApi;
import ru.iopump.qa.cucumber.processor.ProcessResult;
import ru.iopump.qa.cucumber.processor.Processor;

@PumpApi
public interface Transformer<TARGET, P_TYPE, P_EXCEPTION extends RuntimeException, PROCESSOR extends Processor<P_TYPE, P_EXCEPTION>> {

    Class<PROCESSOR> preProcessorClass();

    TARGET transform(@NonNull ProcessResult<P_TYPE, P_EXCEPTION> gherkinArgumentAfterProcessing);
}
