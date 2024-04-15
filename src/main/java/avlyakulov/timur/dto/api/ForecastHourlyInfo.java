package avlyakulov.timur.dto.api;

import avlyakulov.timur.util.api.converter.TemperatureRoundConverter;
import avlyakulov.timur.util.api.converter.TimeMilliConverter;
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
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForecastHourlyInfo {

    @JsonProperty("dt")
    @JsonDeserialize(converter = TimeMilliConverter.class)
    private String time;

    @JsonDeserialize(converter = TemperatureRoundConverter.class)
    private String temp;

    private Weather weather;

    @JsonProperty(value = "weather")
    public void setWeather(List<Weather> weatherList) {
        this.weather = weatherList.get(0);
    }

    public void setMainWeather(String mainWeather) {
        this.weather.setMain(mainWeather);
    }
}