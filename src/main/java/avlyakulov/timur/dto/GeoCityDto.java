package avlyakulov.timur.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class GeoCityDto {

    @JsonProperty(value = "name")
    private String cityName;

    @JsonProperty(value = "lat")
    private BigDecimal latitude;

    @JsonProperty(value = "lon")
    private BigDecimal longitude;

    private String country;

    private String state;

    public GeoCityDto(String cityName, BigDecimal latitude, BigDecimal longitude, String country) {
        this.cityName = cityName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
    }
}