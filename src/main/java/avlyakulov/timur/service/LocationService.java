package avlyakulov.timur.service;

import avlyakulov.timur.custom_exception.ModelAlreadyExistsException;
import avlyakulov.timur.custom_exception.TooManyLocationsException;
import avlyakulov.timur.dao.LocationDao;
import avlyakulov.timur.dto.LocationDto;
import avlyakulov.timur.model.Location;
import avlyakulov.timur.model.User;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

public class LocationService {

    private final LocationDao locationDao = new LocationDao();

    public void createLocation(LocationDto locationDto) {
        List<Location> locationList = locationDao.findAllByUserId(locationDto.getUserId());
        if (locationList.size() >= 3) {
            throw new TooManyLocationsException("You can't have more than 3 locations");
        } else {
            try {
                locationDao.create(new Location(
                        locationDto.getName(),
                        locationDto.getLatitude(),
                        locationDto.getLongitude(),
                        new User(locationDto.getUserId())
                ));
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