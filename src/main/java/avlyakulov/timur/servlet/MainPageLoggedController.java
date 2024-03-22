package avlyakulov.timur.servlet;

import avlyakulov.timur.dto.LocationDto;
import avlyakulov.timur.dto.UserDto;
import avlyakulov.timur.dto.WeatherCityDto;
import avlyakulov.timur.model.Location;
import avlyakulov.timur.model.Session;
import avlyakulov.timur.service.LocationService;
import avlyakulov.timur.service.SessionService;
import avlyakulov.timur.service.api.OpenWeatherService;
import avlyakulov.timur.util.CookieUtil;
import avlyakulov.timur.util.thymeleaf.ThymeleafUtilRespondHtmlView;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;

@WebServlet(urlPatterns = "/weather")
public class MainPageLoggedController extends HttpServlet {

    private final SessionService sessionService = new SessionService();

    private final LocationService locationService = new LocationService();

    private final String htmlPageLogged = "pages/main-page-logged";

    private final OpenWeatherService openWeatherService = new OpenWeatherService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        String sessionIdFromCookie = CookieUtil.getSessionIdFromCookie(req.getCookies());
        Session userSession = sessionService.getUserSessionIfItNotExpired(sessionIdFromCookie);
        UserDto userLogin = sessionService.getUserDtoByHisSession(userSession);
        context.setVariable("login", userLogin);
        try {
            List<Location> locationList = locationService.getAllLocationByUserId(userLogin.getUserId());
            List<WeatherCityDto> weatherList = openWeatherService.getWeatherByUserLocations(locationList);
            context.setVariable("weatherList", weatherList);
        } catch (URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageLogged, context, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String latitude = req.getParameter("latitude");
        String longitude = req.getParameter("longitude");
        LocationDto location = new LocationDto(new BigDecimal(latitude), new BigDecimal(longitude));
        locationService.deleteLocationByCoordinate(location);
        resp.sendRedirect("/WeatherApp-1.0/weather");
    }
}