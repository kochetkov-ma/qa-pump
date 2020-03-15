package ru.iopump.qa.exception;

import javax.annotation.Nullable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.iopump.qa.util.ClassUtil;

/**
 * No constructors. Only static constructors and 'with' methods witch return copy of this 'PumpCoreException'.
 */
@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PumpCoreException extends QaException {

    private static final long serialVersionUID = 2540707445608794577L;

    protected PumpCoreException(String formattedMessage, Object... arguments) {
        super(formattedMessage, arguments);
    }

    protected PumpCoreException(String formattedMessage, Throwable cause, Object... arguments) {
        super(formattedMessage, cause, arguments);
    }

    protected PumpCoreException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor.
     */
    @NonNull
    public static PumpCoreException of() {
        return new PumpCoreException();
    }

    /**
     * Constructor.
     */
    @NonNull
    public static PumpCoreException of(@Nullable Throwable cause) {
        return cause == null ? of() : of().withCause(cause);
    }

    /**
     * Constructor.
     */
    @NonNull
    public static PumpCoreException of(@Nullable String formattedMessage, @Nullable Object... arguments) {
        return formattedMessage == null ? of() : new PumpCoreException(formattedMessage, arguments);
    }

    /**
     * New copy of this {@link PumpCoreException} with new message.
     */
    @NonNull
    public PumpCoreException withMsg(@Nullable String formattedMessage, @Nullable Object... arguments) {
        return (PumpCoreException) of(formattedMessage, arguments).initCause(getCause());
    }

    /**
     * New copy of this {@link PumpCoreException} with new cause.
     */
    @NonNull
    public PumpCoreException withCause(Throwable cause) {
        return cause == null ? of(getMessage()) : (PumpCoreException) of(getMessage() == null ?
            ClassUtil.getClass(cause).getName() : getMessage()).initCause(cause);
    }
}
