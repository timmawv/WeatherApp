package avlyakulov.timur.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GeoCityDto {

    private double latitude;

    private double longitude;

    private String country;

    private String state;
}