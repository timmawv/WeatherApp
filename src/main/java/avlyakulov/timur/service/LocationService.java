package avlyakulov.timur.service;

import avlyakulov.timur.custom_exception.TooManyLocationsException;
import avlyakulov.timur.dao.LocationDao;
import avlyakulov.timur.dto.LocationDto;
import avlyakulov.timur.mapper.LocationMapper;
import avlyakulov.timur.model.Location;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class LocationService {

    private LocationDao locationDao;

    private ObjectMapper objectMapper = new ObjectMapper();

    private int MAX_USER_LOCATIONS = 3;

    public LocationService(LocationDao locationDao) {
        this.locationDao = locationDao;
    }

    public void createLocation(LocationDto locationDto) {
        long numberUserLocations = locationDao.findNumberUserLocations(locationDto.getUserId());
        if (numberUserLocations >= MAX_USER_LOCATIONS) {
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

    public void createLocationFromJsonFile(String locationJson, Integer userId) throws JsonProcessingException {
        LocationDto locationDto = objectMapper.readValue(locationJson, new TypeReference<>() {
        });
        locationDto.setUserId(userId);
        createLocation(locationDto);
    }

    public void deleteLocationFromJsonFile(String locationJson, Integer userId) throws JsonProcessingException {
        LocationDto locationDto = objectMapper.readValue(locationJson, new TypeReference<>() {
        });
        locationDto.setUserId(userId);
        deleteLocationByCoordinate(locationDto);
    }
}