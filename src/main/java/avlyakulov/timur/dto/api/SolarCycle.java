package avlyakulov.timur.dto.api;

import avlyakulov.timur.util.api.converter.CountryCodeConverter;
import avlyakulov.timur.util.api.converter.TimeMilliConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SolarCycle {

    @JsonDeserialize(converter = CountryCodeConverter.class)
    private String country;

    @JsonDeserialize(converter = TimeMilliConverter.class)
    private String sunrise;

    @JsonDeserialize(converter = TimeMilliConverter.class)
    private String sunset;
}
