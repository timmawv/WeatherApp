package avlyakulov.timur.dto.api;

import avlyakulov.timur.util.api.converter.TimeMilliDayConverter;
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
public class ForecastWeeklyInfo {

    @JsonProperty("dt")
    @JsonDeserialize(converter = TimeMilliDayConverter.class)
    private String day;

    @JsonProperty
    private TemperatureWeekly temp;

    private Weather weather;

    @JsonProperty
    private String summary;

    @JsonProperty(value = "weather")
    public void setWeather(List<Weather> weatherList) {
        this.weather = weatherList.get(0);
    }

    public void setMainWeather(String mainWeather) {
        this.weather.setMain(mainWeather);
    }
}