package avlyakulov.timur.servlet;

import avlyakulov.timur.custom_exception.*;
import avlyakulov.timur.dao.LocationDao;
import avlyakulov.timur.dao.SessionDao;
import avlyakulov.timur.dao.api.UrlBuilder;
import avlyakulov.timur.dto.LocationDto;
import avlyakulov.timur.dto.UserDto;
import avlyakulov.timur.dto.WeatherCityDto;
import avlyakulov.timur.model.Session;
import avlyakulov.timur.service.LocationService;
import avlyakulov.timur.service.SessionService;
import avlyakulov.timur.dao.api.OpenGeoService;
import avlyakulov.timur.dao.api.OpenWeatherService;
import avlyakulov.timur.servlet.util.HttpRequestResponse;
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

    private final String htmlPageWeather = "pages/weather";

    private final HttpRequestResponse httpRequestResponse = new HttpRequestResponse();

    private final UrlBuilder urlBuilder = new UrlBuilder();

    private OpenWeatherService openWeatherService;


    private LocationService locationService;


    private final ObjectMapper objectMapper = new ObjectMapper();

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
        String cityName = req.getParameter("city");
        if (LoginRegistrationValidation.isCityNameValid(cityName, context)) {
            try {
                List<WeatherCityDto> weatherList = openWeatherService.getWeatherListFromCityNameLoggedUser(cityName, userLogin);
                context.setVariable("weatherList", weatherList);
                context.setVariable("cityName", cityName);
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
            UserDto userLogin = (UserDto) req.getAttribute("userLogin");
            locationDto.setUserId(userLogin.getUserId());
            locationService.createLocation(locationDto);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (JsonParseException | TooManyLocationsException | CookieNotExistException | SessionNotValidException |
                 ModelAlreadyExistsException e) {
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
            LocationDto locationDto = objectMapper.readValue(locationJson, new TypeReference<>() {
            });
            UserDto userLogin = (UserDto) req.getAttribute("userLogin");
            locationDto.setUserId(userLogin.getUserId());
            locationService.deleteLocationByCoordinate(locationDto);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (JsonParseException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = resp.getWriter();
            out.println(e.getMessage());
            out.close();
        }
    }
}