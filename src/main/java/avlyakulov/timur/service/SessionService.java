package avlyakulov.timur.service;

import avlyakulov.timur.dao.SessionDao;
import avlyakulov.timur.model.Session;

import java.time.LocalDateTime;
import java.util.UUID;

public class SessionService {

    private final SessionDao sessionDao = new SessionDao();

    public void createSession(Session session) {
        sessionDao.create(session);
    }

    public Session getSessionById(UUID sessionId) {
        return sessionDao.getById(sessionId);
    }

    public void deleteSessionById(UUID sessionId) {
        sessionDao.delete(sessionId);
    }

    public boolean isUserSessionExpired(UUID sessionId) {
        Session session = sessionDao.getById(sessionId);
        LocalDateTime now = LocalDateTime.now();
        return now.isBefore(session.getExpiresAt());
    }
}