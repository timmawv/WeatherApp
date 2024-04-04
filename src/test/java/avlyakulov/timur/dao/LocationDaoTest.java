package avlyakulov.timur.dao;

import avlyakulov.timur.custom_exception.ModelAlreadyExistsException;
import avlyakulov.timur.dto.LocationDto;
import avlyakulov.timur.mapper.LocationMapper;
import avlyakulov.timur.model.Location;
import avlyakulov.timur.model.User;
import avlyakulov.timur.IntegrationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LocationDaoTest extends IntegrationTestBase {
    private static User TIMUR;
    private static Location KHARKIV;

    private UserDao userDao;

    private LocationDao locationDao;

    @BeforeEach
    void setUp() {
        TIMUR = new User("timur", "123");
        userDao = new UserDao();
        userDao.create(TIMUR);
        locationDao = new LocationDao();
        KHARKIV = new Location("Kharkiv", new BigDecimal("50"), new BigDecimal("36"), TIMUR);
    }

    @Test
    void createLocation_locationCreated_LocationNotExistInDB() {
        locationDao.create(KHARKIV);

        List<Location> locations = locationDao.findLocationsByUserId(TIMUR.getId());
        assertThat(locations).hasSize(1);

        Location locationDB = locations.get(0);

        assertThat(locationDB.getName()).isEqualTo(KHARKIV.getName());
        assertThat(locationDB.getLatitude()).isEqualTo(KHARKIV.getLatitude());
        assertThat(locationDB.getLongitude()).isEqualTo(KHARKIV.getLongitude());
    }

    @Test
    void createLocation_throwsException_locationAlreadyExistInDB() {
        locationDao.create(KHARKIV);
        Location testLocation = new Location("Kharkiv", new BigDecimal("50"), new BigDecimal("36"), TIMUR);

        ModelAlreadyExistsException modelAlreadyExistsException = assertThrows(ModelAlreadyExistsException.class, () -> locationDao.create(testLocation));
        assertThat(modelAlreadyExistsException.getMessage()).isEqualTo("This location was already saved");

        List<Location> locations = locationDao.findLocationsByUserId(TIMUR.getId());
        assertThat(locations).hasSize(1);
    }

    @Test
    void findNumberUserLocation_findOne_createdOneLocation() {
        locationDao.create(KHARKIV);

        Long numberUserLocations = locationDao.findNumberUserLocations(TIMUR.getId());

        assertThat(numberUserLocations).isEqualTo(1L);
    }

    @Test
    void findNumberUserLocation_findZero_createdZeroLocation() {
        Long numberUserLocations = locationDao.findNumberUserLocations(TIMUR.getId());

        assertThat(numberUserLocations).isEqualTo(0L);
    }

    @Test
    void deleteLocation_deleteOneLocation() {
        locationDao.create(KHARKIV);

        LocationDto locationDto = LocationMapper.INSTANCE.mapLocationToLocationDto(KHARKIV);
        locationDao.deleteLocation(locationDto);

        List<Location> locations = locationDao.findLocationsByUserId(TIMUR.getId());
        assertThat(locations).hasSize(0);
    }

    @Test
    void deleteLocation_deleteZeroLocation() {
        LocationDto locationDto = LocationMapper.INSTANCE.mapLocationToLocationDto(KHARKIV);
        locationDao.deleteLocation(locationDto);

        List<Location> locations = locationDao.findLocationsByUserId(TIMUR.getId());
        assertThat(locations).hasSize(0);
    }
}