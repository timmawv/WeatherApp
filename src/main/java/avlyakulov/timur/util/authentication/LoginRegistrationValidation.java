package avlyakulov.timur.util.authentication;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import org.thymeleaf.context.Context;

public class LoginRegistrationValidation {
    private static final String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$";

    public static boolean isFieldEmpty(Context context, String... parameters) {
        for (String param : parameters) {
            if (param == null || param.isBlank()) {
                context.setVariable("empty_field_error", "One or more fields are empty. Please avoid empty fields");
                return true;
            }
        }
        return false;
    }

    private static boolean isPasswordsTheSame(String password, String confirmPassword, Context context) {
        if (password.equals(confirmPassword)) {
            return true;
        } else {
            context.setVariable("confirm_password_error", "The passwords aren't same. Please enter the same passwords");
            return false;
        }
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
                        context.setVariable("password_strength_error", "Your password is to easy. Here is some suggestions to help you");
                        context.setVariable("suggestions", strength.getFeedback().getSuggestions());
                        return false;
                    }
                } else {
                    context.setVariable("password_matcher_error", "Your password must contain one capital letter one small letter and one number");
                    return false;
                }
            } else {
                context.setVariable("password_length_error", "The length of password has to be from 6 to 16");
                return false;
            }
        } else {
            return false;
        }
    }
}
