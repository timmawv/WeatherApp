package avlyakulov.timur.util.hibernate;

import avlyakulov.timur.model.Location;
import avlyakulov.timur.model.Session;
import avlyakulov.timur.model.User;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.util.Properties;

@Slf4j
public class HibernateSingletonUtil {

    private static SessionFactory sessionFactory;

    private HibernateSingletonUtil() {
    }

    public static void initEnvironments() {
        String url = System.getenv("DB_URL");
        String username = System.getenv("DB_USERNAME");
        String password = System.getenv("DB_PASSWORD");
        String apiKey = System.getenv("API_WEATHER_KEY");

        if (isAnyParameterNull(url, username, password, apiKey)) {
            //good way to initialize env from file
            /*ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream resourceFile = classLoader.getResourceAsStream("credentials.env");
            if (resourceFile != null) {
                Properties properties = new Properties();
                try {
                    properties.load(resourceFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                properties.forEach((key, value) -> System.setProperty(key.toString(), value.toString()));
            } else {
                log.error("credentials.env isn't in resources");
                throw new RuntimeException("You don't have env file to launch an app");
            }*/
            log.error("The environment variables weren't initialized");
            throw new RuntimeException("You didn't initialized the environment variables");
        } else {
            System.setProperty("DB_URL", url);
            System.setProperty("DB_USERNAME", username);
            System.setProperty("DB_PASSWORD", password);
            System.setProperty("API_WEATHER_KEY", apiKey);
        }
    }

    public static void initSessionFactory(DeployConfigurationType deployConfigurationType) {
        initEnvironments();
        if (sessionFactory == null || sessionFactory.isClosed()) {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Properties hibernateProperty = new Properties();
            try {
                hibernateProperty.load(classLoader.getResourceAsStream(deployConfigurationType.getPropertyFileName()));
            } catch (IOException e) {
                log.error("Error with configure file for hibernate");
            }
            sessionFactory = new Configuration()
                    .addProperties(hibernateProperty)
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Session.class)
                    .addAnnotatedClass(Location.class)
                    .buildSessionFactory();
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            log.error("You are trying to get session factory which wasn't initialized");
            throw new RuntimeException("Session factory is not created");
        }
        return sessionFactory;
    }

    public static SessionFactory getSessionFactory(DeployConfigurationType deployConfigurationType) {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            initSessionFactory(deployConfigurationType);
        }
        return sessionFactory;
    }

    public static void closeSessionFactory() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }

    private static boolean isAnyParameterNull(String... parameters) {
        for (String parameter : parameters) {
            if (parameter == null) {
                return true;
            }
        }
        return false;
    }
}