package avlyakulov.timur.custom_exception;

public class GlobalApiException extends RuntimeException {

    public GlobalApiException(String message) {
        super(message);
    }
}