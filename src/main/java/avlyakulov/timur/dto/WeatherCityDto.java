package avlyakulov.timur.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

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

    private String sunriseWeather;

    private String sunsetWeather;

    private GeoCityDto geoCityDto;

    private boolean favorite;

    public WeatherCityDto(GeoCityDto geoCityDto) {
        this.geoCityDto = geoCityDto;
    }

    public String getCountry() {
        return geoCityDto.getCountry();
    }

    public String getState() {
        return geoCityDto.getState();
    }

    public String getCity() {
        return geoCityDto.getCityName();
    }

    public BigDecimal getLatitude() {
        return new BigDecimal(geoCityDto.getLatitude());
    }

    public BigDecimal getLongitude() {
        return new BigDecimal(geoCityDto.getLongitude());
    }
}