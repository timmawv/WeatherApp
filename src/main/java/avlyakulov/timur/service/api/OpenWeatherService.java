package avlyakulov.timur.service.api;

import avlyakulov.timur.dto.GeoCityDto;
import avlyakulov.timur.dto.UserDto;
import avlyakulov.timur.dto.WeatherCityDto;
import avlyakulov.timur.model.Location;
import avlyakulov.timur.service.LocationService;
import avlyakulov.timur.servlet.util.HttpRequestResponseUtil;
import avlyakulov.timur.util.api.SetMainWeatherUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class OpenWeatherService {
    private final String urlWeather = "https://api.openweathermap.org/data/2.5/weather?units=metric";

    private final String latitude = "&lat=";
    private final String longitude = "&lon=";

    private final String appId = "&appid=" + System.getProperty("API_WEATHER_KEY");

    private final OpenGeoService openGeoService = new OpenGeoService();

    private final LocationService locationService = new LocationService();

    ObjectMapper objectMapper = new ObjectMapper();

    public List<WeatherCityDto> getWeatherListFromHttpRequest(String cityName, UserDto userDto) throws URISyntaxException, IOException, InterruptedException {
        List<Location> locationList = locationService.getAllLocationByUserId(userDto.getUserId());
        List<WeatherCityDto> weatherCityDtoList = new ArrayList<>();
        List<GeoCityDto> cityCoordinateByName = openGeoService.getCityCoordinateByName(cityName);
        for (GeoCityDto geoCityDto : cityCoordinateByName) {
            String urlWeatherFull = urlWeather.concat(latitude.concat(geoCityDto.getLatitude().toString()))
                    .concat(longitude.concat(geoCityDto.getLongitude().toString()))
                    .concat(appId);
            String bodyOfResponse = HttpRequestResponseUtil.getBodyOfResponse(urlWeatherFull);
            WeatherCityDto weatherCityDto = objectMapper.readValue(bodyOfResponse, new TypeReference<>() {
            });
            weatherCityDto.setCityInformation(geoCityDto);
            SetMainWeatherUtil.setMainWeather(weatherCityDto);
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
            WeatherCityDto weatherCityDto = objectMapper.readValue(bodyOfResponse, new TypeReference<>() {
            });
            GeoCityDto cityInformation = new GeoCityDto(location.getName(),
                    location.getLatitude(), location.getLongitude(),
                    weatherCityDto.getSolarCycle().getCountry()
            );
            weatherCityDto.setCityInformation(cityInformation);
            SetMainWeatherUtil.setMainWeather(weatherCityDto);
            setFavoriteToWeather(locationList, weatherCityDto);
            weatherCityDtoList.add(weatherCityDto);
        }
        return weatherCityDtoList;
    }

    private void setFavoriteToWeather(List<Location> locationList, WeatherCityDto weather) {
        locationList
                .forEach(l -> {
                    if (l.getLatitude().equals(weather.getCityInformation().getLatitude()) && l.getLongitude().equals(weather.getCityInformation().getLongitude())) {
                        weather.setFavorite(true);
                    }
                });
    }
}