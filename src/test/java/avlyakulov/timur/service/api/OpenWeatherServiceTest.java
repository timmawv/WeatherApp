package avlyakulov.timur.service.api;

import avlyakulov.timur.dto.GeoCityDto;
import avlyakulov.timur.dto.WeatherCityDto;
import avlyakulov.timur.service.LocationService;
import avlyakulov.timur.servlet.util.HttpRequestResponseUtil;
import avlyakulov.timur.util.hibernate.HibernateSingletonUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

class OpenWeatherServiceTest {

    @Mock
    private OpenGeoService openGeoService;

    @Mock
    private HttpRequestResponseUtil httpRequestResponseUtil;

    @Mock
    private LocationService locationService;

    @InjectMocks
    private OpenWeatherService openWeatherService;

    @BeforeAll
    static void setUpAll() {
        HibernateSingletonUtil.initEnvironments();
    }

    @Test
    public void getWeatherListFromHttpRequest_getWeatherList_coordinateExists() throws URISyntaxException, IOException, InterruptedException {
        doReturn(any()).when(locationService).getAllLocationByUserId(any());
        doReturn(List.of(new GeoCityDto("Kharkiv", new BigDecimal("36.2304"), new BigDecimal("49.9903"), "Ukraine"))).when(openGeoService).getCityCoordinateByName(anyString());
        doReturn(GetJsonFile.getJsonFileWeather()).when(httpRequestResponseUtil).getBodyOfResponse(anyString());
        doReturn(anyString()).when(openWeatherService).concatenateUrlWeather(any(GeoCityDto.class));

        List<WeatherCityDto> weatherListFromHttpRequest = openWeatherService.getWeatherListFromHttpRequest(any(), any());
        WeatherCityDto weatherCityDto = weatherListFromHttpRequest.get(0);
        assertEquals(1, weatherListFromHttpRequest.size());
        assertEquals("Kharkiv", weatherCityDto.getCityInformation().getCityName());
        assertEquals(new BigDecimal("36.2304"), weatherCityDto.getCityInformation().getLatitude());
        assertEquals(new BigDecimal("49.9903"), weatherCityDto.getCityInformation().getLongitude());
        assertEquals("Ukraine", weatherCityDto.getCityInformation().getCountry());
        assertEquals("Clouds", weatherCityDto.getWeather().getMain());
        assertEquals("overcast clouds", weatherCityDto.getWeather().getDescription());
        assertEquals("https://github.com/timmawv/WeatherApp/blob/main/img/animated/overcast-day.svg?raw=true", weatherCityDto.getWeather().getIcon());
        assertEquals("8.86°C", weatherCityDto.getTemperature().getTemp());
        assertEquals("6.27°C", weatherCityDto.getTemperature().getFeelsLike());
        assertEquals("8.86°C", weatherCityDto.getTemperature().getTemperatureMax());
        assertEquals("8.86°C", weatherCityDto.getTemperature().getTemperatureMin());
        assertEquals("50%", weatherCityDto.getTemperature().getHumidity());
        assertEquals("10.0 km", weatherCityDto.getVisibility());
        assertEquals("4.73 m/s", weatherCityDto.getWind().getSpeed());
        assertEquals("East", weatherCityDto.getWind().getWindDirection());
    }
}