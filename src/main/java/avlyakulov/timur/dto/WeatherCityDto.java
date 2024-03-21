package avlyakulov.timur.dto;

import avlyakulov.timur.dto.api.SolarCycle;
import avlyakulov.timur.dto.api.Temperature;
import avlyakulov.timur.dto.api.Weather;
import avlyakulov.timur.dto.api.Wind;
import avlyakulov.timur.util.api.converter.VisibilityConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherCityDto {

    private Weather weather;

    @JsonProperty(value = "main")
    private Temperature temperature;

    @JsonDeserialize(converter = VisibilityConverter.class)
    private String visibility;

    private Wind wind;

    @JsonProperty(value = "sys")
    private SolarCycle solarCycle;

    private GeoCityDto cityInformation;

    private boolean favorite;

    @JsonProperty(value = "weather")
    public void setWeather(List<Weather> weatherList) {
        this.weather = weatherList.get(0);
    }

    public void setMainWeather(String mainWeather) {
        this.weather.setMain(mainWeather);
    }
}