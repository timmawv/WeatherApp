package avlyakulov.timur.service;

import avlyakulov.timur.custom_exception.TooManyLocationsException;
import avlyakulov.timur.dao.LocationDao;
import avlyakulov.timur.dto.LocationDto;
import avlyakulov.timur.mapper.LocationMapper;
import avlyakulov.timur.model.Location;

import java.util.List;

public class LocationService {

    private LocationDao locationDao;

    public LocationService(LocationDao locationDao) {
        this.locationDao = locationDao;
    }

    public void createLocation(LocationDto locationDto) {
        long numberUserLocations = locationDao.findNumberUserLocations(locationDto.getUserId());
        if (numberUserLocations >= 3) {
            throw new TooManyLocationsException("You can't have more than 3 locations");
        }
        Location location = LocationMapper.INSTANCE.mapLocationDtoToLocation(locationDto);
        locationDao.create(location);
    }

    public List<Location> getAllLocationByUserId(Integer userId) {
        return locationDao.findLocationsByUserId(userId);
    }

    public void deleteLocationByCoordinate(LocationDto locationDto) {
        locationDao.deleteLocation(
                locationDto.getLatitude(),
                locationDto.getLongitude(),
                locationDto.getUserId()
        );
    }
}