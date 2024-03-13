package avlyakulov.timur.service;

import avlyakulov.timur.custom_exception.ModelNotFoundException;
import avlyakulov.timur.dao.UserDao;
import avlyakulov.timur.model.User;
import avlyakulov.timur.util.BCryptUtil;
import jakarta.persistence.NoResultException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserService {

    private final UserDao userDao = new UserDao();

    public void createUser(User user) {
        userDao.create(user);
    }

    public User getUserById(int userId) {
        return userDao.getById(userId);
    }

    public User logUserByCredentials(String login, String password) {
        User user;
        try {
            user = userDao.getUserByLogin(login);
        } catch (NoResultException e) {
            throw new ModelNotFoundException("Login or password isn't correct");
        }
        if (!BCryptUtil.isPasswordCorrect(password, user.getPassword())) {
            throw new ModelNotFoundException("Login or password isn't correct");
        }
        return user;
    }
}