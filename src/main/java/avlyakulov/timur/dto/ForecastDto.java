package avlyakulov.timur.dto;

import avlyakulov.timur.dto.api.ForecastHourlyInfo;
import avlyakulov.timur.dto.api.ForecastWeeklyInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForecastDto {

    @JsonIgnore
    private String cityInfo;

    //1 в hourly всегда приходит текущий час допустим 15 50 то будет 15 00
    private List<ForecastHourlyInfo> forecastHourly = new ArrayList<>();

    @JsonProperty("daily")
    private List<ForecastWeeklyInfo> forecastWeekly;

    @JsonProperty(value = "hourly")
    public void setWeather(List<ForecastHourlyInfo> forecastHourlyReq) {
        for (int i = 0; i <= 24; i += 3) {
            this.forecastHourly.add(forecastHourlyReq.get(i));
        }
    }
}