package avlyakulov.timur.dao;

import avlyakulov.timur.dto.LocationDto;
import avlyakulov.timur.model.Location;

import java.util.List;

public class LocationDao extends HibernateDao {

    public void create(Location location) {
        executeInTransaction(session -> session.persist(location));
    }

    public List<Location> findAllByUserId(int userId) {
        return executeNotInTransaction(session -> session.createQuery("from Location where user.id = :userId", Location.class)
                .setParameter("userId", userId)
                .getResultList());
    }

    public Long findNumberUserLocations(int userId) {
        return executeNotInTransaction(session -> session.createQuery("select count(*) from Location where user.id = :userId", Long.class)
                .setParameter("userId", userId)
                .getSingleResult());
    }

    public void deleteLocation(LocationDto locationDto) {
        executeInTransaction(session -> session.createQuery("delete from Location where latitude = :latitude and longitude = :longitude")
                .setParameter("latitude", locationDto.getLatitude())
                .setParameter("longitude", locationDto.getLongitude())
                .executeUpdate());
    }
}