package avlyakulov.timur.dto.api;

import avlyakulov.timur.util.api.converter.TemperatureSingConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Temperature {

    @JsonDeserialize(converter = TemperatureSingConverter.class)
    private String temp;

    @JsonProperty(value = "feels_like")
    @JsonDeserialize(converter = TemperatureSingConverter.class)
    private String feelsLike;

    @JsonProperty(value = "temp_min")
    @JsonDeserialize(converter = TemperatureSingConverter.class)
    private String temperatureMin;

    @JsonProperty(value = "temp_max")
    @JsonDeserialize(converter = TemperatureSingConverter.class)
    private String temperatureMax;

    private String humidity;

    @JsonProperty(value = "humidity")
    public void setHumidity(String humidity) {
        this.humidity = humidity.concat("%");
    }
}