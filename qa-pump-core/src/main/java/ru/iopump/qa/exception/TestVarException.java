package ru.iopump.qa.exception;

import javax.annotation.Nullable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * No constructors. Only static constructors and 'with' methods witch return copy of this 'PumpCoreException'.
 */
@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TestVarException extends PumpException {

    private static final long serialVersionUID = 1537248756540206353L;

    protected TestVarException(String formattedMessage, Object... arguments) {
        super(formattedMessage, arguments);
    }

    protected TestVarException(String formattedMessage, Throwable cause, Object... arguments) {
        super(formattedMessage, cause, arguments);
    }

    protected TestVarException(Throwable cause) {
        super(cause);
    }

    @NonNull
    public static TestVarException of() {
        return new TestVarException();
    }

    @NonNull
    public static TestVarException of(@Nullable Throwable cause) {
        return new TestVarException(cause);
    }

    @NonNull
    public static TestVarException of(@Nullable String formattedMessage, @Nullable Object... arguments) {
        return new TestVarException(formattedMessage, arguments);
    }

    @Override
    public @NonNull TestVarException withMsg(@Nullable String formattedMessage, @Nullable Object... arguments) {
        return (TestVarException)super.withMsg(formattedMessage, arguments);
    }

    @Override
    public @NonNull TestVarException withCause(Throwable cause) {
        return (TestVarException)super.withCause(cause);
    }
}
