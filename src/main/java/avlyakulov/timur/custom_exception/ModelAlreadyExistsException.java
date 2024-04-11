package avlyakulov.timur.custom_exception;

public class ModelAlreadyExistsException extends RuntimeException {


    public ModelAlreadyExistsException(String message) {
        super(message);
    }



}