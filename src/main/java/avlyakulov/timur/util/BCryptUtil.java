package avlyakulov.timur.util;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptUtil {

    public String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean isPasswordCorrect(String candidatePassword, String userPassword) {
        return BCrypt.checkpw(candidatePassword, userPassword);
    }
}
