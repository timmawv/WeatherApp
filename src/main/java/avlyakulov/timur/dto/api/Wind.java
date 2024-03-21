package avlyakulov.timur.dto.api;

import avlyakulov.timur.util.api.converter.WindDegreeConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Wind {

    private String speed;

    @JsonProperty(value = "deg")
    @JsonDeserialize(converter = WindDegreeConverter.class)
    private String windDirection;

    @JsonProperty
    public void setSpeed(String speed) {
        this.speed = speed.concat(" m/s");
    }
}