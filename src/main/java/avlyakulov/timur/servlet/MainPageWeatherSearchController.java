package avlyakulov.timur.servlet;

import avlyakulov.timur.custom_exception.CookieNotExistException;
import avlyakulov.timur.custom_exception.GlobalApiException;
import avlyakulov.timur.custom_exception.SessionNotValidException;
import avlyakulov.timur.dao.LocationDao;
import avlyakulov.timur.dao.SessionDao;
import avlyakulov.timur.dto.WeatherCityDto;
import avlyakulov.timur.service.LocationService;
import avlyakulov.timur.service.SessionService;
import avlyakulov.timur.service.api.OpenGeoService;
import avlyakulov.timur.service.api.OpenWeatherService;
import avlyakulov.timur.servlet.util.HttpRequestResponseUtil;
import avlyakulov.timur.util.CookieUtil;
import avlyakulov.timur.util.authentication.LoginRegistrationValidation;
import avlyakulov.timur.util.authentication.UserSessionCheck;
import avlyakulov.timur.util.thymeleaf.ThymeleafUtilRespondHtmlView;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@WebServlet(urlPatterns = "/main-page/search")
public class MainPageWeatherSearchController extends HttpServlet {

    private final String htmlPageMainWeather = "pages/main-page-weather";

    private final HttpRequestResponseUtil httpRequestResponseUtil = new HttpRequestResponseUtil();

    private OpenWeatherService openWeatherService;

    private SessionService sessionService;

    private LocationService locationService;


    @Override
    public void init() throws ServletException {
        locationService = new LocationService(new LocationDao());
        sessionService = new SessionService(new SessionDao());
        openWeatherService = new OpenWeatherService(new OpenGeoService(httpRequestResponseUtil),
                locationService, httpRequestResponseUtil);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        String cityName = req.getParameter("city");
        UserSessionCheck.validateUserSession(sessionService, resp, req.getCookies());
        printPage(cityName, context, resp);
    }

    private void printPage(String cityName, Context context, HttpServletResponse resp) throws IOException {
        if (LoginRegistrationValidation.isCityNameValid(cityName, context)) {
            try {
                List<WeatherCityDto> weatherList = openWeatherService.getWeatherListFromCityNameNoLoggedUser(cityName);
                context.setVariable("weatherList", weatherList);
                ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageMainWeather, context, resp);
            } catch (URISyntaxException | InterruptedException | GlobalApiException e) {
                context.setVariable("error_city_name", e.getMessage());
                ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageMainWeather, context, resp);
            }
        } else {
            ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageMainWeather, context, resp);
        }
    }
}