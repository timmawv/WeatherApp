package avlyakulov.timur.dao.api;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UrlBuilderTest {

    private final UrlBuilder urlBuilder = new UrlBuilder();

    private final String urlWeather = "https://api.openweathermap.org/data/2.5/weather?units=metric&lat=%s&lon=%s";

    private final String urlWeatherBuilt = "https://api.openweathermap.org/data/2.5/weather?units=metric&lat=50.20&lon=39.46&appid=dummy";

    private final String urlGeo = "http://api.openweathermap.org/geo/1.0/direct?limit=3&q=%s";

    private final String urlGeoBuilt = "http://api.openweathermap.org/geo/1.0/direct?limit=3&q=Mashivka&appid=dummy";

    @Test
    void buildUrlWithParameters_urlWeather() {
        String maybeFullWeatherUrl = urlBuilder.buildUrlWithParameters(urlWeather, "50.20", "39.46");

        assertThat(maybeFullWeatherUrl).isEqualTo(urlWeatherBuilt);
    }

    @Test
    void buildUrlWithParameters_urlGeo() {
        String maybeFullGeoUrl = urlBuilder.buildUrlWithParameters(urlGeo, "Mashivka");

        assertThat(maybeFullGeoUrl).isEqualTo(urlGeoBuilt);
    }
}