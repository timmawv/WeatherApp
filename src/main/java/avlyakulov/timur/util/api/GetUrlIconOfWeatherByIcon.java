package avlyakulov.timur.util.api;

public class GetUrlIconOfWeatherByIcon {

    private static StringBuilder urlIconWeather = new StringBuilder("https://openweathermap.org/img/wn/");

    public static String getUrlWeatherIcon(String iconCode) {
        String icon = iconCode + "@2x.png";
        return urlIconWeather.append(icon).toString();
    }
}