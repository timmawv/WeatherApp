package avlyakulov.timur.util.api.converter;

import com.fasterxml.jackson.databind.util.StdConverter;

public class VisibilityConverter extends StdConverter<String, String> {
    @Override
    public String convert(String visibility) {
        double vis = Double.parseDouble(visibility);
        vis = vis / 1000;
        vis = Math.round(vis);
        return String.valueOf(vis).concat(" km");
    }
}