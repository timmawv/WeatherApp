package avlyakulov.timur.custom_exception;

public class SessionNotValid extends RuntimeException {
    public SessionNotValid() {
        super();
    }

    public SessionNotValid(String message) {
        super(message);
    }

    public SessionNotValid(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionNotValid(Throwable cause) {
        super(cause);
    }

}