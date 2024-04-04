package avlyakulov.timur.dao;

import avlyakulov.timur.IntegrationTestBase;
import avlyakulov.timur.custom_exception.ModelAlreadyExistsException;
import avlyakulov.timur.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserDaoTest extends IntegrationTestBase {

    private static User TIMUR = new User("timur", "123");

    private UserDao userDao;

    @BeforeEach
    void setUp() {
        userDao = new UserDao();
        TIMUR = new User("timur", "123");
    }

    @Test
    void createUserUserNotExistInDB() {
        userDao.create(TIMUR);

        List<User> users = userDao.findAll();

        assertThat(users).hasSize(1);
        assertThat(users.get(0)).isEqualTo(TIMUR);
    }

    @Test
    void createUser_throwException_UserAlreadyExistInDB() {
        userDao.create(TIMUR);

        User testUser = new User("timur", "333");

        ModelAlreadyExistsException modelAlreadyExistsException = assertThrows(ModelAlreadyExistsException.class, () -> userDao.create(testUser));
        assertThat(modelAlreadyExistsException.getMessage()).isEqualTo("User with such login name already exists");

        List<User> users = userDao.findAll();
        assertThat(users).hasSize(1);
    }

    @Test
    void getUserByLoginUserExistInDB() {
        userDao.create(TIMUR);

        User maybeUser = userDao.getUserByLogin(TIMUR.getLogin());

        assertThat(maybeUser).isEqualTo(TIMUR);
    }

    @Test
    void getUserByLoginUserNotExistInDB() {
        User maybeUser = userDao.getUserByLogin(TIMUR.getLogin());

        assertThat(maybeUser).isNull();
    }
}