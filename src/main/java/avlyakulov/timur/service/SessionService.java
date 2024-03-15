package avlyakulov.timur.service;

import avlyakulov.timur.dao.SessionDao;
import avlyakulov.timur.dto.UserDto;
import avlyakulov.timur.model.Session;
import avlyakulov.timur.model.User;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class SessionService {

    private final SessionDao sessionDao = new SessionDao();

    public void createSession(Session session) {
        sessionDao.create(session);
    }

    public UserDto getUserByHisSession(String sessionId) {
        User userById = sessionDao.getUserById(UUID.fromString(sessionId));
        return new UserDto(userById.getId(), userById.getLogin());
    }

    public void deleteSessionById(UUID sessionId) {
        sessionDao.delete(sessionId);
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