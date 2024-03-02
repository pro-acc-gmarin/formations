package utils.exception;

public class RecordNotFoundException extends Exception {

    private static final long serialVersionUID = 7718828512143293558L;

    public RecordNotFoundException() {
        super();
    }

    public RecordNotFoundException(final String message, Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public RecordNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public RecordNotFoundException(final String message) {
        super(message);
    }

    public RecordNotFoundException(final Throwable cause) {
        super(cause);
    }

}
