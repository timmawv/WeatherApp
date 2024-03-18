package avlyakulov.timur.service.api;

import avlyakulov.timur.dto.GeoCityDto;
import avlyakulov.timur.dto.UserDto;
import avlyakulov.timur.dto.WeatherCityDto;
import avlyakulov.timur.model.Location;
import avlyakulov.timur.service.LocationService;
import avlyakulov.timur.servlet.util.HttpRequestResponseUtil;
import avlyakulov.timur.util.api.GetUrlIconOfWeatherByIcon;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neovisionaries.i18n.CountryCode;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OpenWeatherService {
    private final String urlWeather = "https://api.openweathermap.org/data/2.5/weather?units=metric";

    private final String latitude = "&lat=";
    private final String longitude = "&lon=";

    private final String appId = "&appid=" + System.getProperty("API_WEATHER_KEY");

    private final String celsiusSign = "°C";

    private final OpenGeoService openGeoService = new OpenGeoService();

    private final LocationService locationService = new LocationService();

    ObjectMapper objectMapper = new ObjectMapper();

    public List<WeatherCityDto> getWeatherListFromHttpRequest(String cityName, UserDto userDto) throws URISyntaxException, IOException, InterruptedException {
        List<Location> locationList = locationService.getAllLocationByUserId(userDto.getUserId());
        List<WeatherCityDto> weatherCityDtoList = new ArrayList<>();
        List<GeoCityDto> cityCoordinateByName = openGeoService.getCityCoordinateByName(cityName);
        for (GeoCityDto geoCityDto : cityCoordinateByName) {
            WeatherCityDto weatherCityDto = new WeatherCityDto(geoCityDto);
            String urlWeatherFull = urlWeather.concat(latitude.concat(geoCityDto.getLatitude().toString()))
                    .concat(longitude.concat(geoCityDto.getLatitude().toString()))
                    .concat(appId);
            String bodyOfResponse = HttpRequestResponseUtil.getBodyOfResponse(urlWeatherFull);
            JsonNode jsonNode = objectMapper.readTree(bodyOfResponse);
            setWeather(weatherCityDto, jsonNode);
            setTemperatureWeather(weatherCityDto, jsonNode);
            setVisibilityInKilometres(weatherCityDto, jsonNode);
            setWindWeather(weatherCityDto, jsonNode);
            setTimeAndSunriseAndSunsetToWeather(weatherCityDto, jsonNode);
            setFavoriteToWeather(locationList, weatherCityDto);
            weatherCityDtoList.add(weatherCityDto);
        }
        return weatherCityDtoList;
    }

    public List<WeatherCityDto> getWeatherByUserLocations(List<Location> locationList) throws URISyntaxException, IOException, InterruptedException {
        List<WeatherCityDto> weatherCityDtoList = new ArrayList<>();
        for (Location location : locationList) {
            String urlWeatherFull = urlWeather.concat(latitude.concat(location.getLatitude().toString()))
                    .concat(longitude.concat(location.getLongitude().toString()))
                    .concat(appId);
            String bodyOfResponse = HttpRequestResponseUtil.getBodyOfResponse(urlWeatherFull);
            JsonNode jsonNode = objectMapper.readTree(bodyOfResponse);
            GeoCityDto geoInfoWeather = getGeoInfoWeather(location, jsonNode);
            WeatherCityDto weatherCityDto = new WeatherCityDto(geoInfoWeather);
            setWeather(weatherCityDto, jsonNode);
            setTemperatureWeather(weatherCityDto, jsonNode);
            setVisibilityInKilometres(weatherCityDto, jsonNode);
            setWindWeather(weatherCityDto, jsonNode);
            setTimeAndSunriseAndSunsetToWeather(weatherCityDto, jsonNode);
            setFavoriteToWeather(locationList, weatherCityDto);
            weatherCityDtoList.add(weatherCityDto);
        }
        return weatherCityDtoList;
    }

    private GeoCityDto getGeoInfoWeather(Location location, JsonNode jsonNode) {
        return new GeoCityDto(location.getLatitude(), location.getLongitude(),
                CountryCode.getByAlpha2Code(jsonNode.get("sys").get("country").asText()).getName(),
                location.getName()
        );

    }

    private void setWeather(WeatherCityDto weather, JsonNode jsonNode) {
        JsonNode jsonWeather = jsonNode.get("weather");
        if (jsonWeather != null) {
            JsonNode jsonWeatherNode = jsonWeather.get(0);
            weather.setMainWeather(jsonWeatherNode.get("main").asText());
            weather.setDetailedWeather(jsonWeatherNode.get("description").asText());
            String icon = jsonWeatherNode.get("icon").asText();
            String urlIcon = GetUrlIconOfWeatherByIcon.getUrlWeatherIcon(icon);
            weather.setUrlIconWeather(urlIcon);
        }
    }

    private void setTemperatureWeather(WeatherCityDto weather, JsonNode jsonNode) {
        JsonNode temperatureNode = jsonNode.get("main");
        if (temperatureNode != null) {
            weather.setTemperatureWeather(temperatureNode.get("temp").asText() + celsiusSign);
            weather.setFeelsLikeWeather(temperatureNode.get("feels_like").asText() + celsiusSign);
            weather.setMinTemperatureWeather(temperatureNode.get("temp_min").asText() + celsiusSign);
            weather.setMaxTemperatureWeather(temperatureNode.get("temp_max").asText() + celsiusSign);
            weather.setHumidityWeather(temperatureNode.get("humidity").asText() + "%");
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
            Date date = new Date(currentTimeEpoch * 1000L);

            // Форматирование даты и времени
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String formattedDate = sdf.format(date);
//            Instant instant = Instant.ofEpochMilli(currentTimeEpoch);
//            LocalDateTime currentTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
//            String currentTimeStr = currentTime.format(formatter);
            weather.setCurrentTimeWeather(formattedDate);
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

    private void setFavoriteToWeather(List<Location> locationList, WeatherCityDto weather) {
        locationList
                .forEach(l -> {
                    if (l.getLatitude().equals(weather.getLatitude()) && l.getLongitude().equals(weather.getLongitude())) {
                        weather.setFavorite(true);
                    }
                });
    }
}