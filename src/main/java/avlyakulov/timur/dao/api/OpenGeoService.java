package avlyakulov.timur.dao.api;

import avlyakulov.timur.custom_exception.BadHttpRequest;
import avlyakulov.timur.custom_exception.GlobalApiException;
import avlyakulov.timur.custom_exception.ModelNotFoundException;
import avlyakulov.timur.dto.api.GeoCityDto;
import avlyakulov.timur.servlet.util.HttpRequestResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class OpenGeoService {

    private final UrlBuilder urlBuilder;

    private final String cityNameRegex = ".*\\d+.*";

    private final HttpRequestResponse httpRequestResponse;

    private final String urlGeo = "http://api.openweathermap.org/geo/1.0/direct?limit=3&q=%s";


    public OpenGeoService(HttpRequestResponse httpRequestResponse, UrlBuilder urlBuilder) {
        this.httpRequestResponse = httpRequestResponse;
        this.urlBuilder = urlBuilder;
    }

    public List<GeoCityDto> getCitiesDtoByName(String nameCity) throws URISyntaxException, IOException, InterruptedException {
        if (!isCityNameValid(nameCity)) {
            throw new GlobalApiException("Don't enter numbers, blank name, spaces in name.");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String urlGeoFull = urlBuilder.buildUrlWithParameters(urlGeo, nameCity);
        try {
            String bodyOfResponse = httpRequestResponse.getBodyOfResponse(urlGeoFull);
            return objectMapper.readValue(bodyOfResponse, new TypeReference<>() {
            });
        } catch (BadHttpRequest e) {
            throw new ModelNotFoundException("Bad request from user");
        }
    }

    private boolean isCityNameValid(String cityName) {
        if (cityName.isBlank() || cityName.contains(" ") || cityName.matches(cityNameRegex)) {
            return false;
        } else {
            return true;
        }
    }
}