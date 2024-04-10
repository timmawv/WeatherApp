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

    private static User DIMA;
    private static Location KHARKIV_TIMUR;

    private static Location KHARKIV_DIMA;

    private UserDao userDao;

    private LocationDao locationDao;

    @BeforeEach
    void setUp() {
        TIMUR = new User("timur", "123");
        DIMA = new User("dima", "123");
        userDao = new UserDao();
        userDao.create(TIMUR);
        userDao.create(DIMA);
        locationDao = new LocationDao();
        KHARKIV_TIMUR = new Location("Kharkiv", new BigDecimal("50"), new BigDecimal("36"), TIMUR);
        KHARKIV_DIMA = new Location("Kharkiv", new BigDecimal("50"), new BigDecimal("36"), DIMA);
    }

    @Test
    void createLocation_locationCreated_LocationNotExistInDB() {
        locationDao.create(KHARKIV_TIMUR);

        List<Location> locations = locationDao.findLocationsByUserId(TIMUR.getId());
        assertThat(locations).hasSize(1);

        Location locationDB = locations.get(0);

        assertThat(locationDB.getName()).isEqualTo(KHARKIV_TIMUR.getName());
        assertThat(locationDB.getLatitude()).isEqualTo(KHARKIV_TIMUR.getLatitude());
        assertThat(locationDB.getLongitude()).isEqualTo(KHARKIV_TIMUR.getLongitude());
    }

    @Test
    void createLocation_throwsException_locationAlreadyExistInDB() {
        locationDao.create(KHARKIV_TIMUR);
        Location testLocation = new Location("Kharkiv", new BigDecimal("50"), new BigDecimal("36"), TIMUR);

        ModelAlreadyExistsException modelAlreadyExistsException = assertThrows(ModelAlreadyExistsException.class, () -> locationDao.create(testLocation));
        assertThat(modelAlreadyExistsException.getMessage()).isEqualTo("This location was already saved");

        List<Location> locations = locationDao.findLocationsByUserId(TIMUR.getId());
        assertThat(locations).hasSize(1);
    }

    @Test
    void findNumberUserLocation_findOne_createdOneLocation() {
        locationDao.create(KHARKIV_TIMUR);

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
        locationDao.create(KHARKIV_TIMUR);

        LocationDto locationDto = LocationMapper.INSTANCE.mapLocationToLocationDto(KHARKIV_TIMUR);
        locationDao.deleteLocation(locationDto);

        List<Location> locations = locationDao.findLocationsByUserId(TIMUR.getId());
        assertThat(locations).hasSize(0);
    }

    @Test
    void deleteLocation_deleteOnlyUserLocation() {
        locationDao.create(KHARKIV_TIMUR);
        locationDao.create(KHARKIV_DIMA);


        LocationDto locationDto = LocationMapper.INSTANCE.mapLocationToLocationDto(KHARKIV_TIMUR);
        locationDao.deleteLocation(locationDto);

        List<Location> allLocations = locationDao.findAll();
        assertThat(allLocations).hasSize(1);

        List<Location> locations = locationDao.findLocationsByUserId(TIMUR.getId());
        assertThat(locations).hasSize(0);
    }

    @Test
    void deleteLocation_deleteZeroLocation() {
        LocationDto locationDto = LocationMapper.INSTANCE.mapLocationToLocationDto(KHARKIV_TIMUR);
        locationDao.deleteLocation(locationDto);

        List<Location> locations = locationDao.findLocationsByUserId(TIMUR.getId());
        assertThat(locations).hasSize(0);
    }
}