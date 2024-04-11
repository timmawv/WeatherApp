package avlyakulov.timur.servlet;

import avlyakulov.timur.dao.LocationDao;
import avlyakulov.timur.dao.SessionDao;
import avlyakulov.timur.dao.api.UrlBuilder;
import avlyakulov.timur.dto.LocationDto;
import avlyakulov.timur.dto.UserDto;
import avlyakulov.timur.dto.WeatherCityDto;
import avlyakulov.timur.model.Location;
import avlyakulov.timur.model.Session;
import avlyakulov.timur.service.LocationService;
import avlyakulov.timur.service.SessionService;
import avlyakulov.timur.dao.api.OpenGeoService;
import avlyakulov.timur.dao.api.OpenWeatherService;
import avlyakulov.timur.servlet.util.HttpRequestResponse;
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

    private final HttpRequestResponse httpRequestResponse = new HttpRequestResponse();

    private final UrlBuilder urlBuilder = new UrlBuilder();


    private LocationService locationService;

    private OpenWeatherService openWeatherService;

    private final String htmlPageLogged = "pages/main-page-logged";

    @Override
    public void init() throws ServletException {
        locationService = new LocationService(new LocationDao());
        openWeatherService = new OpenWeatherService(new OpenGeoService(httpRequestResponse, urlBuilder),
                locationService, httpRequestResponse, urlBuilder);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        UserDto userLogin = (UserDto) req.getAttribute("userLogin");
        context.setVariable("login", userLogin);
        try {
            List<WeatherCityDto> weatherList = openWeatherService.getWeatherByUserLocations(userLogin.getUserId());
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
        UserDto userLogin = (UserDto) req.getAttribute("userLogin");
        LocationDto locationDto = new LocationDto(new BigDecimal(latitude), new BigDecimal(longitude), userLogin.getUserId());
        locationService.deleteLocationByCoordinate(locationDto);
        resp.sendRedirect("/WeatherApp-1.0/weather");
    }
}