package avlyakulov.timur.dao;

import avlyakulov.timur.IntegrationTestBase;
import avlyakulov.timur.custom_exception.ModelAlreadyExistsException;
import avlyakulov.timur.model.Session;
import avlyakulov.timur.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SessionDaoTest extends IntegrationTestBase {

    private static User TIMUR;

    private UUID sessionId = UUID.fromString("4f49f711-cc3f-459e-a9f3-8a6e1656befc");

    private static Session sessionUser;

    private UserDao userDao;

    private SessionDao sessionDao;

    @BeforeEach
    void setUp() {
        TIMUR = new User("timur", "123");
        userDao = new UserDao();
        userDao.create(TIMUR);
        sessionDao = new SessionDao();
        sessionUser = new Session(sessionId, TIMUR);
        sessionUser.setExpiresAt(LocalDateTime.now().plus(30, ChronoUnit.MINUTES));
    }

    @Test
    void createSessionUser_sessionNotExistInDB() {
        sessionDao.create(sessionUser);

        List<Session> sessions = sessionDao.findAll();
        assertThat(sessions).hasSize(1);
        Session maybeSessionUser = sessions.get(0);
        assertThat(maybeSessionUser.getId()).isEqualTo(sessionUser.getId());
        assertThat(maybeSessionUser.getExpiresAt()).isEqualTo(sessionUser.getExpiresAt());
        assertThat(maybeSessionUser.getUser()).isEqualTo(sessionUser.getUser());
    }

    @Test
    void createSessionUser_throwException_userAlreadyHasSessionInDB() {
        sessionDao.create(sessionUser);

        sessionUser = new Session(sessionId, TIMUR);
        sessionUser.setExpiresAt(LocalDateTime.now().plus(30, ChronoUnit.MINUTES));

        ModelAlreadyExistsException modelAlreadyExistsException = assertThrows(ModelAlreadyExistsException.class, () -> sessionDao.create(sessionUser));
        assertThat(modelAlreadyExistsException.getMessage()).isEqualTo("User already has session in db");

        List<Session> sessions = sessionDao.findAll();
        assertThat(sessions).hasSize(1);
    }

    @Test
    void getSessionById_sessionExistInDB() {
        sessionDao.create(sessionUser);

        Session session = sessionDao.getById(sessionId);

        assertThat(session.getId()).isEqualTo(sessionUser.getId());
        assertThat(session.getExpiresAt()).isEqualTo(sessionUser.getExpiresAt());
        assertThat(session.getUser()).isEqualTo(sessionUser.getUser());
    }

    @Test
    void getSessionById_getNull_sessionNotExistInDB() {
        Session session = sessionDao.getById(sessionId);
        assertThat(session).isNull();
    }

    @Test
    void deleteSessionById_sessionExistsInDB() {
        sessionDao.create(sessionUser);

        sessionDao.delete(sessionUser.getId());

        List<Session> sessions = sessionDao.findAll();
        assertThat(sessions).hasSize(0);
    }

    @Test
    void deleteSessionByUserId_sessionExistsInDB() {
        sessionDao.create(sessionUser);

        sessionDao.deleteSessionByUserId(TIMUR.getId());

        List<Session> sessions = sessionDao.findAll();
        assertThat(sessions).hasSize(0);
    }

    @Test
    void isUserSessionValid_returnTrue_sessionValidAndExistsInDB() {
        sessionDao.create(sessionUser);

        Boolean isSessionValid = sessionDao.isSessionValid(sessionUser.getId());

        assertThat(isSessionValid).isTrue();
    }

    @Test
    void isUserSessionValid_returnFalse_sessionIsExpiredAndExistsInDB() {
        sessionUser.setExpiresAt(LocalDateTime.now().minusDays(100));
        sessionDao.create(sessionUser);

        Boolean isSessionValid = sessionDao.isSessionValid(sessionUser.getId());

        assertThat(isSessionValid).isFalse();
    }

    @Test
    void isUserSessionValid_returnNull_sessionNotExistInDB() {
        Boolean isSessionValid = sessionDao.isSessionValid(sessionUser.getId());

        assertThat(isSessionValid).isNull();
    }
}