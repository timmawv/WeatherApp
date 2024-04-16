package avlyakulov.timur.util.authentication;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import org.thymeleaf.context.Context;

public class LoginRegistrationValidation {
    private final String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$";

    private final String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    private final String emailTypeRegex = ".*\\.[a-zA-Z0-9]{2,}";

    private final String nameFieldError = "error_field";


    public boolean isPasswordTheSameAndStrong(String password, String confirmPassword, Context context) {
        if (!isPasswordsTheSame(password, confirmPassword)) {
            setErrorToContext(context, "The passwords aren't same. Please enter the same passwords");
            return false;
        }
        if (!(password.length() >= 3 && password.length() <= 16)) {
            setErrorToContext(context, "The length of password has to be from 3 to 16");
            return false;
        }
        if (!password.matches(passwordRegex)) {
            setErrorToContext(context, "Your password must contain one capital letter one small letter and one number");
            return false;
        }
        Zxcvbn zxcvbn = new Zxcvbn();
        Strength strength = zxcvbn.measure(password);
        if (strength.getScore() >= 3) {
            return true;
        } else {
            setErrorToContext(context, "Your password is to easy. Here is some suggestions to help you <br>" + strength.getFeedback().getSuggestions());
            return false;
        }
    }


    private boolean isPasswordsTheSame(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    public boolean isUserLoginValid(String login, Context context) {
        if (login.matches(emailRegex)) {
            return true;
        }
        if (login.contains("@") || login.matches(emailTypeRegex)) {
            setErrorToContext(context, "Your email isn't valid. Please enter valid email");
            return false;
        }
        return isLoginValid(login, context);
    }


    private boolean isLoginValid(String login, Context context) {
        if (!login.contains(" ") && (login.length() >= 2 && login.length() <= 16)) {
            return true;
        } else {
            setErrorToContext(context, "Your login can't contain space. Your length has to be from 2 to 16 symbols.");
            return false;
        }
    }

    private void setErrorToContext(Context context, String errorMessage) {
        context.setVariable(nameFieldError, errorMessage);
    }
}
