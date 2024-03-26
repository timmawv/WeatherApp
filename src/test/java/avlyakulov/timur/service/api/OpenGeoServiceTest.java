package avlyakulov.timur.service.api;

import avlyakulov.timur.custom_exception.BadHttpRequest;
import avlyakulov.timur.custom_exception.ModelNotFoundException;
import avlyakulov.timur.custom_exception.ServerErrorException;
import avlyakulov.timur.dto.GeoCityDto;
import avlyakulov.timur.servlet.util.HttpRequestResponseUtil;
import avlyakulov.timur.util.hibernate.HibernateSingletonUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class OpenGeoServiceTest {

    @Mock
    private HttpRequestResponseUtil httpRequestResponseUtil;

    @InjectMocks
    OpenGeoService openGeoService;

    @BeforeAll
    static void setUpAll() {
        HibernateSingletonUtil.initEnvironments();
    }

    @BeforeEach
    void setUp() {
        openGeoService = new OpenGeoService();
        openGeoService.setHttpRequestResponseUtil(httpRequestResponseUtil);
    }

    @Test
    public void getCityCoordinateByName_getListGeoCityDto_cityExists() throws URISyntaxException, IOException, InterruptedException {
        doReturn(GetJsonFile.getJsonFileGeo()).when(httpRequestResponseUtil).getBodyOfResponse(anyString());

        List<GeoCityDto> listCities = openGeoService.getCityCoordinateByName(anyString());
        GeoCityDto geoCityDto = listCities.get(0);

        assertEquals(3, listCities.size());
        assertEquals("Kyiv", geoCityDto.getCityName());
        assertEquals(new BigDecimal("50.4500336"), geoCityDto.getLatitude());
        assertEquals(new BigDecimal("30.5241361"), geoCityDto.getLongitude());
        assertEquals("Ukraine", geoCityDto.getCountry());
        assertNull(geoCityDto.getState());
    }

    @Test
    public void getCityCoordinateByName_throwException_badRequest() throws URISyntaxException, IOException, InterruptedException {
        doThrow(BadHttpRequest.class).when(httpRequestResponseUtil).getBodyOfResponse(anyString());

        ModelNotFoundException modelNotFoundException = assertThrows(ModelNotFoundException.class, () -> openGeoService.getCityCoordinateByName(anyString()));
        assertEquals("Bad request from user", modelNotFoundException.getMessage());
    }

    @Test
    public void getCityCoordinateByName_throwException_openWeatherNotWork() throws URISyntaxException, IOException, InterruptedException {
        doThrow(ServerErrorException.class).when(httpRequestResponseUtil).getBodyOfResponse(anyString());

        assertThrows(ServerErrorException.class, () -> openGeoService.getCityCoordinateByName(anyString()));
    }
}