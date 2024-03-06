package avlyakulov.timur.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class BCryptUtil {

    public static String encryptPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }
}
