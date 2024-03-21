package avlyakulov.timur.util.api.converter;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.util.HashMap;
import java.util.Map;

public class IconUrlConverter extends StdConverter<String, String> {

    private static Map<String, String> iconUrls = new HashMap<>(Map.of(
            "01d", "https://github.com/timmawv/WeatherApp/blob/main/img/animated/day.svg?raw=true",
            "01n", "https://github.com/timmawv/WeatherApp/blob/main/img/animated/night.svg?raw=true",

            "02d", "https://github.com/timmawv/WeatherApp/blob/main/img/animated/cloudy-day-1.svg?raw=true",
            "02n", "https://github.com/timmawv/WeatherApp/blob/main/img/animated/cloudy-night-1.svg?raw=true",

            "03d", "https://github.com/timmawv/WeatherApp/blob/main/img/animated/cloudy_scattered.svg?raw=true",
            "03n", "https://github.com/timmawv/WeatherApp/blob/main/img/animated/cloudy_scattered.svg?raw=true",

            "04d", "https://github.com/timmawv/WeatherApp/blob/main/img/animated/overcast-day.svg?raw=true",
            "04n", "https://github.com/timmawv/WeatherApp/blob/main/img/animated/overcast-night.svg?raw=true",

            "09d", "https://github.com/timmawv/WeatherApp/blob/main/img/animated/rainy-6.svg?raw=true",
            "09n", "https://github.com/timmawv/WeatherApp/blob/main/img/animated/rainy-6.svg?raw=true"
    ));

    static {
        iconUrls.putAll(Map.of(
                "10d", "https://github.com/timmawv/WeatherApp/blob/main/img/animated/rainy-3.svg?raw=true",
                "10n", "https://github.com/timmawv/WeatherApp/blob/main/img/animated/rainy-5.svg?raw=true",

                "11d", "https://github.com/timmawv/WeatherApp/blob/main/img/animated/thunder.svg?raw=true",
                "11n", "https://github.com/timmawv/WeatherApp/blob/main/img/animated/thunder.svg?raw=true",

                "13d", "https://github.com/timmawv/WeatherApp/blob/main/img/animated/snowy-1.svg?raw=true",
                "13n", "https://github.com/timmawv/WeatherApp/blob/main/img/animated/snowy-5.svg?raw=true",

                "50d", "https://github.com/timmawv/WeatherApp/blob/main/img/animated/partly-cloudy-day-fog.svg?raw=true",
                "50n", "https://github.com/timmawv/WeatherApp/blob/main/img/animated/partly-cloudy-night-fog.svg?raw=true"
        ));
    }

    @Override
    public String convert(String iconCode) {
        return iconUrls.get(iconCode);
    }
}
