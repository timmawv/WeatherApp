package avlyakulov.timur.util.api.converter;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TemperatureRoundConverter extends StdConverter<String, String> {

    @Override
    public String convert(String temperature) {
        BigDecimal temperatureDecimal = new BigDecimal(temperature);
        temperatureDecimal = temperatureDecimal.setScale(0, RoundingMode.HALF_UP);
        return temperatureDecimal.toString();
    }
}