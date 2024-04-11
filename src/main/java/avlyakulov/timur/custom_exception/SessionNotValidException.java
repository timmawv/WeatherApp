package avlyakulov.timur.custom_exception;

public class SessionNotValidException extends RuntimeException {
    public SessionNotValidException() {
        super();
    }

    public SessionNotValidException(String message) {
        super(message);
    }

}