package avlyakulov.timur.service;

import avlyakulov.timur.dao.UserDao;
import avlyakulov.timur.model.User;
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
}
