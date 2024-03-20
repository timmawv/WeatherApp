package avlyakulov.timur.service;

import avlyakulov.timur.custom_exception.SessionNotValid;
import avlyakulov.timur.dao.SessionDao;
import avlyakulov.timur.dto.UserDto;
import avlyakulov.timur.model.Session;
import avlyakulov.timur.model.User;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class SessionService {

    private final SessionDao sessionDao = new SessionDao();

    public Session createSession(User user) {
        Session session = new Session(UUID.randomUUID(), user);
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

    public void deleteSessionById(UUID sessionId) {
        sessionDao.delete(sessionId);
    }

    public void deleteSessionByUserId(int userId) {
        sessionDao.deleteByUserid(userId);
    }

    public boolean isUserSessionExpired(UUID sessionId) {
        Optional<Session> session = sessionDao.getById(sessionId);
        if (session.isPresent()) {
            LocalDateTime now = LocalDateTime.now();
            return now.isAfter(session.get().getExpiresAt());
        } else {
            return true;
        }
    }
}