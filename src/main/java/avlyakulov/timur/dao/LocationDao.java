package avlyakulov.timur.dao;

import avlyakulov.timur.dto.LocationDto;
import avlyakulov.timur.model.Location;
import avlyakulov.timur.util.hibernate.HibernateSingletonUtil;
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

    public Long findNumberUserLocations(int userId) {
        try (Session hibernateSession = sessionFactory.openSession()) {
            return hibernateSession.createQuery("select count(*) from Location where user.id = :userId", Long.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
        }
    }

    public void deleteLocation(LocationDto locationDto) {
        try (Session hibernateSession = sessionFactory.openSession()) {
            hibernateSession.beginTransaction();//открываем транзакцию

            hibernateSession.createQuery("delete from Location where latitude = :latitude and longitude = :longitude")
                    .setParameter("latitude", locationDto.getLatitude())
                    .setParameter("longitude", locationDto.getLongitude())
                    .executeUpdate();

            hibernateSession.getTransaction().commit();//закрываем транзакцию
        }
    }
}