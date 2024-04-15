package avlyakulov.timur.util.api;

import avlyakulov.timur.dto.WeatherCityDto;
import avlyakulov.timur.dto.api.ForecastHourlyInfo;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SetMainWeatherUtil {

    public static void setMainWeather(WeatherCityDto weather) {
        String[] temp = weather.getTemperature().getTemp().split("°C");
        BigDecimal temperature = new BigDecimal(temp[0]);
        temperature = temperature.setScale(0, RoundingMode.HALF_UP);
        weather.setMainWeather(weather.getWeather().getMain()
                .concat(", ".concat(temperature.toString()).concat("°C")));
    }

    public static void setMainWeather(ForecastHourlyInfo forecastHourlyInfo) {
        String[] temp = forecastHourlyInfo.getTemp().split("°C");
        BigDecimal temperature = new BigDecimal(temp[0]);
        temperature = temperature.setScale(0, RoundingMode.HALF_UP);
        forecastHourlyInfo.setMainWeather(forecastHourlyInfo.getWeather().getMain()
                .concat(", ".concat(temperature.toString()).concat("°C")));
    }
}