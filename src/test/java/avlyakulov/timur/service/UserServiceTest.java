package avlyakulov.timur.service;

import avlyakulov.timur.custom_exception.ModelAlreadyExistsException;
import avlyakulov.timur.custom_exception.ModelNotFoundException;
import avlyakulov.timur.dao.UserDao;
import avlyakulov.timur.model.User;
import avlyakulov.timur.util.BCryptUtil;
import avlyakulov.timur.util.hibernate.DeployConfigurationType;
import avlyakulov.timur.util.hibernate.HibernateSingletonUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static SessionFactory sessionFactory;

    private final UserDao userDao = new UserDao();

    private final BCryptUtil bCryptUtil = new BCryptUtil();

    private UserService userService = new UserService();

    @BeforeAll
    static void setUp() {
        sessionFactory = HibernateSingletonUtil.getSessionFactory(DeployConfigurationType.TEST);
    }

    @AfterAll
    static void tear() {
        HibernateSingletonUtil.closeSessionFactory();
    }

    @AfterEach
    void tearTables() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.createQuery("delete from User ").executeUpdate();

            session.getTransaction().commit();
        }
    }


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