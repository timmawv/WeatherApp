package avlyakulov.timur.util;


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
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Properties properties = new Properties();
            try {
                properties.load(classLoader.getResourceAsStream("credentials.env"));
            } catch (IOException e) {
                log.error("We can't initialize environments for hibernate");
            }
            properties.forEach((key, value) -> System.setProperty(key.toString(), value.toString()));
        } else {
            System.setProperty("DB_URL", url);
            System.setProperty("DB_USERNAME", username);
            System.setProperty("DB_PASSWORD", password);
            System.setProperty("API_WEATHER_KEY", apiKey);
        }
    }

    public static void initSessionFactory() {
        initEnvironments();
        if (sessionFactory == null) {
            sessionFactory = new Configuration()
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Session.class)
                    .addAnnotatedClass(Location.class)
                    .buildSessionFactory();
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            initSessionFactory();
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