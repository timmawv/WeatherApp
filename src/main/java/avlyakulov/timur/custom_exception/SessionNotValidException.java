package avlyakulov.timur.custom_exception;

public class SessionNotValidException extends RuntimeException {
    public SessionNotValidException() {
        super();
    }

    public SessionNotValidException(String message) {
        super(message);
    }

    public SessionNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionNotValidException(Throwable cause) {
        super(cause);
    }

}