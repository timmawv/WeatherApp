package avlyakulov.timur.dto.api;

import avlyakulov.timur.util.api.converter.TemperatureRoundConverter;
import avlyakulov.timur.util.api.converter.TemperatureSingConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TemperatureWeekly {

    @JsonDeserialize(converter = TemperatureRoundConverter.class)
    private String day;

    @JsonDeserialize(converter = TemperatureSingConverter.class)
    private String tempMax;

    @JsonDeserialize(converter = TemperatureSingConverter.class)
    private String tempMin;
}