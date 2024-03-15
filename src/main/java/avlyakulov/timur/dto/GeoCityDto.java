package avlyakulov.timur.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GeoCityDto {

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String country;

    private String state;

    private String city;

    public GeoCityDto(BigDecimal latitude, BigDecimal longitude, String country, String city) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
    }
}