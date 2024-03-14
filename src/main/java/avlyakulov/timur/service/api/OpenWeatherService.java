package avlyakulov.timur.service.api;

import avlyakulov.timur.dto.GeoCityDto;
import avlyakulov.timur.dto.WeatherCityDto;
import avlyakulov.timur.servlet.util.HttpRequestResponseUtil;
import avlyakulov.timur.util.api.GetUrlIconOfWeatherByIcon;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OpenWeatherService {
    private final StringBuilder urlWeather = new StringBuilder("https://api.openweathermap.org/data/2.5/weather?units=metric");

    private final StringBuilder latitude = new StringBuilder("&lat=");
    private final StringBuilder longitude = new StringBuilder("&lon=");

    private final String appId = "&appid=" + System.getProperty("API_WEATHER_KEY");

    private final String celsiusSign = "°C";

    private final OpenGeoService openGeoService = new OpenGeoService();

    ObjectMapper objectMapper = new ObjectMapper();

    public List<WeatherCityDto> getWeatherList(String cityName) throws URISyntaxException, IOException, InterruptedException {
        List<WeatherCityDto> weatherCityDtoList = new ArrayList<>();
        List<GeoCityDto> cityCoordinateByName = openGeoService.getCityCoordinateByName(cityName);
        for (GeoCityDto geoCityDto : cityCoordinateByName) {
            WeatherCityDto weatherCityDto = new WeatherCityDto(geoCityDto.getCountry(), geoCityDto.getState());
            urlWeather.append(latitude.append(geoCityDto.getLatitude()))
                    .append(longitude.append(geoCityDto.getLatitude()))
                    .append(appId);
            String bodyOfResponse = HttpRequestResponseUtil.getBodyOfResponse(urlWeather.toString());
            JsonNode jsonNode = objectMapper.readTree(bodyOfResponse);
            setWeather(weatherCityDto, jsonNode);
            setTemperatureWeather(weatherCityDto, jsonNode);
            setVisibilityInKilometres(weatherCityDto, jsonNode);
            setWindWeather(weatherCityDto, jsonNode);
            setTimeAndSunriseAndSunsetToWeather(weatherCityDto, jsonNode);
            weatherCityDtoList.add(weatherCityDto);
        }
        return weatherCityDtoList;
    }

    private void setWeather(WeatherCityDto weather, JsonNode jsonNode) {
        JsonNode weatherNode = jsonNode.get("weather");
        if (weatherNode != null) {
            weather.setMainWeather(weatherNode.get("main").asText());
            weather.setDetailedWeather(weatherNode.get("description").asText());
            String urlIcon = GetUrlIconOfWeatherByIcon.getUrlWeatherIcon(weatherNode.get("icon").asText());
            weather.setUrlIconWeather(urlIcon);
        }
    }

    private void setTemperatureWeather(WeatherCityDto weather, JsonNode jsonNode) {
        JsonNode weatherNode = jsonNode.get("main");
        if (weatherNode != null) {
            weather.setTemperatureWeather(weatherNode.get("temp").asText() + celsiusSign);
            weather.setFeelsLikeWeather(weatherNode.get("feels_like").asText() + celsiusSign);
            weather.setMinTemperatureWeather(weatherNode.get("temp_min").asText() + celsiusSign);
            weather.setMaxTemperatureWeather(weatherNode.get("temp_max").asText() + celsiusSign);
            weather.setHumidityWeather(weatherNode.get("humidity").asText());
        }
    }

    private void setVisibilityInKilometres(WeatherCityDto weather, JsonNode jsonNode) {
        double visibility = jsonNode.get("visibility").asDouble() / 1000;
        weather.setVisibilityKilometersWeather(visibility + " km");
    }

    private void setWindWeather(WeatherCityDto weather, JsonNode jsonNode) {
        JsonNode windNode = jsonNode.get("wind");
        if (windNode != null) {
            weather.setWindSpeedWeather(windNode.get("speed") + " m/s");
        }
    }

    private void setTimeAndSunriseAndSunsetToWeather(WeatherCityDto weather, JsonNode jsonNode) {
        long currentTimeEpoch = jsonNode.get("dt").asLong();
        if (currentTimeEpoch != 0) {
            LocalDateTime currentTime = LocalDateTime.from(Instant.ofEpochSecond(currentTimeEpoch));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String currentTimeStr = currentTime.format(formatter);
            weather.setCurrentTimeWeather(currentTimeStr);
        }
        JsonNode sysNode = jsonNode.get("sys");
        if (sysNode != null) {
            long sunriseEpoch = sysNode.get("sunrise").asLong();
            long sunsetEpoch = sysNode.get("sunset").asLong();

            // Конвертация времени в формат LocalDateTime
            LocalDateTime sunriseDateTime = Instant.ofEpochSecond(sunriseEpoch)
                    .atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime sunsetDateTime = Instant.ofEpochSecond(sunsetEpoch)
                    .atZone(ZoneId.systemDefault()).toLocalDateTime();

            // Форматирование времени в нужный формат
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String formattedSunrise = sunriseDateTime.format(formatter);
            String formattedSunset = sunsetDateTime.format(formatter);

            weather.setSunriseWeather(formattedSunrise);
            weather.setSunsetWeather(formattedSunset);
        }
    }
}