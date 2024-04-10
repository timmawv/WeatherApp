package avlyakulov.timur.service;

import avlyakulov.timur.custom_exception.UserCredentialsException;
import avlyakulov.timur.dao.UserDao;
import avlyakulov.timur.model.User;
import avlyakulov.timur.util.BCryptPassword;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserService implements BCryptPassword {

    private UserDao userDao;


    public UserService(UserDao userDao) {
        this.userDao = userDao;

    }

    public void createUser(User user) {
        user.setPassword(encryptPassword(user.getPassword()));
        userDao.create(user);
    }

    public User getUserByLoginAndPassword(String login, String password) {
        User user = userDao.getUserByLogin(login);
        if (user == null) {
            throw new UserCredentialsException("Login or password isn't correct");
        }
        if (!isPasswordCorrect(password, user.getPassword())) {
            throw new UserCredentialsException("Login or password isn't correct");
        }
        return user;
    }
}