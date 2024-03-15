package avlyakulov.timur.dao;

import avlyakulov.timur.custom_exception.ModelNotFoundException;
import avlyakulov.timur.model.Session;
import avlyakulov.timur.model.User;
import avlyakulov.timur.util.HibernateSingletonUtil;
import jakarta.persistence.NoResultException;
import org.hibernate.SessionFactory;

import java.util.Optional;
import java.util.UUID;

public class SessionDao {
    private final SessionFactory sessionFactory = HibernateSingletonUtil.getSessionFactory();

    public void create(Session session) {
        try (org.hibernate.Session hibernateSession = sessionFactory.openSession()) {
            hibernateSession.beginTransaction();//открываем транзакцию

            hibernateSession.persist(session);

            hibernateSession.getTransaction().commit();//закрываем транзакцию
        }
    }

    public Optional<Session> getById(UUID sessionId) {
        try (org.hibernate.Session hibernateSession = sessionFactory.openSession()) {
            return Optional.ofNullable(hibernateSession.get(Session.class, sessionId));
        }
    }

    public User getUserById(UUID sessionId) {
        try (org.hibernate.Session hibernateSession = sessionFactory.openSession()) {
            return hibernateSession.createNamedQuery("HQL_GetUserByHisSession", User.class)
                    .setParameter("sessionId", sessionId)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new ModelNotFoundException("Session with such id doesn't exist");
        }
    }

    public void delete(UUID sessionId) {
        try (org.hibernate.Session hibernateSession = sessionFactory.openSession()) {
            hibernateSession.beginTransaction();//открываем транзакцию

            hibernateSession.createNamedQuery("HQL_DeleteSessionById")
                    .setParameter("sessionId", sessionId)
                    .executeUpdate();

            hibernateSession.getTransaction().commit();//закрываем транзакцию
        }
    }
}