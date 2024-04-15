package avlyakulov.timur.util.api.converter;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class TimeMilliDayConverter extends StdConverter<String, String> {

    @Override
    public String convert(String timeMilli) {
        long time = Long.parseLong(timeMilli);

        LocalDateTime dateTime = Instant.ofEpochSecond(time)
                .atZone(ZoneId.of("Europe/Kiev")).toLocalDateTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM");
        return dateTime.format(formatter);
    }
}