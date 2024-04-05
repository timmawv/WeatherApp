package avlyakulov.timur.servlet;

import avlyakulov.timur.custom_exception.GlobalApiException;
import avlyakulov.timur.custom_exception.JsonParseException;
import avlyakulov.timur.custom_exception.ModelAlreadyExistsException;
import avlyakulov.timur.custom_exception.TooManyLocationsException;
import avlyakulov.timur.dao.LocationDao;
import avlyakulov.timur.dao.SessionDao;
import avlyakulov.timur.dto.LocationDto;
import avlyakulov.timur.dto.UserDto;
import avlyakulov.timur.dto.WeatherCityDto;
import avlyakulov.timur.model.Session;
import avlyakulov.timur.service.LocationService;
import avlyakulov.timur.service.SessionService;
import avlyakulov.timur.service.api.OpenGeoService;
import avlyakulov.timur.service.api.OpenWeatherService;
import avlyakulov.timur.servlet.util.HttpRequestResponseUtil;
import avlyakulov.timur.util.CookieUtil;
import avlyakulov.timur.util.HttpRequestJsonReader;
import avlyakulov.timur.util.authentication.LoginRegistrationValidation;
import avlyakulov.timur.util.thymeleaf.ThymeleafUtilRespondHtmlView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.List;

@WebServlet(urlPatterns = "/weather/search")
public class WeatherSearchServlet extends HttpServlet {

    private final HttpRequestResponseUtil httpRequestResponseUtil = new HttpRequestResponseUtil();

    private OpenWeatherService openWeatherService;

    private SessionService sessionService;

    private LocationService locationService;

    private final String htmlPageWeather = "pages/weather";

    private final ObjectMapper objectMapper = new ObjectMapper();

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
        String sessionIdFromCookie = CookieUtil.getSessionIdFromCookie(req.getCookies());
        Session userSession = sessionService.getUserSessionIfItNotExpired(sessionIdFromCookie);
        UserDto userLogin = sessionService.getUserDtoByHisSession(userSession);
        context.setVariable("login", userLogin);
        String cityName = req.getParameter("city");
        if (LoginRegistrationValidation.isCityNameValid(cityName, context)) {
            try {
                List<WeatherCityDto> weatherList = openWeatherService.getWeatherListFromCityName(cityName, userLogin);
                context.setVariable("weatherList", weatherList);
                ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageWeather, context, resp);
            } catch (URISyntaxException | InterruptedException | GlobalApiException e) {
                context.setVariable("error_city_name", e.getMessage());
                ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageWeather, context, resp);
            }
        } else {
            ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageWeather, context, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String locationJson = HttpRequestJsonReader.readJsonFileFromRequest(req);
            LocationDto locationDto = objectMapper.readValue(locationJson, new TypeReference<>() {
            });
            locationService.createLocation(locationDto);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (JsonParseException | TooManyLocationsException | ModelAlreadyExistsException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            out.print(objectMapper.writeValueAsString(e.getMessage()));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String locationJson = HttpRequestJsonReader.readJsonFileFromRequest(req);
            LocationDto location = objectMapper.readValue(locationJson, new TypeReference<>() {
            });
            locationService.deleteLocationByCoordinate(location);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (JsonParseException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = resp.getWriter();
            out.println(e.getMessage());
            out.close();
        }
    }
}