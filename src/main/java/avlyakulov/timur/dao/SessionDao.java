package avlyakulov.timur.dao;

import avlyakulov.timur.model.Session;
import avlyakulov.timur.util.HibernateSingletonUtil;
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

    public void delete(UUID sessionId) {
        try (org.hibernate.Session hibernateSession = sessionFactory.openSession()) {
            hibernateSession.beginTransaction();//открываем транзакцию

            hibernateSession.createNamedQuery("HQL_DeleteSessionById")
                    .setParameter("sessionId", sessionId)
                    .executeUpdate();

            hibernateSession.getTransaction().commit();//закрываем транзакцию
        }
    }

    public void deleteByUserid(int userId) {
        try (org.hibernate.Session hibernateSession = sessionFactory.openSession()) {
            hibernateSession.beginTransaction();//открываем транзакцию

            hibernateSession.createNamedQuery("HQL_DeleteSessionByUserId")
                    .setParameter("userId", userId)
                    .executeUpdate();

            hibernateSession.getTransaction().commit();//закрываем транзакцию
        }
    }

    public boolean isSessionValid(UUID sessionId) {
        try (org.hibernate.Session hibernateSession = sessionFactory.openSession()) {
            return hibernateSession.createNamedQuery("HQL_IsSessionValid", Boolean.class)
                    .setParameter("sessionId", sessionId)
                    .getSingleResult();
        }
    }
}