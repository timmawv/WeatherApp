package avlyakulov.timur.dao.api;

import avlyakulov.timur.JsonLoadTestBase;
import avlyakulov.timur.custom_exception.BadHttpRequest;
import avlyakulov.timur.custom_exception.ModelNotFoundException;
import avlyakulov.timur.dto.api.GeoCityDto;
import avlyakulov.timur.servlet.util.HttpRequestResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class OpenGeoServiceTest extends JsonLoadTestBase {

    @Mock
    private HttpRequestResponse httpRequestResponse;


    private UrlBuilder urlBuilder = new UrlBuilder();


    private OpenGeoService openGeoService;

    @BeforeEach
    void setUp() {
        openGeoService = new OpenGeoService(httpRequestResponse, urlBuilder);
    }

    @Test
    void getCitiesDtoByName_locationExists() throws URISyntaxException, IOException, InterruptedException {
        doReturn(geoJson).when(httpRequestResponse).getBodyOfResponse(anyString());

        List<GeoCityDto> cities = openGeoService.getCitiesDtoByName("Poltava");

        assertThat(cities).hasSize(1);

        GeoCityDto geoCityDto = cities.get(0);

        assertThat(geoCityDto.getLatitude()).isEqualTo(new BigDecimal("49.5897423"));
        assertThat(geoCityDto.getLongitude()).isEqualTo(new BigDecimal("34.5507948"));
        assertThat(geoCityDto.getCountry()).isEqualTo("Ukraine");
        assertThat(geoCityDto.getState()).isEqualTo("Poltava Oblast");
        assertThat(geoCityDto.getCityName()).isEqualTo("Poltava");
    }

    @Test
    void getCitiesDtoByName_locationsExist() throws URISyntaxException, IOException, InterruptedException {
        doReturn(geosJson).when(httpRequestResponse).getBodyOfResponse(anyString());

        List<GeoCityDto> cities = openGeoService.getCitiesDtoByName("Poltava");

        assertThat(cities).hasSize(3);
    }

    @Test
    void getCitiesDtoByName_locationsNotExist() throws URISyntaxException, IOException, InterruptedException {
        doReturn(emptyJson).when(httpRequestResponse).getBodyOfResponse(anyString());

        List<GeoCityDto> cities = openGeoService.getCitiesDtoByName("Dummy");

        assertThat(cities).isEmpty();
    }

    @Test
    void getCitiesDtoByName_throwsException_badRequestFromUser() throws URISyntaxException, IOException, InterruptedException {
        doThrow(BadHttpRequest.class).when(httpRequestResponse).getBodyOfResponse(anyString());

        assertThrows(ModelNotFoundException.class, () -> openGeoService.getCitiesDtoByName("   "));
    }
}