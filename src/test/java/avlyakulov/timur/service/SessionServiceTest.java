package avlyakulov.timur.service;

import avlyakulov.timur.custom_exception.SessionNotValid;
import avlyakulov.timur.dao.SessionDao;
import avlyakulov.timur.dao.UserDao;
import avlyakulov.timur.dto.UserDto;
import avlyakulov.timur.model.User;
import avlyakulov.timur.util.hibernate.DeployConfigurationType;
import avlyakulov.timur.util.hibernate.HibernateSingletonUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SessionServiceTest {

    private static SessionFactory sessionFactory;

    private UserDao userDao = new UserDao();

    private SessionDao sessionDao = new SessionDao();

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private SessionService sessionService = new SessionService();

    @BeforeAll
    static void setUp() {
        sessionFactory = HibernateSingletonUtil.getSessionFactory(DeployConfigurationType.TEST);
    }

    @AfterEach
    void tearTables() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.createQuery("delete from Session ").executeUpdate();
            session.createQuery("delete from User ").executeUpdate();

            session.getTransaction().commit();
        }
        sessionService = new SessionService();
    }

    @AfterAll
    static void tear() {
        HibernateSingletonUtil.closeSessionFactory();
    }

    @Test
    public void createSession_sessionWasCreated_validData() {
        User user = new User("timur", "123");
        userDao.create(user);

        avlyakulov.timur.model.Session session = sessionService.createSession(user);
        List<avlyakulov.timur.model.Session> sessions = sessionDao.findAll();

        assertNotNull(session.getId());
        assertEquals(FORMATTER.format(LocalDateTime.now().plus(30, ChronoUnit.MINUTES)), FORMATTER.format(session.getExpiresAt()));
        assertEquals(user, session.getUser());
        assertEquals(1, sessions.size());
    }

    @Test
    public void getUserSessionIfItNotExpired_getSession_sessionNotExpired() {
        User user = new User("timur", "123");
        userDao.create(user);
        avlyakulov.timur.model.Session session = sessionService.createSession(user);
        UUID id = session.getId();

        avlyakulov.timur.model.Session userSession = sessionService.getUserSessionIfItNotExpired(id.toString());

        assertEquals(id, userSession.getId());
        assertEquals(user, session.getUser());
    }

    @Test
    public void getUserSessionIfItNotExpired_notGetSession_sessionNotExist() {
        User user = new User("timur", "123");
        userDao.create(user);
        UUID id = UUID.randomUUID();

        SessionNotValid sessionNotValid = assertThrows(SessionNotValid.class, () -> sessionService.getUserSessionIfItNotExpired(id.toString()));
        assertEquals("User session was expired or it doesn't exits", sessionNotValid.getMessage());
    }

    @Test
    public void getUserSessionIfItNotExpired_notGetSession_sessionIsExpired() {
        sessionService.setMinutesSessionExist(0);
        User user = new User("timur", "123");
        userDao.create(user);
        avlyakulov.timur.model.Session session = sessionService.createSession(user);
        UUID id = session.getId();

        SessionNotValid sessionNotValid = assertThrows(SessionNotValid.class, () -> sessionService.getUserSessionIfItNotExpired(id.toString()));
        assertEquals("User session was expired", sessionNotValid.getMessage());
    }

    @Test
    public void getUserDtoByHisSession_getUserDto_sessionIsValid() {
        User user = new User("timur", "123");
        userDao.create(user);
        avlyakulov.timur.model.Session session = sessionService.createSession(user);

        UserDto userDto = sessionService.getUserDtoByHisSession(session);

        assertEquals(user.getId(), userDto.getUserId());
        assertEquals("timur", userDto.getLogin());
    }

    @Test
    public void deleteSessionById_sessionWasDeleted_sessionExist() {
        User user = new User("timur", "123");
        userDao.create(user);
        avlyakulov.timur.model.Session session = sessionService.createSession(user);
        UUID id = session.getId();

        sessionService.deleteSessionById(id.toString());

        List<avlyakulov.timur.model.Session> sessions = sessionDao.findAll();
        assertEquals(0, sessions.size());
    }

    @Test
    public void deleteSessionByUserId_sessionWasDeleted_sessionExist() {
        User user = new User("timur", "123");
        userDao.create(user);
        sessionService.createSession(user);

        sessionService.deleteSessionByUserId(user.getId());

        List<avlyakulov.timur.model.Session> sessions = sessionDao.findAll();
        assertEquals(0, sessions.size());
    }

    @Test
    public void isUserValid_sessionIsValid_sessionExist() {
        User user = new User("timur", "123");
        userDao.create(user);
        avlyakulov.timur.model.Session session = sessionService.createSession(user);
        UUID id = session.getId();

        boolean isUserSessionValid = sessionService.isUserSessionValid(id.toString());

        List<avlyakulov.timur.model.Session> sessions = sessionDao.findAll();
        assertEquals(1, sessions.size());
        assertTrue(isUserSessionValid);
    }

    @Test
    public void isUserValid_sessionNotValid_sessionExist() {
        sessionService.setMinutesSessionExist(0);
        User user = new User("timur", "123");
        userDao.create(user);
        avlyakulov.timur.model.Session session = sessionService.createSession(user);
        UUID id = session.getId();

        boolean isUserSessionValid = sessionService.isUserSessionValid(id.toString());

        List<avlyakulov.timur.model.Session> sessions = sessionDao.findAll();
        assertEquals(1, sessions.size());
        assertFalse(isUserSessionValid);
    }
}