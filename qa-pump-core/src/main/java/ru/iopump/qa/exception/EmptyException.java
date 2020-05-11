package ru.iopump.qa.exception;

/**
 * Exception with no stacktrace.
 */
@SuppressWarnings("unused")
public class EmptyException extends QaException {

    private static final long serialVersionUID = 6943595892576399571L;

    public EmptyException() {
    }

    public EmptyException(String formattedMessage, Object... arguments) {
        super(formattedMessage, arguments);
    }

    public EmptyException(String formattedMessage, Throwable cause, Object... arguments) {
        super(formattedMessage, cause, arguments);
    }

    public EmptyException(Throwable cause) {
        super(cause);
    }

    @Override
    public synchronized Throwable fillInStackTrace() { // NOPMD
        return this;
    }
}
