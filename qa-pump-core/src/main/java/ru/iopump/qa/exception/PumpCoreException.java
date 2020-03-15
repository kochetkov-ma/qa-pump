package ru.iopump.qa.exception;

import javax.annotation.Nullable;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@SuppressWarnings("unused")
@NoArgsConstructor
public class PumpCoreException extends QaException {

    private static final long serialVersionUID = 2540707445608794577L;

    public PumpCoreException(String formattedMessage, Object... arguments) {
        this(formattedMessage, null, arguments);
    }

    public PumpCoreException(String formattedMessage, Throwable cause, Object... arguments) {
        super(formattedMessage, cause, arguments);
    }

    public PumpCoreException(Throwable cause) {
        super(cause);
    }

    @NonNull
    public static PumpCoreException of() {
        return new PumpCoreException();
    }

    @NonNull
    public static PumpCoreException of(@Nullable String formattedMessage, @Nullable Object... arguments) {
        return formattedMessage == null ? of() : new PumpCoreException(formattedMessage, arguments);
    }

    @NonNull
    public PumpCoreException withMsg(@Nullable String formattedMessage, @Nullable Object... arguments) {
        return (PumpCoreException) of(formattedMessage, arguments).initCause(getCause());
    }

    @NonNull
    public PumpCoreException withCause() {
        return (PumpCoreException) of(getMessage()).initCause(getCause());
    }
}
