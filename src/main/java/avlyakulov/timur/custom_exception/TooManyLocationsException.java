package avlyakulov.timur.custom_exception;

public class TooManyLocationsException extends RuntimeException {

    public TooManyLocationsException(String message) {
        super(message);
    }

}