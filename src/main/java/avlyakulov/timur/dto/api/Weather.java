package avlyakulov.timur.dto.api;

import avlyakulov.timur.util.api.converter.IconUrlConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {

    private String main;

    private String description;

    @JsonDeserialize(converter = IconUrlConverter.class)
    private String icon;
}