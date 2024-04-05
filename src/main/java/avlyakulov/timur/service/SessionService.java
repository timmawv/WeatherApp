package avlyakulov.timur.service;

import avlyakulov.timur.custom_exception.SessionNotValidException;
import avlyakulov.timur.dao.SessionDao;
import avlyakulov.timur.dto.UserDto;
import avlyakulov.timur.mapper.UserMapper;
import avlyakulov.timur.model.Session;
import avlyakulov.timur.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class SessionService {

    private final SessionDao sessionDao;

    public SessionService(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
    }

    public String createSessionAndGetItsId(User user) {
        Session session = new Session(UUID.randomUUID().toString(), user);
        sessionDao.create(session);
        return session.getId();
    }

    public Session getUserSessionIfItNotExpired(String sessionId) {
        Session session = sessionDao.getById(sessionId);
        if (session == null) {
            throw new SessionNotValidException("User session was expired or it doesn't exist");
        } else {
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(session.getExpiresAt())) {
                return session;
            } else {
                throw new SessionNotValidException("User session was expired");
            }
        }
    }

    public UserDto getUserDtoByHisSession(Session session) {
        User user = session.getUser();
        return UserMapper.INSTANCE.mapUserToUserDto(user);
    }

    public void deleteSessionById(String sessionId) {
        sessionDao.delete(sessionId);
    }

    public void deleteSessionByUserId(int userId) {
        sessionDao.deleteSessionByUserId(userId);
    }

    public Boolean isUserSessionValid(String sessionId) {
        Boolean isSessionValid = sessionDao.isSessionValid(sessionId);
        if (isSessionValid == null) {
            throw new SessionNotValidException();
        } else {
            return isSessionValid;
        }
    }
}