package avlyakulov.timur.servlet;

import avlyakulov.timur.dao.SessionDao;
import avlyakulov.timur.dao.api.OpenForecastService;
import avlyakulov.timur.dao.api.UrlBuilder;
import avlyakulov.timur.dto.ForecastDto;
import avlyakulov.timur.dto.LocationDto;
import avlyakulov.timur.dto.UserDto;
import avlyakulov.timur.dto.api.*;
import avlyakulov.timur.model.Session;
import avlyakulov.timur.service.SessionService;
import avlyakulov.timur.servlet.util.HttpRequestResponse;
import avlyakulov.timur.util.CookieUtil;
import avlyakulov.timur.util.authentication.UserSessionCheck;
import avlyakulov.timur.util.thymeleaf.ThymeleafUtilRespondHtmlView;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(urlPatterns = "/forecast")
public class ForecastServlet extends HttpServlet {

    private final SessionService sessionService = new SessionService(new SessionDao());

    private final String htmlPageForecast = "pages/forecast";

    private final OpenForecastService openForecastService = new OpenForecastService(new UrlBuilder(), new HttpRequestResponse());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        BigDecimal latitude = new BigDecimal(req.getParameter("latitude"));
        BigDecimal longitude = new BigDecimal(req.getParameter("longitude"));
        String cityInfo = req.getParameter("city");
        LocationDto locationDto = new LocationDto(latitude, longitude, cityInfo);
        boolean hasUserValidSession = UserSessionCheck.hasUserValidSession(sessionService, resp, req.getCookies());
        if (hasUserValidSession) {
            String sessionIdFromCookie = CookieUtil.getSessionIdFromCookie(req.getCookies());
            Session session = sessionService.getUserSessionIfItNotExpired(sessionIdFromCookie);
            UserDto userDto = sessionService.getUserDtoByHisSession(session);
            context.setVariable("login", userDto);
        }
        ForecastDto forecastWeeklyHourly = openForecastService.getForecastWeeklyHourly(locationDto);

        //hourly
        List<String> temp = forecastWeeklyHourly.getForecastHourly().stream().map(ForecastHourlyInfo::getTemp).toList();
        List<String> time = forecastWeeklyHourly.getForecastHourly().stream().map(ForecastHourlyInfo::getTime).toList();
        List<String> mainWeather = forecastWeeklyHourly.getForecastHourly().stream().map(ForecastHourlyInfo::getWeather).map(Weather::getMain).toList();
        List<String> descriptionWeather = forecastWeeklyHourly.getForecastHourly().stream().map(ForecastHourlyInfo::getWeather).map(Weather::getDescription).toList();
        context.setVariable("forecasts", forecastWeeklyHourly);
        context.setVariable("temp", temp);
        context.setVariable("time", time);
        context.setVariable("mainWeather", mainWeather);
        context.setVariable("descriptions", descriptionWeather);

        //weekly
        List<String> tempWeekly = forecastWeeklyHourly.getForecastWeekly().stream().map(ForecastWeeklyInfo::getTemp).map(TemperatureWeekly::getDay).toList();
        List<String> timeWeekly = forecastWeeklyHourly.getForecastWeekly().stream().map(ForecastWeeklyInfo::getDay).toList();
        List<String> mainWeatherWeekly = forecastWeeklyHourly.getForecastWeekly().stream().map(ForecastWeeklyInfo::getWeather).map(Weather::getMain).toList();
        List<String> summaryWeatherWeekly = forecastWeeklyHourly.getForecastWeekly().stream().map(ForecastWeeklyInfo::getSummary).map(sum -> sum.replace(", ", ",")).toList();
        context.setVariable("tempWeekly", tempWeekly);
        context.setVariable("timeWeekly", timeWeekly);
        context.setVariable("mainWeatherWeekly", mainWeatherWeekly);
        context.setVariable("summaryWeatherWeekly", summaryWeatherWeekly);
        ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageForecast, context, resp);
    }
}