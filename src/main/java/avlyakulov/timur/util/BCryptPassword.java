package avlyakulov.timur.util;

import org.mindrot.jbcrypt.BCrypt;

public interface BCryptPassword {

    default String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    default boolean isPasswordCorrect(String candidatePassword, String userPassword) {
        return BCrypt.checkpw(candidatePassword, userPassword);
    }
}