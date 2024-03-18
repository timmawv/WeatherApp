package avlyakulov.timur.service.api;

import avlyakulov.timur.dto.GeoCityDto;
import avlyakulov.timur.servlet.util.HttpRequestResponseUtil;
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
        List<GeoCityDto> geoCityDtoList = new ArrayList<>();
        String urlGeoFull = urlGeo.concat(city.concat(nameCity)).concat(appId);
        String bodyOfResponse = HttpRequestResponseUtil.getBodyOfResponse(urlGeoFull);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(bodyOfResponse);
        for (JsonNode node : jsonNode) {
            if (node.has("lat") && node.has("lon") && node.has("country")) {
                BigDecimal lat = new BigDecimal(node.get("lat").asText());
                BigDecimal lon = new BigDecimal(node.get("lon").asText());
                String country = CountryCode.getByAlpha2Code(node.get("country").asText()).getName();
                if (node.has("state") && node.has("name")) {
                    String state = node.get("state").asText();
                    String city = node.get("name").asText();
                    GeoCityDto geoCityDto = new GeoCityDto(lat, lon, country, state, city);
                    geoCityDtoList.add(geoCityDto);
                } else {
                    GeoCityDto geoCityDto = new GeoCityDto(lat, lon, country, nameCity);
                    geoCityDtoList.add(geoCityDto);
                }
            }
        }
        return geoCityDtoList;
    }
}