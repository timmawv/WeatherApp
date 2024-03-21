package avlyakulov.timur.util.api.converter;

import com.fasterxml.jackson.databind.util.StdConverter;

public class TemperatureSingConverter extends StdConverter<String, String> {
    @Override
    public String convert(String temperature) {
        return temperature.concat("°C");
    }
}
