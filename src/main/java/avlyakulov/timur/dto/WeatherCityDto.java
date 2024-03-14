package avlyakulov.timur.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeatherCityDto {

    private String mainWeather;

    private String detailedWeather;

    private String urlIconWeather;

    private String temperatureWeather;

    private String minTemperatureWeather;

    private String maxTemperatureWeather;

    private String feelsLikeWeather;

    private String humidityWeather;

    private String visibilityKilometersWeather;

    private String windSpeedWeather;

    private String currentTimeWeather;

    private String sunriseWeather;

    private String sunsetWeather;

    private String country;

    private String state;

    public WeatherCityDto(String country, String state) {
        this.country = country;
        this.state = state;
    }
}