package avlyakulov.timur.dao;

import avlyakulov.timur.util.hibernate.HibernateSingletonUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.function.Consumer;
import java.util.function.Function;

public class HibernateDao {

    private final SessionFactory sessionFactory = HibernateSingletonUtil.getSessionFactory();
    //add find all test
    public void executeInTransaction(Consumer<Session> operation) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            operation.accept(session);
            session.flush();

            session.getTransaction().commit();
        }
    }

    public <T> T executeNotInTransaction(Function<Session, T> operation) {
        try (Session session = sessionFactory.openSession()) {
            return operation.apply(session);
        }
    }
}