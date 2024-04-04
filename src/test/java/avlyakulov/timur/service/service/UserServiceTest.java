package avlyakulov.timur.service.service;

import avlyakulov.timur.custom_exception.ModelAlreadyExistsException;
import avlyakulov.timur.custom_exception.ModelNotFoundException;
import avlyakulov.timur.dao.UserDao;
import avlyakulov.timur.model.User;
import avlyakulov.timur.IntegrationTestBase;
import avlyakulov.timur.service.UserService;
import avlyakulov.timur.util.BCryptUtil;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest extends IntegrationTestBase {

    private static SessionFactory sessionFactory;

    private final UserDao userDao = new UserDao();

    private final BCryptUtil bCryptUtil = new BCryptUtil();

    private UserService userService = new UserService();


    //MethodName_ExpectedBehavior_StateUnderTest
    @Test
    public void createUser_userWasCreated_ValidCredentials() {
        User user = new User("timur", "111");

        userService.createUser(user);

        User userCreated = userDao.getUserByLogin("timur");
        List<User> users = userDao.findAll();

        assertNotNull(userCreated.getId());
        assertEquals("timur", userCreated.getLogin());
        assertTrue(bCryptUtil.isPasswordCorrect("111", user.getPassword()));
        assertEquals(1, users.size());
    }

    @Test
    public void createUser_userNotCreated_LoginAlreadyExist() {
        //given
        User user = new User("timur", "111");
        userService.createUser(user);
        //when
        //then
        User user2 = new User("timur", "111");
        assertThrows(ModelAlreadyExistsException.class, () -> userService.createUser(user2));
        List<User> users = userDao.findAll();
        assertEquals(1, users.size());
    }

    @Test
    public void logUser_userWasLogged_validCredentials() {
        User user = new User("timur", "111");
        userService.createUser(user);
        User userExpected = userService.logUserByCredentials("timur", "111");
        assertEquals(user.getId(), userExpected.getId());
        assertEquals(user.getLogin(), userExpected.getLogin());
        assertEquals(user.getPassword(), userExpected.getPassword());
    }

    @Test
    public void logUser_userWasNotLogged_invalidLogin() {
        User user = new User("timur", "111");
        userService.createUser(user);
        ModelNotFoundException modelNotFoundException = assertThrows(ModelNotFoundException.class, () -> userService.logUserByCredentials("sssss", ""));
        assertEquals("Login or password isn't correct", modelNotFoundException.getMessage());
    }

    @Test
    public void logUser_userWasNotLogged_invalidPassword() {
        User user = new User("timur", "111");
        userService.createUser(user);

        ModelNotFoundException modelNotFoundException = assertThrows(ModelNotFoundException.class, () -> userService.logUserByCredentials("timur", "122"));
        assertEquals("Login or password isn't correct", modelNotFoundException.getMessage());
    }
}