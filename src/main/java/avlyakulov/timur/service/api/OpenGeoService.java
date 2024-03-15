package avlyakulov.timur.service.api;

import avlyakulov.timur.dto.GeoCityDto;
import avlyakulov.timur.servlet.util.HttpRequestResponseUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neovisionaries.i18n.CountryCode;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class OpenGeoService {
    private final StringBuilder urlGeo = new StringBuilder("http://api.openweathermap.org/geo/1.0/direct?limit=5");

    private final StringBuilder city = new StringBuilder("&q=");

    private final String appId = "&appid=" + System.getProperty("API_WEATHER_KEY");

    public List<GeoCityDto> getCityCoordinateByName(String nameCity) throws URISyntaxException, IOException, InterruptedException {
        List<GeoCityDto> geoCityDtoList = new ArrayList<>();
        city.append(nameCity);
        urlGeo.append(city).append(appId);
        String bodyOfResponse = HttpRequestResponseUtil.getBodyOfResponse(urlGeo.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(bodyOfResponse);
        for (JsonNode node : jsonNode) {
            if (node.has("lat") && node.has("lon") && node.has("country") && node.has("state")) {
                double lat = node.get("lat").asDouble();
                double lon = node.get("lon").asDouble();
                String country = CountryCode.getByAlpha2Code(node.get("country").asText()).getName();
                String state = node.get("state").asText();
                GeoCityDto geoCityDto = new GeoCityDto(lat, lon, country, state, nameCity);
                geoCityDtoList.add(geoCityDto);
            }
        }
        return geoCityDtoList;
    }
}