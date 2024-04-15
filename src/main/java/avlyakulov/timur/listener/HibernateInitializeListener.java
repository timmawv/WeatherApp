package avlyakulov.timur.listener;

import avlyakulov.timur.util.hibernate.DeployConfigurationType;
import avlyakulov.timur.util.hibernate.HibernateSingletonUtil;
import avlyakulov.timur.util.hibernate.LiquibaseInitializer;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebListener
public class HibernateInitializeListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        HibernateSingletonUtil.initSessionFactory(DeployConfigurationType.PROD);
        LiquibaseInitializer liquibaseInitializer = new LiquibaseInitializer();
        liquibaseInitializer.addMigrationsToDB();
        log.info("HibernateInitializeListener listener was created and initialize Hibernate connection");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateSingletonUtil.closeSessionFactory();
        log.info("HibernateInitializeListener listener was destroyed and closed Hibernate connection");
    }
}