package ru.iopump.qa.cucumber.processor;

import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
class ProcessResultImpl<RESULT, EXCEPTION extends RuntimeException> implements ProcessResult<RESULT, EXCEPTION> {
    RESULT result;
    @Getter
    String resultAsString;
    EXCEPTION processException;

    @Override
    public Optional<RESULT> getResult() {
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<EXCEPTION> getProcessException() {
        return Optional.ofNullable(processException);
    }
}
