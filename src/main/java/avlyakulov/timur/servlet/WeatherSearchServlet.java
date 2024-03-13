package avlyakulov.timur.servlet;

import avlyakulov.timur.service.SessionService;
import avlyakulov.timur.util.CookieUtil;
import avlyakulov.timur.util.thymeleaf.ThymeleafUtilRespondHtmlView;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = "/weather/search")
public class WeatherSearchServlet extends HttpServlet {

    private SessionService sessionService = new SessionService();

    private final String htmlPageWeather = "pages/weather";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String city = req.getParameter("city");

        Context context = new Context();
        Optional<String> sessionIdFromCookie = CookieUtil.getSessionIdFromCookie(req.getCookies());
        if (sessionIdFromCookie.isPresent()) {
            String userLogin = sessionService.getUserLoginByHisSession(sessionIdFromCookie.get());
            context.setVariable("login", userLogin);
        }
        ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageWeather, context, resp);
    }
}