package avlyakulov.timur.service.api;

import avlyakulov.timur.dto.GeoCityDto;
import avlyakulov.timur.servlet.util.HttpRequestResponseUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neovisionaries.i18n.CountryCode;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class OpenGeoService {
    private final String urlGeo = "http://api.openweathermap.org/geo/1.0/direct?limit=3";

    private final String city = "&q=";

    private final String appId = "&appid=" + System.getProperty("API_WEATHER_KEY");

    public List<GeoCityDto> getCityCoordinateByName(String nameCity) throws URISyntaxException, IOException, InterruptedException {
        String urlGeoFull = urlGeo.concat(city.concat(nameCity)).concat(appId);
        String bodyOfResponse = HttpRequestResponseUtil.getBodyOfResponse(urlGeoFull);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(bodyOfResponse, new TypeReference<>(){});
    }
}