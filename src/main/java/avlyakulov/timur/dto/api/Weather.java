package avlyakulov.timur.dto.api;

import avlyakulov.timur.util.api.converter.IconUrlConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {

    private String main;

    private String description;

    @JsonDeserialize(converter = IconUrlConverter.class)
    private String icon;
}