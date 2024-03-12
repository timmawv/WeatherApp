package avlyakulov.timur.servlet;

import avlyakulov.timur.model.Session;
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
import java.util.UUID;

@WebServlet(urlPatterns = "/weather/main-page")
public class MainPageLoggedController extends HttpServlet {

    private final SessionService sessionService = new SessionService();

    private final String htmlPageLogged = "pages/main-page-logged";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        Optional<String> sessionId = CookieUtil.getSessionIdFromCookie(req.getCookies());
        if (sessionId.isPresent()) {
            Session userSession = sessionService.getSessionById(UUID.fromString(sessionId.get()));
            context.setVariable("login", userSession.getUser().getLogin());
            ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageLogged, context, resp);
        } else {
            resp.sendRedirect("/WeatherApp-1.0/main-page");
        }
    }
}