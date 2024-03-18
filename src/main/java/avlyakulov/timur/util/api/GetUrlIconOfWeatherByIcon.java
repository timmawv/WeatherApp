package avlyakulov.timur.util.api;

import java.util.HashMap;
import java.util.Map;

public class GetUrlIconOfWeatherByIcon {
    private Map<String, String> iconUrls = new HashMap<>(
            Map.of(
                    "01d", "https://github.com/timmawv/WeatherApp/blob/main/img/animated/day.svg?raw=true",
                    "01n", "https://github.com/timmawv/WeatherApp/blob/main/img/animated/night.svg?raw=true",

                    "02d", "https://github.com/timmawv/WeatherApp/blob/main/img/animated/cloudy-day-1.svg?raw=true",
                    "02n", "https://github.com/timmawv/WeatherApp/blob/main/img/animated/cloudy-night-1.svg?raw=true",

                    "03d", "https://github.com/timmawv/WeatherApp/blob/main/img/animated/cloudy.svg?raw=true",
                    "03n", "https://github.com/timmawv/WeatherApp/blob/main/img/animated/cloudy.svg?raw=true",

                    "04d", "https://github.com/timmawv/WeatherApp/blob/main/img/animated/cloudy.svg?raw=true",
                    "04n", "https://github.com/timmawv/WeatherApp/blob/main/img/animated/cloudy.svg?raw=true",
            )

    );
    public static String getUrlWeatherIcon(String iconCode) {
        final String urlIconWeather = "https://openweathermap.org/img/wn/";
        String icon = iconCode.concat("@2x.png");
        return urlIconWeather.concat(icon);
    }
}