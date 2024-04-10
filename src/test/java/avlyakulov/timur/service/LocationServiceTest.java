package avlyakulov.timur.service;

import avlyakulov.timur.custom_exception.TooManyLocationsException;
import avlyakulov.timur.dao.LocationDao;
import avlyakulov.timur.dto.LocationDto;
import avlyakulov.timur.model.Location;
import avlyakulov.timur.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    private final User TIMUR = new User(1, "timur", "123");

    private final Location KHARKIV = new Location("Kharkiv", new BigDecimal("50"), new BigDecimal("36"), TIMUR);

    private final LocationDto KHARKIV_DTO = new LocationDto("Kharkiv", new BigDecimal("50"), new BigDecimal("36"), 1);

    private final List<Location> listLocationsEmpty = Collections.emptyList();

    private final List<Location> listTimurLocations = List.of(KHARKIV);

    @Captor
    ArgumentCaptor<Location> argumentCaptor;
    @Mock
    private LocationDao locationDao;
    @InjectMocks
    private LocationService locationService;

    @Test
    void createLocation_locationWasCreated_userNotHaveAnyLocations() {
        doReturn(0L).when(locationDao).findNumberUserLocations(KHARKIV_DTO.getUserId());

        locationService.createLocation(KHARKIV_DTO);

        verify(locationDao, times(1)).findNumberUserLocations(KHARKIV_DTO.getUserId());
        verify(locationDao, times(1)).create(argumentCaptor.capture());

        Location locationToCreate = argumentCaptor.getValue();

        assertThat(locationToCreate.getName()).isEqualTo(KHARKIV_DTO.getName());
        assertThat(locationToCreate.getLatitude()).isEqualTo(KHARKIV_DTO.getLatitude());
        assertThat(locationToCreate.getLongitude()).isEqualTo(KHARKIV_DTO.getLongitude());
        assertThat(locationToCreate.getUser().getId()).isEqualTo(KHARKIV_DTO.getUserId());
    }

    @Test
    void createLocation_locationNotCreated_userHaveAlreadyThreeLocations() {
        doReturn(3L).when(locationDao).findNumberUserLocations(KHARKIV_DTO.getUserId());

        TooManyLocationsException tooManyLocationsException = assertThrows(TooManyLocationsException.class, () -> locationService.createLocation(KHARKIV_DTO));
        assertThat(tooManyLocationsException.getMessage()).isEqualTo("You can't have more than 3 locations");
        verify(locationDao, times(1)).findNumberUserLocations(KHARKIV_DTO.getUserId());
        verify(locationDao, times(0)).create(any());
    }

    @Test
    void getAllLocationsByUserId_getEmptyList() {
        doReturn(listLocationsEmpty).when(locationDao).findLocationsByUserId(TIMUR.getId());

        List<Location> locationList = locationService.getAllLocationByUserId(TIMUR.getId());

        assertThat(locationList).isEmpty();
    }

    @Test
    void getAllLocationsByUserId_getListWithUserLocation() {
        doReturn(listTimurLocations).when(locationDao).findLocationsByUserId(TIMUR.getId());

        List<Location> locationList = locationService.getAllLocationByUserId(TIMUR.getId());

        assertThat(locationList).hasSize(1);
        Location maybeLocation = locationList.get(0);
        assertThat(maybeLocation).isEqualTo(KHARKIV);
    }

    @Test
    void deleteLocationsByCoordinate() {
        locationService.deleteLocationByCoordinate(KHARKIV_DTO);
        verify(locationDao, times(1)).deleteLocation(KHARKIV_DTO.getLatitude(), KHARKIV_DTO.getLongitude(), KHARKIV_DTO.getUserId());
    }
}