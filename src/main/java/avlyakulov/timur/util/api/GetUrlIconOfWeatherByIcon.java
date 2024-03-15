package avlyakulov.timur.util.api;

public class GetUrlIconOfWeatherByIcon {


    public static String getUrlWeatherIcon(String iconCode) {
        final String urlIconWeather = "https://openweathermap.org/img/wn/";
        String icon = iconCode.concat("@2x.png");
        return urlIconWeather.concat(icon);
    }
}