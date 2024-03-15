package avlyakulov.timur.dao;

import avlyakulov.timur.model.Location;
import avlyakulov.timur.util.HibernateSingletonUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class LocationDao {

    private final SessionFactory sessionFactory = HibernateSingletonUtil.getSessionFactory();

    public void create(Location location) {
        try (Session hibernateSession = sessionFactory.openSession()) {
            hibernateSession.beginTransaction();//открываем транзакцию

            hibernateSession.persist(location);

            hibernateSession.getTransaction().commit();//закрываем транзакцию
        }
    }

    public List<Location> findAllByUserId(int userId) {
        try (Session hibernateSession = sessionFactory.openSession()) {
            return hibernateSession.createQuery("from Location where user.id = :userId", Location.class)
                    .setParameter("userId", userId)
                    .getResultList();
        }
    }

    public void deleteLocation(Location location) {
        try (Session hibernateSession = sessionFactory.openSession()) {
            hibernateSession.beginTransaction();//открываем транзакцию

            hibernateSession.createQuery("delete from Location where latitude = :latitude and longitude = :longitude")
                    .setParameter("latitude", location.getLatitude())
                    .setParameter("longitude", location.getLongitude())
                    .executeUpdate();

            hibernateSession.getTransaction().commit();//закрываем транзакцию
        }
    }
}