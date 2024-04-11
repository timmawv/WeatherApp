package avlyakulov.timur.dao.api;

import avlyakulov.timur.JsonLoadTestBase;
import avlyakulov.timur.dto.api.GeoCityDto;
import avlyakulov.timur.dto.UserDto;
import avlyakulov.timur.dto.WeatherCityDto;
import avlyakulov.timur.model.Location;
import avlyakulov.timur.service.LocationService;
import avlyakulov.timur.servlet.util.HttpRequestResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class OpenWeatherServiceTest extends JsonLoadTestBase {

    private final UserDto TIMUR = new UserDto(1, "timur");

    private GeoCityDto POLTAVA = new GeoCityDto(new BigDecimal("49.5897423"), new BigDecimal("34.5507948"), "Ukraine", "Poltava");

    private final List<GeoCityDto> locationListFromOpenGeo = List.of(POLTAVA);
    private final List<GeoCityDto> emptyLocationListFromOpenGeo = Collections.emptyList();
    private Location POLTAVA_LOCATION = new Location("Poltava", new BigDecimal("49.5897423"), new BigDecimal("34.5507948"), null);

    private final List<Location> userNoLocations = Collections.emptyList();

    private final List<Location> userHasOneLocation = List.of(POLTAVA_LOCATION);

    @Mock
    private OpenGeoService openGeoService;

    @Mock
    private LocationService locationService;

    @Mock
    private UrlBuilder urlBuilder;

    @Mock
    private HttpRequestResponse httpRequestResponse;

    @InjectMocks
    private OpenWeatherService openWeatherService;

    @Test
    void getWeatherListFromCityName_getOneWeather() throws URISyntaxException, IOException, InterruptedException {
        doReturn(userNoLocations).when(locationService).getAllLocationByUserId(TIMUR.getUserId());
        doReturn(locationListFromOpenGeo).when(openGeoService).getCitiesDtoByName(POLTAVA.getCityName());
        doReturn(weatherJson).when(httpRequestResponse).getBodyOfResponse(anyString());

        List<WeatherCityDto> weatherListFromCityName = openWeatherService.getWeatherListFromCityNameLoggedUser(POLTAVA.getCityName(), TIMUR);

        assertThat(weatherListFromCityName).hasSize(1);

        WeatherCityDto maybeWeatherCityDto = weatherListFromCityName.get(0);

        //city information test
        assertThat(maybeWeatherCityDto.getCityInformation()).isEqualTo(POLTAVA);

        //weather test
        assertThat(maybeWeatherCityDto.getWeather().getMain()).isEqualTo("Clouds, 11°C");
        assertThat(maybeWeatherCityDto.getWeather().getDescription()).isEqualTo("scattered clouds");
        assertThat(maybeWeatherCityDto.getWeather().getIcon()).isEqualTo("https://github.com/timmawv/WeatherApp/blob/main/img/animated/cloudy_scattered.svg?raw=true");

        //main weather test
        assertThat(maybeWeatherCityDto.getTemperature().getTemp()).isEqualTo("10.53°C");
        assertThat(maybeWeatherCityDto.getTemperature().getFeelsLike()).isEqualTo("8.74°C");
        assertThat(maybeWeatherCityDto.getTemperature().getTemperatureMin()).isEqualTo("10.53°C");
        assertThat(maybeWeatherCityDto.getTemperature().getTemperatureMax()).isEqualTo("10.53°C");
        assertThat(maybeWeatherCityDto.getTemperature().getHumidity()).isEqualTo("42%");

        //visibility weather test
        assertThat(maybeWeatherCityDto.getVisibility()).isEqualTo("10.0 km");

        //wind test
        assertThat(maybeWeatherCityDto.getWind().getSpeed()).isEqualTo("2.94 m/s");
        assertThat(maybeWeatherCityDto.getWind().getWindDirection()).isEqualTo("North-West");

        //Sunrise and sunset test and country code test
        assertThat(maybeWeatherCityDto.getSolarCycle().getCountry()).isEqualTo("Ukraine");
        assertThat(maybeWeatherCityDto.getSolarCycle().getSunrise()).isEqualTo("06:09");
        assertThat(maybeWeatherCityDto.getSolarCycle().getSunset()).isEqualTo("19:18");

        //check favorite weather
        assertThat(maybeWeatherCityDto.isFavorite()).isFalse();
    }

    @Test
    void getWeatherListFromCityName_getOneWeather_userHasSameLocation() throws URISyntaxException, IOException, InterruptedException {
        doReturn(userHasOneLocation).when(locationService).getAllLocationByUserId(TIMUR.getUserId());
        doReturn(locationListFromOpenGeo).when(openGeoService).getCitiesDtoByName(POLTAVA.getCityName());
        doReturn(weatherJson).when(httpRequestResponse).getBodyOfResponse(anyString());

        List<WeatherCityDto> weatherListFromCityName = openWeatherService.getWeatherListFromCityNameLoggedUser(POLTAVA.getCityName(), TIMUR);

        assertThat(weatherListFromCityName).hasSize(1);

        WeatherCityDto maybeWeatherCityDto = weatherListFromCityName.get(0);

        assertThat(maybeWeatherCityDto.isFavorite()).isTrue();
    }

    @Test
    void getWeatherListFromCityName_getZeroWeather_cityNotFound() throws URISyntaxException, IOException, InterruptedException {
        doReturn(emptyLocationListFromOpenGeo).when(openGeoService).getCitiesDtoByName(anyString());

        List<WeatherCityDto> weatherListFromCityName = openWeatherService.getWeatherListFromCityNameLoggedUser("Dummy", TIMUR);

        assertThat(weatherListFromCityName).isEmpty();
    }

    @Test
    void getWeatherByUserLocations_userHasOneLocation() throws URISyntaxException, IOException, InterruptedException {
        doReturn(weatherJson).when(httpRequestResponse).getBodyOfResponse(anyString());
        doReturn(userHasOneLocation).when(locationService).getAllLocationByUserId(anyInt());

        List<WeatherCityDto> weatherByUserLocations = openWeatherService.getWeatherByUserLocations(TIMUR.getUserId());

        assertThat(weatherByUserLocations).hasSize(1);
    }

    @Test
    void getWeatherByUserLocations_userHasNoLocation() throws URISyntaxException, IOException, InterruptedException {
        doReturn(userNoLocations).when(locationService).getAllLocationByUserId(anyInt());

        List<WeatherCityDto> weatherByUserLocations = openWeatherService.getWeatherByUserLocations(TIMUR.getUserId());

        assertThat(weatherByUserLocations).isEmpty();
    }
}