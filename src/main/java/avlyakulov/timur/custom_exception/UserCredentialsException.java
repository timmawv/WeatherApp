package avlyakulov.timur.custom_exception;

public class UserCredentialsException extends RuntimeException {

    public UserCredentialsException(String message) {
        super(message);
    }

}
