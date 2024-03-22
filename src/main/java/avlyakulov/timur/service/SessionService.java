package avlyakulov.timur.service;

import avlyakulov.timur.custom_exception.SessionNotValid;
import avlyakulov.timur.dao.SessionDao;
import avlyakulov.timur.dto.UserDto;
import avlyakulov.timur.model.Session;
import avlyakulov.timur.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

public class SessionService {

    private final SessionDao sessionDao = new SessionDao();

    private final int minutesSessionExist = 30;

    public Session createSession(User user) {
        Session session = new Session(UUID.randomUUID(), user);
        session.setExpiresAt(LocalDateTime.now().plus(minutesSessionExist, ChronoUnit.MINUTES));
        sessionDao.create(session);
        return session;
    }

    public Session getUserSessionIfItNotExpired(String sessionId) {
        Optional<Session> session = sessionDao.getById(UUID.fromString(sessionId));
        if (session.isPresent()) {
            LocalDateTime now = LocalDateTime.now();
            if (now.isAfter(session.get().getExpiresAt())) {
                throw new SessionNotValid("User session was expired");
            } else {
                return session.get();
            }
        } else {
            throw new SessionNotValid("User session was expired or it doesn't exits");
        }
    }

    public UserDto getUserDtoByHisSession(Session session) {
        User user = session.getUser();
        return new UserDto(user.getId(), user.getLogin());
    }

    public void deleteSessionById(String sessionId) {
        sessionDao.delete(UUID.fromString(sessionId));
    }

    public void deleteSessionByUserId(int userId) {
        sessionDao.deleteByUserid(userId);
    }

    public boolean isUserSessionValid(String sessionId) {
        return sessionDao.isSessionValid(UUID.fromString(sessionId));
    }
}