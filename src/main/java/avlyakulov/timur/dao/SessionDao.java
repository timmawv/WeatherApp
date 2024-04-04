package avlyakulov.timur.dao;

import avlyakulov.timur.custom_exception.ModelAlreadyExistsException;
import avlyakulov.timur.model.Session;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;
import java.util.UUID;

@Slf4j
public class SessionDao extends HibernateDao {

    public List<Session> findAll() {
        return executeNotInTransaction(session -> session.createQuery("from Session", Session.class).getResultList());
    }

    public void create(Session sessionUser) {
        try {
            executeInTransaction(session -> session.persist(sessionUser));
        } catch (ConstraintViolationException e) {
            log.error("User already has session in db");
            throw new ModelAlreadyExistsException("User already has session in db");
        }
    }

    public Session getById(UUID sessionId) {
        return executeNotInTransaction(session -> session.get(Session.class, sessionId));
    }

    public void delete(UUID sessionId) {
        executeInTransaction(session -> session.createNamedQuery("HQL_DeleteSessionById")
                .setParameter("sessionId", sessionId)
                .executeUpdate());
    }

    public void deleteSessionByUserId(int userId) {
        executeInTransaction(session -> session.createNamedQuery("HQL_DeleteSessionByUserId")
                .setParameter("userId", userId)
                .executeUpdate());
    }

    public Boolean isSessionValid(UUID sessionId) {
        return executeNotInTransaction(session -> session.createNamedQuery("HQL_IsSessionValid", Boolean.class)
                .setParameter("sessionId", sessionId)
                .getSingleResultOrNull());
    }
}