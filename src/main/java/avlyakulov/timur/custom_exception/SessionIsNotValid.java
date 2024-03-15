package avlyakulov.timur.custom_exception;

public class SessionIsNotValid extends RuntimeException {
    public SessionIsNotValid() {
        super();
    }

    public SessionIsNotValid(String message) {
        super(message);
    }

    public SessionIsNotValid(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionIsNotValid(Throwable cause) {
        super(cause);
    }

    protected SessionIsNotValid(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}