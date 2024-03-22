package avlyakulov.timur.servlet;

import avlyakulov.timur.custom_exception.JsonParseException;
import avlyakulov.timur.custom_exception.ModelAlreadyExistsException;
import avlyakulov.timur.custom_exception.TooManyLocationsException;
import avlyakulov.timur.dto.LocationDto;
import avlyakulov.timur.dto.UserDto;
import avlyakulov.timur.dto.WeatherCityDto;
import avlyakulov.timur.model.Session;
import avlyakulov.timur.service.LocationService;
import avlyakulov.timur.service.SessionService;
import avlyakulov.timur.service.api.OpenWeatherService;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = "/weather/search")
public class WeatherSearchServlet extends HttpServlet {

    private final OpenWeatherService openWeatherService = new OpenWeatherService();

    private final SessionService sessionService = new SessionService();

    private final LocationService locationService = new LocationService();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String htmlPageWeather = "pages/weather";

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
                List<WeatherCityDto> weatherList = openWeatherService.getWeatherListFromHttpRequest(cityName, userLogin);
                context.setVariable("weatherList", weatherList);
                ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageWeather, context, resp);
            } catch (URISyntaxException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            //todo get rid of this creating List
            List<WeatherCityDto> weatherList = new ArrayList<>();
            context.setVariable("weatherList", weatherList);
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