package avlyakulov.timur.listener;

import avlyakulov.timur.util.hibernate.DeployConfigurationType;
import avlyakulov.timur.util.hibernate.HibernateSingletonUtil;
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
        log.info("HibernateInitializeListener listener was created and initialize Hibernate connection");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        HibernateSingletonUtil.closeSessionFactory();
        log.info("HibernateInitializeListener listener was destroyed and closed Hibernate connection");
    }
}