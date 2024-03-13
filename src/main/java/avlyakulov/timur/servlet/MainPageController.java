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
import java.util.UUID;

@WebServlet(urlPatterns = "/main-page")
public class MainPageController extends HttpServlet {
    private final String htmlPageMain = "pages/main-page";

    private final SessionService sessionService = new SessionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        Optional<String> sessionId = CookieUtil.getSessionIdFromCookie(req.getCookies());
        if (sessionId.isPresent()) {
            if (!sessionService.isUserSessionExpired(UUID.fromString(sessionId.get())))
                resp.sendRedirect("/WeatherApp-1.0/weather");
            else {
                CookieUtil.deleteSessionIdCookie(resp);
                ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageMain, context, resp);
            }
        } else {
            ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageMain, context, resp);
        }
    }
}