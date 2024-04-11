package avlyakulov.timur.dto.api;

import avlyakulov.timur.util.api.converter.CountryCodeConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoCityDto {

    @JsonProperty(value = "lat")
    private BigDecimal latitude;

    @JsonProperty(value = "lon")
    private BigDecimal longitude;

    @JsonDeserialize(converter = CountryCodeConverter.class)
    private String country;

    private String state;

    @JsonProperty(value = "name")
    private String cityName;

    public GeoCityDto(BigDecimal latitude, BigDecimal longitude, String country, String cityName) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.cityName = cityName;
    }
}