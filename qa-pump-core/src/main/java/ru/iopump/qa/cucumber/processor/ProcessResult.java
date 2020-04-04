package ru.iopump.qa.cucumber.processor;

import java.util.Optional;
import lombok.NonNull;
import ru.iopump.qa.annotation.PumpApi;

@PumpApi
public interface ProcessResult<RESULT, EXCEPTION extends RuntimeException> {
    /**
     * Result after processing.
     * Rule: If suppressed exception not exist - result MUST be exist,
     *
     * @return Result of processing.
     */
    Optional<RESULT> getResult();

    /**
     * String representation of result.
     * It may be {@link ru.iopump.qa.util.Str#toStr(Object)} or raw value or transformed raw value.
     *
     * @return String representation of result.
     */
    @NonNull
    String getResultAsString();

    /**
     * Suppressed exceptions during processing.
     * Not all exceptions will be suppressed but some.<br/>
     * Rule: If suppressed exception exists - result may be empty,
     * but {@link String} representation of result must exist {@link #getResultAsString}.
     * And not every exception must be suppressed.
     *
     * @return Suppressed exceptions during processing.
     */
    Optional<EXCEPTION> getProcessException();

    /**
     * Check if processing was success but result is 'null'.
     * Result optional is null and exception optional is null.
     * Just for convenience.
     *
     * @return true - if evaluation was success but result is 'null'
     */
    default boolean isNullResult() {
        return getResult().isEmpty() && getProcessException().isEmpty();
    }
}