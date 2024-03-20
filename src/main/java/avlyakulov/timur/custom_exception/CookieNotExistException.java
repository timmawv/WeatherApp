package avlyakulov.timur.custom_exception;

public class CookieNotExistException extends RuntimeException {
    public CookieNotExistException() {
        super();
    }

    public CookieNotExistException(String message) {
        super(message);
    }

    public CookieNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public CookieNotExistException(Throwable cause) {
        super(cause);
    }

}