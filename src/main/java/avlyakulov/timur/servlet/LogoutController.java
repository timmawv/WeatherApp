package avlyakulov.timur.servlet;

import avlyakulov.timur.service.SessionService;
import avlyakulov.timur.util.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@WebServlet(urlPatterns = "/logout")
public class LogoutController extends HttpServlet {

    private final SessionService sessionService = new SessionService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<String> sessionId = CookieUtil.getSessionIdFromCookie(req.getCookies());
        if (sessionId.isPresent()) {
            sessionService.deleteSessionById(UUID.fromString(sessionId.get()));
            CookieUtil.deleteSessionIdCookie(resp);
            resp.sendRedirect("/WeatherApp-1.0/main-page");
        } else {
            resp.sendRedirect("/WeatherApp-1.0/main-page");
        }
    }
}