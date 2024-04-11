package avlyakulov.timur.servlet;

import avlyakulov.timur.custom_exception.GlobalApiException;
import avlyakulov.timur.dao.LocationDao;
import avlyakulov.timur.dao.SessionDao;
import avlyakulov.timur.dao.api.UrlBuilder;
import avlyakulov.timur.dto.WeatherCityDto;
import avlyakulov.timur.service.LocationService;
import avlyakulov.timur.service.SessionService;
import avlyakulov.timur.dao.api.OpenGeoService;
import avlyakulov.timur.dao.api.OpenWeatherService;
import avlyakulov.timur.servlet.util.HttpRequestResponse;
import avlyakulov.timur.util.authentication.UserSessionCheck;
import avlyakulov.timur.util.thymeleaf.ThymeleafUtilRespondHtmlView;
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

    private final HttpRequestResponse httpRequestResponse = new HttpRequestResponse();

    private final UrlBuilder urlBuilder = new UrlBuilder();

    private OpenWeatherService openWeatherService;

    private SessionService sessionService;

    private LocationService locationService;


    @Override
    public void init() throws ServletException {
        locationService = new LocationService(new LocationDao());
        sessionService = new SessionService(new SessionDao());
        openWeatherService = new OpenWeatherService(new OpenGeoService(httpRequestResponse, urlBuilder),
                locationService, httpRequestResponse, urlBuilder);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        boolean hasUserValidSession = UserSessionCheck.hasUserValidSession(sessionService, resp, req.getCookies());
        if (hasUserValidSession) {
            resp.sendRedirect("/WeatherApp-1.0/weather");
        }
        String cityName = req.getParameter("city");
        printPage(cityName, context, resp);
    }

    private void printPage(String cityName, Context context, HttpServletResponse resp) throws IOException {
        try {
            List<WeatherCityDto> weatherList = openWeatherService.getWeatherListFromCityNameNoLoggedUser(cityName);
            context.setVariable("weatherList", weatherList);
        } catch (URISyntaxException | InterruptedException | GlobalApiException e) {
            context.setVariable("error_city_name", e.getMessage());
        }
        ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageMainWeather, context, resp);
    }
}