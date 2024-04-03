package avlyakulov.timur.service;

import avlyakulov.timur.custom_exception.ModelAlreadyExistsException;
import avlyakulov.timur.custom_exception.TooManyLocationsException;
import avlyakulov.timur.dao.LocationDao;
import avlyakulov.timur.dto.LocationDto;
import avlyakulov.timur.mapper.LocationMapper;
import avlyakulov.timur.model.Location;
import avlyakulov.timur.model.User;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

public class LocationService {

    private final LocationDao locationDao = new LocationDao();

    public void createLocation(LocationDto locationDto) {
        long numberUserLocations = locationDao.findNumberUserLocations(locationDto.getUserId());
        if (numberUserLocations >= 3) {
            throw new TooManyLocationsException("You can't have more than 3 locations");
        } else {
            Location location = LocationMapper.INSTANCE.mapLocationDtoToLocation(locationDto);
            try {
                locationDao.create(location);
            } catch (ConstraintViolationException e) {
                throw new ModelAlreadyExistsException("This location was already saved");
            }
        }
    }

    public List<Location> getAllLocationByUserId(Integer userId) {
        return locationDao.findAllByUserId(userId);
    }

    public void deleteLocationByCoordinate(LocationDto locationDto) {
        locationDao.deleteLocation(locationDto);
    }
}