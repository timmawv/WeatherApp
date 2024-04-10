package avlyakulov.timur.dao.api;

public class UrlBuilder {

    private final String apiWeatherKey = "&appid=".concat(System.getenv("API_WEATHER_KEY"));

    public String buildUrlWithParameters(String url, String... params) {
        return String.format(url, params).concat(apiWeatherKey);
    }
}