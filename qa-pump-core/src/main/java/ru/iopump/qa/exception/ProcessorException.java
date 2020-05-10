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
public final class ProcessorException extends QaException {

    private static final long serialVersionUID = 1537248756540266353L;

    protected ProcessorException(String formattedMessage, Object... arguments) {
        super(formattedMessage, arguments);
    }

    protected ProcessorException(String formattedMessage, Throwable cause, Object... arguments) {
        super(formattedMessage, cause, arguments);
    }

    protected ProcessorException(Throwable cause) {
        super(cause);
    }

    @NonNull
    public static ProcessorException of() {
        return new ProcessorException();
    }

    @NonNull
    public static ProcessorException of(@Nullable Throwable cause) {
        return new ProcessorException(cause);
    }

    @NonNull
    public static ProcessorException of(@Nullable String formattedMessage, @Nullable Object... arguments) {
        return new ProcessorException(formattedMessage, arguments);
    }

    public final ProcessorException withMsg(@Nullable String formattedMessage, @Nullable Object... arguments) {
        return new ProcessorException(formattedMessage, getCause(), arguments);
    }

    public final ProcessorException withCause(Throwable cause) {
        return new ProcessorException(getMessage() == null ? Str.toStr(cause) : getMessage(), cause);
    }
}
