package avlyakulov.timur.service;

import avlyakulov.timur.custom_exception.ModelAlreadyExistsException;
import avlyakulov.timur.custom_exception.TooManyLocationsException;
import avlyakulov.timur.dao.LocationDao;
import avlyakulov.timur.dao.UserDao;
import avlyakulov.timur.dto.LocationDto;
import avlyakulov.timur.model.Location;
import avlyakulov.timur.model.User;
import avlyakulov.timur.util.hibernate.DeployConfigurationType;
import avlyakulov.timur.util.hibernate.HibernateSingletonUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LocationServiceTest {

    private static SessionFactory sessionFactory;

    private LocationService locationService = new LocationService();

    private LocationDao locationDao = new LocationDao();

    private UserDao userDao = new UserDao();

    @BeforeAll
    static void setUp() {
        sessionFactory = HibernateSingletonUtil.getSessionFactory(DeployConfigurationType.TEST);
    }

    @AfterEach
    void tearTables() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            session.createQuery("delete from Location ").executeUpdate();
            session.createQuery("delete from User ").executeUpdate();

            session.getTransaction().commit();
        }
    }

    @AfterAll
    static void tear() {
        HibernateSingletonUtil.closeSessionFactory();
    }

    @Test
    public void createLocation_locationCreated_userNotHaveAnyLocations() {
        User user = new User("timur", "123");
        userDao.create(user);
        LocationDto locationDto = new LocationDto("Mashivka", new BigDecimal("49.4441667"), new BigDecimal("34.87"), user.getId());

        locationService.createLocation(locationDto);

        List<Location> locationList = locationDao.findAllByUserId(user.getId());
        Location location = locationList.get(0);
        assertEquals(1, locationList.size());
        assertEquals("Mashivka", location.getName());
        assertEquals(new BigDecimal("49.4441667"), location.getLatitude());
        assertEquals(new BigDecimal("34.87"), location.getLongitude());
    }

    @Test
    public void createLocation_locationNotCreated_userAlreadyHave3Locations() {
        User user = new User("timur", "123");
        userDao.create(user);
        LocationDto locationDto = new LocationDto("Mashivka", new BigDecimal("49.4441667"), new BigDecimal("34.87"), user.getId());
        LocationDto locationDto2 = new LocationDto("Mashivka", new BigDecimal("48.4441667"), new BigDecimal("34.87"), user.getId());
        LocationDto locationDto3 = new LocationDto("Mashivka", new BigDecimal("47.4441667"), new BigDecimal("34.87"), user.getId());
        LocationDto locationDto4 = new LocationDto("Kharkiv", new BigDecimal("46.4441667"), new BigDecimal("34.87"), user.getId());

        locationService.createLocation(locationDto);
        locationService.createLocation(locationDto2);
        locationService.createLocation(locationDto3);

        TooManyLocationsException tooManyLocationsException = assertThrows(TooManyLocationsException.class, () -> locationService.createLocation(locationDto4));
        assertEquals("You can't have more than 3 locations", tooManyLocationsException.getMessage());
        List<Location> locationList = locationDao.findAllByUserId(user.getId());
        assertEquals(3, locationList.size());
    }

    @Test
    public void createLocation_locationNotCreated_userAlreadyHaveThisLocation() {
        User user = new User("timur", "123");
        userDao.create(user);
        LocationDto locationDto = new LocationDto("Mashivka", new BigDecimal("49.4441667"), new BigDecimal("34.87"), user.getId());
        LocationDto locationDto2 = new LocationDto("Mashivka", new BigDecimal("49.4441667"), new BigDecimal("34.87"), user.getId());


        locationService.createLocation(locationDto);

        ModelAlreadyExistsException locationAlreadyExistsException = assertThrows(ModelAlreadyExistsException.class, () -> locationService.createLocation(locationDto2));
        assertEquals("This location was already saved", locationAlreadyExistsException.getMessage());
        List<Location> locationList = locationDao.findAllByUserId(user.getId());
        assertEquals(1, locationList.size());
    }

    @Test
    public void getAllLocationByUserId_locationNotFound_noLocationInDB() {
        User user = new User("timur", "123");
        userDao.create(user);

        List<Location> locationList = locationService.getAllLocationByUserId(user.getId());
        assertEquals(0, locationList.size());
    }

    @Test
    public void getAllLocationByUserId_locationsWereFound_2LocationsWereSaved() {
        User user = new User("timur", "123");
        userDao.create(user);
        LocationDto locationDto = new LocationDto("Mashivka", new BigDecimal("49.4441667"), new BigDecimal("34.87"), user.getId());
        LocationDto locationDto2 = new LocationDto("Kharkiv", new BigDecimal("46.4441667"), new BigDecimal("34.87"), user.getId());

        locationService.createLocation(locationDto);
        locationService.createLocation(locationDto2);

        List<Location> locationList = locationService.getAllLocationByUserId(user.getId());
        assertEquals(2, locationList.size());
    }

    @Test
    public void deleteLocationByCoordinate_locationWasDeleted_locationExists() {
        User user = new User("timur", "123");
        userDao.create(user);
        LocationDto locationDto = new LocationDto("Mashivka", new BigDecimal("49.4441667"), new BigDecimal("34.87"), user.getId());
        LocationDto locationDto2 = new LocationDto("Kharkiv", new BigDecimal("46.4441667"), new BigDecimal("34.87"), user.getId());
        locationService.createLocation(locationDto);
        locationService.createLocation(locationDto2);

        locationService.deleteLocationByCoordinate(locationDto);

        List<Location> locationList = locationService.getAllLocationByUserId(user.getId());
        assertEquals(1, locationList.size());
    }

    @Test
    public void deleteLocationByCoordinate_locationNotDeleted_locationNotExist() {
        User user = new User("timur", "123");
        User user2 = new User("dima", "123");
        userDao.create(user);
        userDao.create(user2);
        LocationDto locationDto = new LocationDto("Mashivka", new BigDecimal("49.4441667"), new BigDecimal("34.87"), user.getId());
        LocationDto locationDto2 = new LocationDto("Kharkiv", new BigDecimal("46.4441667"), new BigDecimal("34.87"), user2.getId());
        locationService.createLocation(locationDto);

        locationService.deleteLocationByCoordinate(locationDto2);

        List<Location> locationList = locationService.getAllLocationByUserId(user.getId());
        assertEquals(1, locationList.size());
    }
}