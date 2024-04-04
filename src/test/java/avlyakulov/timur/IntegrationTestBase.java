package avlyakulov.timur;

import avlyakulov.timur.util.hibernate.DeployConfigurationType;
import avlyakulov.timur.util.hibernate.HibernateSingletonUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;

public abstract class IntegrationTestBase {

    private static SessionFactory sessionFactory;

    @BeforeAll
    static void openConnection() {
        sessionFactory = HibernateSingletonUtil.getSessionFactory(DeployConfigurationType.TEST);
    }

    @AfterEach
    void tearTables() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.createQuery("delete from Location ").executeUpdate();
            session.createQuery("delete from Session ").executeUpdate();
            session.createQuery("delete from User ").executeUpdate();

            session.getTransaction().commit();
        }
    }

    @AfterAll
    static void closeConnection() {
        HibernateSingletonUtil.closeSessionFactory();
    }
}