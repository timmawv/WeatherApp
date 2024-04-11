package avlyakulov.timur.util.authentication;

public class CheckAnyEmptyField {

    public static boolean isAnyFieldNullOrEmpty(String... parameters) {
        for (String param : parameters) {
            if (param == null || param.isBlank())
                return true;
        }
        return false;
    }
}