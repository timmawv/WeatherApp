package avlyakulov.timur.custom_exception;

public class BadHttpRequest extends RuntimeException {

    public BadHttpRequest() {
    }

    public BadHttpRequest(String message) {
        super(message);
    }
}
