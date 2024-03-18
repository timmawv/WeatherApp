package avlyakulov.timur.service;

import avlyakulov.timur.custom_exception.ModelAlreadyExistsException;
import avlyakulov.timur.custom_exception.TooManyLocationsException;
import avlyakulov.timur.dao.LocationDao;
import avlyakulov.timur.dto.LocationDto;
import avlyakulov.timur.model.Location;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

public class LocationService {

    private final LocationDao locationDao = new LocationDao();

    public void createLocation(Location location) {
        List<Location> locationList = locationDao.findAllByUserId(location.getUser().getId());
        if (locationList.size() > 5) {
            throw new TooManyLocationsException("You can't have more than 5 locations");
        } else {
            try {
                locationDao.create(location);
            } catch (ConstraintViolationException e) {
                throw new ModelAlreadyExistsException("This location was already saved!");
            }
        }
    }


    public List<Location> getAllLocationByUserId(int userId) {
        return locationDao.findAllByUserId(userId);
    }

    public void deleteLocationByCoordinate(LocationDto locationDto) {
        locationDao.deleteLocation(locationDto);
    }
}