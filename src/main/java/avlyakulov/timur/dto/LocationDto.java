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
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationDto {

    @JsonProperty(value = "cityName")
    private String name;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private Integer userId;

    public LocationDto(BigDecimal latitude, BigDecimal longitude, Integer userId) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.userId = userId;
    }
}