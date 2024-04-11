package avlyakulov.timur.dao;

import avlyakulov.timur.custom_exception.ModelAlreadyExistsException;
import avlyakulov.timur.dto.LocationDto;
import avlyakulov.timur.model.Location;
import org.hibernate.exception.ConstraintViolationException;

import java.math.BigDecimal;
import java.util.List;

public class LocationDao extends HibernateDao {

    public List<Location> findAll() {
        return executeNotInTransaction(session -> session.createQuery("from Location", Location.class).getResultList());
    }

    public void create(Location location) {
        try {
            executeInTransaction(session -> session.persist(location));
        } catch (ConstraintViolationException e) {
            throw new ModelAlreadyExistsException("This location was already saved");
        }
    }

    public List<Location> findLocationsByUserId(int userId) {
        return executeNotInTransaction(session -> session.createQuery("from Location where user.id = :userId", Location.class)
                .setParameter("userId", userId)
                .getResultList());
    }

    public Long findNumberUserLocations(int userId) {
        return executeNotInTransaction(session -> session.createQuery("select count(*) from Location where user.id = :userId", Long.class)
                .setParameter("userId", userId)
                .getSingleResult());
    }

    public void deleteLocation(BigDecimal latitude, BigDecimal longitude, Integer userId) {
        executeInTransaction(session -> session.createQuery("delete from Location where latitude = :latitude and longitude = :longitude and user.id = :userId")
                .setParameter("latitude", latitude)
                .setParameter("longitude", longitude)
                .setParameter("userId", userId)
                .executeUpdate());
    }
}