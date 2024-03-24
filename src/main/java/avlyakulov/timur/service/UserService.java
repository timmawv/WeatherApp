package avlyakulov.timur.service;

import avlyakulov.timur.custom_exception.ModelNotFoundException;
import avlyakulov.timur.dao.UserDao;
import avlyakulov.timur.model.User;
import avlyakulov.timur.util.BCryptUtil;
import jakarta.persistence.NoResultException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
public class UserService {

    private final UserDao userDao = new UserDao();

    private final BCryptUtil bCryptUtil = new BCryptUtil();


    public void createUser(User user) {
        user.setPassword(bCryptUtil.encryptPassword(user.getPassword()));
        userDao.create(user);
    }

    public User logUserByCredentials(String login, String password) {
        User user;
        try {
            user = userDao.getUserByLogin(login);
        } catch (NoResultException e) {
            log.info("Invalid login");
            throw new ModelNotFoundException("Login or password isn't correct");
        }
        if (bCryptUtil.isPasswordCorrect(password, user.getPassword())) {
            return user;
        } else {
            log.info("Invalid password");
            throw new ModelNotFoundException("Login or password isn't correct");
        }
    }
}