package ru.iopump.qa.exception;

import javax.annotation.Nullable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.iopump.qa.util.Str;

/**
 * No constructors. Only static constructors and 'with' methods witch return copy of this 'PumpCoreException'.
 */
@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class PumpException extends QaException {

    private static final long serialVersionUID = 2540707445608794577L;

    protected PumpException(String formattedMessage, Object... arguments) {
        super(formattedMessage, arguments);
    }

    protected PumpException(String formattedMessage, Throwable cause, Object... arguments) {
        super(formattedMessage, cause, arguments);
    }

    protected PumpException(Throwable cause) {
        super(cause);
    }

    @NonNull
    public static PumpException of() {
        return new PumpException();
    }

    @NonNull
    public static PumpException of(@Nullable Throwable cause) {
        return new PumpException(cause);
    }

    @NonNull
    public static PumpException of(@Nullable String formattedMessage, @Nullable Object... arguments) {
        return new PumpException(formattedMessage, arguments);
    }

    /**
     * New copy of this {@link PumpException} with new message.
     */
    @NonNull
    public PumpException withMsg(@Nullable String formattedMessage, @Nullable Object... arguments) {
        return new PumpException(formattedMessage, getCause(), arguments);
    }

    /**
     * New copy of this {@link PumpException} with new cause.
     */
    @NonNull
    public PumpException withCause(Throwable cause) {
        return new PumpException(getMessage() == null ? Str.toStr(cause) : getMessage(), cause);
    }
}
