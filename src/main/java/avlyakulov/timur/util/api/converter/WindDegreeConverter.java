package avlyakulov.timur.util.api.converter;

import com.fasterxml.jackson.databind.util.StdConverter;

public class WindDegreeConverter extends StdConverter<Integer, String> {

    private static final String[] directions = {"North", "North-East", "East-North", "East", "East-South",
            "South-East", "South", "South-West", "West-South", "West", "West-North", "North-West"};

    @Override
    public String convert(Integer degree) {
        if (degree >= 345 && degree <= 360) {
            return directions[0];
        } else {
            int segmentSize = 360 / directions.length;
            int segmentIndex = (degree + segmentSize / 2) / segmentSize;
            return directions[segmentIndex];
        }
    }
}