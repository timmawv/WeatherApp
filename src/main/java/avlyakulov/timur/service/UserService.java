package avlyakulov.timur.service;

import avlyakulov.timur.custom_exception.ModelNotFoundException;
import avlyakulov.timur.custom_exception.UserCredentialsException;
import avlyakulov.timur.dao.UserDao;
import avlyakulov.timur.model.User;
import avlyakulov.timur.util.BCryptUtil;
import jakarta.persistence.NoResultException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserService {

    private UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void createUser(User user) {
        user.setPassword(BCryptUtil.encryptPassword(user.getPassword()));
        userDao.create(user);
    }

    public User logUserByCredentials(String login, String password) {
        User user = userDao.getUserByLogin(login);
        if (user == null) {
            log.info("Invalid login");
            throw new ModelNotFoundException("Login or password isn't correct");
        } else {
            if (BCryptUtil.isPasswordCorrect(password, user.getPassword())) {
                return user;
            } else {
                log.info("Invalid password");
                throw new UserCredentialsException("Login or password isn't correct");
            }
        }
    }
}