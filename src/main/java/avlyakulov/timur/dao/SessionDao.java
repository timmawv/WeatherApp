package avlyakulov.timur.dao;

import avlyakulov.timur.model.Session;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SessionDao extends HibernateDao {

    public List<Session> findAll() {
        return executeNotInTransaction(session -> session.createQuery("from Session", Session.class).getResultList());
    }


    public void create(Session sessionUser) {
        executeInTransaction(session -> session.persist(sessionUser));
    }

    public Optional<Session> getById(UUID sessionId) {
        return executeNotInTransaction(session -> Optional.ofNullable(session.get(Session.class, sessionId)));
    }

    public void delete(UUID sessionId) {
        executeInTransaction(session -> session.createNamedQuery("HQL_DeleteSessionById")
                .setParameter("sessionId", sessionId)
                .executeUpdate());
    }

    public void deleteSessionByUserid(int userId) {
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