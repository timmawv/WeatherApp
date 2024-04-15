package avlyakulov.timur.dao.api;

import avlyakulov.timur.dto.ForecastHourlyDto;
import avlyakulov.timur.dto.LocationDto;
import avlyakulov.timur.servlet.util.HttpRequestResponse;
import avlyakulov.timur.util.api.SetMainWeatherUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URISyntaxException;

public class OpenForecastService {
    private final String urlForecast = "https://api.openweathermap.org/data/3.0/onecall?units=metric&lat=%s&lon=%s";

    private final UrlBuilder urlBuilder;

    private final HttpRequestResponse httpRequestResponse;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public OpenForecastService(UrlBuilder urlBuilder, HttpRequestResponse httpRequestResponse) {
        this.urlBuilder = urlBuilder;
        this.httpRequestResponse = httpRequestResponse;
    }

    public ForecastHourlyDto getForecastHourly(LocationDto locationDto) {
        String fullUrlForecast = getFullUrlForecast(locationDto);
        try {
            String bodyOfResponse = httpRequestResponse.getBodyOfResponse(fullUrlForecast);
            ForecastHourlyDto forecastHourlyDto = objectMapper.readValue(bodyOfResponse, new TypeReference<>() {
            });
            forecastHourlyDto.getForecastHourly().forEach(SetMainWeatherUtil::setMainWeather);
            forecastHourlyDto.setCityInfo(locationDto.getName());
            return forecastHourlyDto;
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    private String getFullUrlForecast(LocationDto locationDto) {
        return urlBuilder.buildUrlWithParameters(
                urlForecast,
                locationDto.getLatitude().toString(),
                locationDto.getLongitude().toString()
        );
    }
}
