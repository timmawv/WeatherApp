package avlyakulov.timur.servlet;

import avlyakulov.timur.custom_exception.CookieNotExistException;
import avlyakulov.timur.custom_exception.JsonParseException;
import avlyakulov.timur.custom_exception.ModelAlreadyExistsException;
import avlyakulov.timur.custom_exception.TooManyLocationsException;
import avlyakulov.timur.dto.GeoCityDto;
import avlyakulov.timur.dto.LocationDto;
import avlyakulov.timur.dto.UserDto;
import avlyakulov.timur.dto.WeatherCityDto;
import avlyakulov.timur.model.Location;
import avlyakulov.timur.model.User;
import avlyakulov.timur.service.LocationService;
import avlyakulov.timur.service.SessionService;
import avlyakulov.timur.service.api.OpenWeatherService;
import avlyakulov.timur.util.CookieUtil;
import avlyakulov.timur.util.thymeleaf.ThymeleafUtilRespondHtmlView;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = "/weather/search")
public class WeatherSearchServlet extends HttpServlet {

    private final OpenWeatherService openWeatherService = new OpenWeatherService();

    private final SessionService sessionService = new SessionService();

    private final LocationService locationService = new LocationService();

    private final String htmlPageWeather = "pages/weather";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        UserDto userLogin;
        try {
            String sessionIdFromCookie = CookieUtil.getSessionIdFromCookie(req.getCookies());
            userLogin = sessionService.getUserDtoByHisSession(sessionIdFromCookie);
            context.setVariable("login", userLogin);
        } catch (CookieNotExistException e) {
            resp.sendRedirect("/WeatherApp-1.0/main-page");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String currentTime = LocalDateTime.now().format(formatter);

        String cityName = req.getParameter("city");
//        try {
//            List<WeatherCityDto> weatherList = openWeatherService.getWeatherList(cityName);
//            context.setVariable("weatherList", weatherList);
//        } catch (URISyntaxException | InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        GeoCityDto mashivkaGeoCityDto = new GeoCityDto(new BigDecimal("49.443"), new BigDecimal("34.867"), "Ukraine", "Poltava Oblast", "Mashivka");
        GeoCityDto karlovkaGeoCityDto = new GeoCityDto(new BigDecimal("49.457"), new BigDecimal("35.130"), "Ukraine", "Poltava Oblast", "Karlovka");
        WeatherCityDto mashivkaCityDto = new WeatherCityDto(
                "Clouds", "Broken clouds", "https://openweathermap.org/img/wn/10d@2x.png",
                "2°C", "-2°C", "4°C", "0°C", "87%", "10 km", "3 m/s",
                currentTime,
                "05:33", "19:23", mashivkaGeoCityDto, false
        );

        WeatherCityDto karlovkaCityDto = new WeatherCityDto(
                "Clouds", "Broken clouds", "https://openweathermap.org/img/wn/10d@2x.png",
                "2°C", "-2°C", "4°C", "0°C", "87%", "10 km", "3 m/s",
                currentTime,
                "05:33", "19:23", karlovkaGeoCityDto, true
        );
        context.setVariable("weatherList", List.of(mashivkaCityDto, karlovkaCityDto, karlovkaCityDto));
        ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageWeather, context, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Location locationFromRequestJsonFile = getLocationFromRequestJsonFile(req);
            locationService.createLocation(locationFromRequestJsonFile);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (JsonParseException | TooManyLocationsException | ModelAlreadyExistsException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = resp.getWriter();
            out.println(e.getMessage());
            out.close();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            LocationDto location = getLocationFromRequestJsonFileForDelete(req);
            locationService.deleteLocationByCoordinate(location);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (JsonParseException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            PrintWriter out = resp.getWriter();
            out.println(e.getMessage());
            out.close();
        }
    }

    private JsonNode readJsonFileFromRequest(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(sb.toString());
    }

    private Location getLocationFromRequestJsonFile(HttpServletRequest req) throws IOException {
        JsonNode jsonNode = readJsonFileFromRequest(req);
        if (jsonNode.has("latitude") && jsonNode.has("longitude") && jsonNode.has("cityName") && jsonNode.has("userId")) {
            return new Location(
                    jsonNode.get("cityName").asText(),
                    new BigDecimal(jsonNode.get("latitude").asText()),
                    new BigDecimal(jsonNode.get("longitude").asText()),
                    new User(jsonNode.get("userId").asInt())
            );
        } else {
            throw new JsonParseException("One or more fields didn't parse");
        }
    }

    private LocationDto getLocationFromRequestJsonFileForDelete(HttpServletRequest req) throws IOException {
        JsonNode jsonNode = readJsonFileFromRequest(req);
        if (jsonNode.has("latitude") && jsonNode.has("longitude")) {
            return new LocationDto(
                    new BigDecimal(jsonNode.get("latitude").asText()),
                    new BigDecimal(jsonNode.get("longitude").asText())
            );
        } else {
            throw new JsonParseException("One or more fields didn't parse");
        }
    }
}