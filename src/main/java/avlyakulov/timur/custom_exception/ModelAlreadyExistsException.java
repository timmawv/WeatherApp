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

}