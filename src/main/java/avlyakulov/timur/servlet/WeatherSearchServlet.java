package avlyakulov.timur.servlet;

import avlyakulov.timur.custom_exception.GlobalApiException;
import avlyakulov.timur.custom_exception.ModelAlreadyExistsException;
import avlyakulov.timur.custom_exception.TooManyLocationsException;
import avlyakulov.timur.dao.LocationDao;
import avlyakulov.timur.dao.api.OpenGeoService;
import avlyakulov.timur.dao.api.OpenWeatherService;
import avlyakulov.timur.dao.api.UrlBuilder;
import avlyakulov.timur.dto.UserDto;
import avlyakulov.timur.dto.WeatherCityDto;
import avlyakulov.timur.service.LocationService;
import avlyakulov.timur.servlet.util.HttpRequestResponse;
import avlyakulov.timur.util.HttpRequestJsonReader;
import avlyakulov.timur.util.thymeleaf.ThymeleafUtilRespondHtmlView;
import com.fasterxml.jackson.core.JsonParseException;
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
        try {
            List<WeatherCityDto> weatherList = openWeatherService.getWeatherListFromCityNameLoggedUser(cityName, userLogin);
            context.setVariable("weatherList", weatherList);
            context.setVariable("cityName", cityName);
        } catch (URISyntaxException | InterruptedException | GlobalApiException e) {
            context.setVariable("error_city_name", e.getMessage());
        }
        ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageWeather, context, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String locationJson = HttpRequestJsonReader.readJsonFileFromRequest(req);
            UserDto userLogin = (UserDto) req.getAttribute("userLogin");
            locationService.createLocationFromJsonFile(locationJson, userLogin.getUserId());
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
            UserDto userLogin = (UserDto) req.getAttribute("userLogin");
            locationService.deleteLocationFromJsonFile(locationJson, userLogin.getUserId());
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (JsonParseException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = resp.getWriter();
            out.println(e.getMessage());
            out.close();
        }
    }
}