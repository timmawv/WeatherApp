package avlyakulov.timur.custom_exception;

public class TooManyLocationsException extends RuntimeException {
    public TooManyLocationsException() {
    }

    public TooManyLocationsException(String message) {
        super(message);
    }

    public TooManyLocationsException(String message, Throwable cause) {
        super(message, cause);
    }

    public TooManyLocationsException(Throwable cause) {
        super(cause);
    }

}