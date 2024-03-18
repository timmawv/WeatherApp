package avlyakulov.timur.custom_exception;

public class ModelAlreadyExistsException extends RuntimeException {
    public ModelAlreadyExistsException() {
        super();
    }

    public ModelAlreadyExistsException(String message) {
        super(message);
    }

    public ModelAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    protected ModelAlreadyExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}