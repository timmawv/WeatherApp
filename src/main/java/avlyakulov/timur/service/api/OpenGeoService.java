package avlyakulov.timur.service.api;

import avlyakulov.timur.custom_exception.BadHttpRequest;
import avlyakulov.timur.custom_exception.ModelNotFoundException;
import avlyakulov.timur.dto.GeoCityDto;
import avlyakulov.timur.servlet.util.HttpRequestResponseUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class OpenGeoService {
    private final String urlGeo = "http://api.openweathermap.org/geo/1.0/direct?limit=3";

    private final String city = "&q=";

    private final String appId = "&appid=".concat(System.getProperty("API_WEATHER_KEY"));

    private final HttpRequestResponseUtil httpRequestResponseUtil;

    public OpenGeoService(HttpRequestResponseUtil httpRequestResponseUtil) {
        this.httpRequestResponseUtil = httpRequestResponseUtil;
    }

    public List<GeoCityDto> getCitiesDtoByName(String nameCity) throws URISyntaxException, IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        String urlGeoFull = urlGeo.concat(city.concat(nameCity)).concat(appId);
        try {
            String bodyOfResponse = httpRequestResponseUtil.getBodyOfResponse(urlGeoFull);
            return objectMapper.readValue(bodyOfResponse, new TypeReference<>() {
            });
        } catch (BadHttpRequest e) {
            throw new ModelNotFoundException("Bad request from user");
        }
    }
}