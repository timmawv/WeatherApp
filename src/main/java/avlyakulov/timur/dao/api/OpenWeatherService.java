package avlyakulov.timur.dao.api;

import avlyakulov.timur.custom_exception.GlobalApiException;
import avlyakulov.timur.custom_exception.ModelNotFoundException;
import avlyakulov.timur.custom_exception.ServerErrorException;
import avlyakulov.timur.dto.api.GeoCityDto;
import avlyakulov.timur.dto.UserDto;
import avlyakulov.timur.dto.WeatherCityDto;
import avlyakulov.timur.model.Location;
import avlyakulov.timur.service.LocationService;
import avlyakulov.timur.servlet.util.HttpRequestResponse;
import avlyakulov.timur.util.api.SetMainWeatherUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class OpenWeatherService {

    private final String urlWeather = "https://api.openweathermap.org/data/2.5/weather?units=metric&lat=%s&lon=%s";

    private final OpenGeoService openGeoService;

    private final LocationService locationService;

    private final HttpRequestResponse httpRequestResponse;

    private final UrlBuilder urlBuilder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public OpenWeatherService(OpenGeoService openGeoService, LocationService locationService, HttpRequestResponse httpRequestResponse, UrlBuilder urlBuilder) {
        this.openGeoService = openGeoService;
        this.locationService = locationService;
        this.httpRequestResponse = httpRequestResponse;
        this.urlBuilder = urlBuilder;
    }

    public List<WeatherCityDto> getWeatherListFromCityNameNoLoggedUser(String cityName) throws URISyntaxException, IOException, InterruptedException {
        List<WeatherCityDto> weatherCityDtoList = new ArrayList<>();
        try {
            List<GeoCityDto> cityCoordinateByName = openGeoService.getCitiesDtoByName(cityName);
            for (GeoCityDto geoCityDto : cityCoordinateByName) {
                String urlWeatherFull = getFullUrlWeather(geoCityDto);
                String bodyOfResponse = httpRequestResponse.getBodyOfResponse(urlWeatherFull);
                WeatherCityDto weatherCityDto = objectMapper.readValue(bodyOfResponse, new TypeReference<>() {
                });
                weatherCityDto.setCityInformation(geoCityDto);
                SetMainWeatherUtil.setMainWeather(weatherCityDto);
                weatherCityDtoList.add(weatherCityDto);
            }
            return weatherCityDtoList;
        } catch (ModelNotFoundException | ServerErrorException e) {
            throw new GlobalApiException(e.getMessage());
        }
    }

    public List<WeatherCityDto> getWeatherListFromCityNameLoggedUser(String cityName, UserDto userDto) throws URISyntaxException, IOException, InterruptedException {
        List<Location> locationList = locationService.getAllLocationByUserId(userDto.getUserId());
        List<WeatherCityDto> weatherCityDtoList = new ArrayList<>();
        try {
            List<GeoCityDto> cityCoordinateByName = openGeoService.getCitiesDtoByName(cityName);
            for (GeoCityDto geoCityDto : cityCoordinateByName) {
                String urlWeatherFull = getFullUrlWeather(geoCityDto);
                String bodyOfResponse = httpRequestResponse.getBodyOfResponse(urlWeatherFull);
                WeatherCityDto weatherCityDto = objectMapper.readValue(bodyOfResponse, new TypeReference<>() {
                });
                weatherCityDto.setCityInformation(geoCityDto);
                SetMainWeatherUtil.setMainWeather(weatherCityDto);
                setFavoriteToWeather(locationList, weatherCityDto);
                weatherCityDtoList.add(weatherCityDto);
            }
            return weatherCityDtoList;
        } catch (ModelNotFoundException | ServerErrorException e) {
            throw new GlobalApiException(e.getMessage());
        }
    }

    public List<WeatherCityDto> getWeatherByUserLocations(Integer userId) throws URISyntaxException, IOException, InterruptedException {
        List<WeatherCityDto> weatherCityDtoList = new ArrayList<>();
        List<Location> locationList = locationService.getAllLocationByUserId(userId);
        for (Location location : locationList) {
            String urlWeatherFull = urlBuilder.buildUrlWithParameters(urlWeather, location.getLatitude().toString(), location.getLongitude().toString());
            String bodyOfResponse = httpRequestResponse.getBodyOfResponse(urlWeatherFull);
            WeatherCityDto weatherCityDto = objectMapper.readValue(bodyOfResponse, new TypeReference<>() {
            });
            GeoCityDto cityInformation = new GeoCityDto(
                    location.getLatitude(), location.getLongitude(),
                    weatherCityDto.getSolarCycle().getCountry(),
                    location.getName()
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

    private String getFullUrlWeather(GeoCityDto geoCityDto) {
        return urlBuilder.buildUrlWithParameters(
                urlWeather,
                geoCityDto.getLatitude().toString(),
                geoCityDto.getLongitude().toString()
        );
    }
}