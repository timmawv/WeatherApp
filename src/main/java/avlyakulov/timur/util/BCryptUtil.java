package avlyakulov.timur.util;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptUtil {

    public static String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean isPasswordCorrect(String candidatePassword, String userPassword) {
        return BCrypt.checkpw(candidatePassword, userPassword);
    }
}
