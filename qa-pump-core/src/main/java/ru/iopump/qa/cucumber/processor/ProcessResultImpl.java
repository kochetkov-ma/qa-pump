package ru.iopump.qa.cucumber.processor;

import groovy.lang.GroovyRuntimeException;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
class ProcessResultImpl implements ProcessResult {
    Object result;
    @Getter
    String resultAsString;
    GroovyRuntimeException processException;
    @Getter
    String sourceString;

    @Override
    public Optional<Object> getResult() {
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<GroovyRuntimeException> getProcessException() {
        return Optional.ofNullable(processException);
    }
}
