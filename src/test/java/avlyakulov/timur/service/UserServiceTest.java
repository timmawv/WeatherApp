package avlyakulov.timur.service;

import avlyakulov.timur.custom_exception.ModelAlreadyExistsException;
import avlyakulov.timur.model.User;
import avlyakulov.timur.util.DeployConfigurationType;
import avlyakulov.timur.util.HibernateSingletonUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static SessionFactory sessionFactory;

    private final UserService userService = new UserService();

    @BeforeAll
    static void setUp() {
        sessionFactory = HibernateSingletonUtil.getSessionFactory(DeployConfigurationType.TEST);
    }

    @AfterEach
    void tearTables() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.createQuery("delete from User ").executeUpdate();
            session.createQuery("delete from Session ").executeUpdate();
            session.createQuery("delete from Location ").executeUpdate();

            session.getTransaction().commit();
        }
    }

    @AfterAll
    static void tear() {
        HibernateSingletonUtil.closeSessionFactory();
    }

    //MethodName_ExpectedBehavior_StateUnderTest
    @Test
    public void createUser_userWasCreated_ValidCredentials() {
        User user = new User("timur", "111");
        userService.createUser(user);
//        User userCreated = userService.getUserById();
//        assertEquals(user.getId(), userCreated.getId());
//        assertEquals(user.getLogin(), userCreated.getLogin());
//        assertEquals(user.getPassword(), userCreated.getPassword());
    }

    @Test
    public void createUser_userNotCreated_LoginAlreadyExist() {
        User user = new User("timur", "111");
        userService.createUser(user);
        User user2 = new User("timur", "111");
        assertThrows(ModelAlreadyExistsException.class, () -> userService.createUser(user2));
    }
}