package avlyakulov.timur.util.authentication;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import org.thymeleaf.context.Context;

public class LoginRegistrationValidation {
    private static final String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$";

    private static final String nameFieldError = "error_field";

    public static boolean isFieldEmpty(Context context, String... parameters) {
        for (String param : parameters) {
            if (param == null || param.isBlank()) {
                setErrorToContext(context, "One or more fields are empty. Please avoid empty fields");
                return true;
            }
        }
        return false;
    }

    public static boolean isPasswordTheSameAndStrong(String password, String confirmPassword, Context context) {
        if (isPasswordsTheSame(password, confirmPassword, context)) {
            if (password.length() >= 6 && password.length() <= 16) {
                if (password.matches(passwordRegex)) {
                    Zxcvbn zxcvbn = new Zxcvbn();
                    Strength strength = zxcvbn.measure(password);
                    if (strength.getScore() >= 3) {
                        return true;
                    } else {
                        setErrorToContext(context, "Your password is to easy. Here is some suggestions to help you <br>" + strength.getFeedback().getSuggestions());
                        return false;
                    }
                } else {
                    setErrorToContext(context, "Your password must contain one capital letter one small letter and one number");
                    return false;
                }
            } else {
                setErrorToContext(context, "The length of password has to be from 6 to 16");
                return false;
            }
        } else {
            return false;
        }
    }

    private static boolean isPasswordsTheSame(String password, String confirmPassword, Context context) {
        if (password.equals(confirmPassword)) {
            return true;
        } else {
            setErrorToContext(context, "The passwords aren't same. Please enter the same passwords");
            return false;
        }
    }

    private static void setErrorToContext(Context context, String errorMessage) {
        context.setVariable(nameFieldError, errorMessage);
    }
}
