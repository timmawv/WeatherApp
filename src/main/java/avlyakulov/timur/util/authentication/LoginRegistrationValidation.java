package avlyakulov.timur.util.authentication;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import org.thymeleaf.context.Context;

public class LoginRegistrationValidation {
    private static final String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$";

    private static final String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private static final String cityNameRegex = ".*\\d+.*";

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
            if (password.length() >= 3 && password.length() <= 16) {
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
                setErrorToContext(context, "The length of password has to be from 3 to 16");
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

    public static boolean isUserLoginValid(String login, Context context) {
        if (login.matches(emailRegex)) {
            return true;
        } else {
            return isLoginValid(login, context);
        }
    }


    private static boolean isLoginValid(String login, Context context) {
        if (!login.contains(" ") && (login.length() >= 2 && login.length() <= 10)) {
            return true;
        } else {
            setErrorToContext(context, "Your login can't contain space. Your length has to be from 2 to 10 symbols.");
            return false;
        }
    }

    public static boolean isCityNameValid(String cityName, Context context) {
        if (cityName.isBlank() || cityName.contains(" ") || cityName.matches(cityNameRegex)) {
            context.setVariable("error_city_name", "Don't enter numbers, blank name, spaces in name.");
            return false;
        } else {
            return true;
        }
    }

    private static void setErrorToContext(Context context, String errorMessage) {
        context.setVariable(nameFieldError, errorMessage);
    }
}
