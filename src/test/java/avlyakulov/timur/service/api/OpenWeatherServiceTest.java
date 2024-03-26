package avlyakulov.timur.service.api;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class OpenWeatherServiceTest {

    @Mock
    OpenGeoService openGeoService;

    @Mock
    private OpenWeatherService openWeatherService = new OpenWeatherService();

    @Test
    public void getWeatherListFromHttpRequest_getWeatherList_coordinateExists() {

    }
}