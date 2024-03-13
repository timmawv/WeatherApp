package avlyakulov.timur.util;

import org.thymeleaf.context.Context;

public class ContextUtil {
    private static final String nameFieldError = "error_field";

    public static void setErrorToContext(Context context, String errorMessage) {
        context.setVariable(nameFieldError, errorMessage);
    }
}