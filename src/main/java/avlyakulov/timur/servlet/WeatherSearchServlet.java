package avlyakulov.timur.servlet;

import avlyakulov.timur.dto.GeoCityDto;
import avlyakulov.timur.dto.UserDto;
import avlyakulov.timur.dto.WeatherCityDto;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = "/weather/search")
public class WeatherSearchServlet extends HttpServlet {

    private final OpenWeatherService openWeatherService = new OpenWeatherService();

    private final SessionService sessionService = new SessionService();

    private final String htmlPageWeather = "pages/weather";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        Optional<String> sessionIdFromCookie = CookieUtil.getSessionIdFromCookie(req.getCookies());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String currentTime = LocalDateTime.now().format(formatter);
        if (sessionIdFromCookie.isPresent()) {
            UserDto userLogin = sessionService.getUserByHisSession(sessionIdFromCookie.get());
            context.setVariable("login", userLogin);
        }
        String cityName = req.getParameter("city");
//        try {
//            List<WeatherCityDto> weatherList = openWeatherService.getWeatherList(cityName);
//            context.setVariable("weatherList", weatherList);
//        } catch (URISyntaxException | InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        GeoCityDto geoCityDto = new GeoCityDto(49.4441667, 34.87, "Ukraine", "Poltava Oblast", "Mashivka");
        WeatherCityDto weatherCityDto = new WeatherCityDto(
                "Clouds", "Broken clouds", "https://openweathermap.org/img/wn/10d@2x.png",
                "2°C", "-2°C", "4°C", "0°C", "87%", "10 km", "3 m/s",
                currentTime,
                "05:33", "19:23", geoCityDto
        );
        context.setVariable("weatherList", List.of(weatherCityDto, weatherCityDto, weatherCityDto, weatherCityDto, weatherCityDto));
        ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageWeather, context, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(sb.toString());
        System.out.println(jsonNode.toPrettyString());
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}