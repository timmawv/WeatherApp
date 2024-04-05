package avlyakulov.timur.service.api;

import avlyakulov.timur.JsonLoadTestBase;
import avlyakulov.timur.custom_exception.BadHttpRequest;
import avlyakulov.timur.custom_exception.ModelNotFoundException;
import avlyakulov.timur.dto.GeoCityDto;
import avlyakulov.timur.servlet.util.HttpRequestResponseUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class OpenGeoServiceTest extends JsonLoadTestBase {

    @Mock
    private HttpRequestResponseUtil httpRequestResponseUtil;

    @InjectMocks
    private OpenGeoService openGeoService;

    @Test
    void getCitiesDtoByName_locationExists() throws URISyntaxException, IOException, InterruptedException {
        doReturn(getGeoJson()).when(httpRequestResponseUtil).getBodyOfResponse(anyString());

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
        doReturn(getGeosJson()).when(httpRequestResponseUtil).getBodyOfResponse(anyString());

        List<GeoCityDto> cities = openGeoService.getCitiesDtoByName("Poltava");

        assertThat(cities).hasSize(3);
    }

    @Test
    void getCitiesDtoByName_locationsNotExist() throws URISyntaxException, IOException, InterruptedException {
        doReturn(getEmptyJson()).when(httpRequestResponseUtil).getBodyOfResponse(anyString());

        List<GeoCityDto> cities = openGeoService.getCitiesDtoByName("Dummy");

        assertThat(cities).isEmpty();
    }

    @Test
    void getCitiesDtoByName_throwsException_badRequestFromUser() throws URISyntaxException, IOException, InterruptedException {
        doThrow(BadHttpRequest.class).when(httpRequestResponseUtil).getBodyOfResponse(anyString());

        assertThrows(ModelNotFoundException.class, () -> openGeoService.getCitiesDtoByName("   "));
    }
}