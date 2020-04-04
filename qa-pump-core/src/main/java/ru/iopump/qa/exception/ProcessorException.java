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
public class ProcessorException extends PumpException {

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

    @Override
    public @NonNull ProcessorException withMsg(@Nullable String formattedMessage, @Nullable Object... arguments) {
        return (ProcessorException)super.withMsg(formattedMessage, arguments);
    }

    @Override
    public @NonNull ProcessorException withCause(Throwable cause) {
        return (ProcessorException)super.withCause(cause);
    }
}
