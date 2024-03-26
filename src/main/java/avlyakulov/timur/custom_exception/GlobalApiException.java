package avlyakulov.timur.custom_exception;

public class GlobalApiException extends RuntimeException {
    public GlobalApiException() {
    }

    public GlobalApiException(String message) {
        super(message);
    }
}