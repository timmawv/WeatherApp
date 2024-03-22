package avlyakulov.timur.servlet.auth;

import avlyakulov.timur.service.SessionService;
import avlyakulov.timur.util.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/logout")
public class LogoutController extends HttpServlet {

    private final SessionService sessionService = new SessionService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionIdFromCookie = CookieUtil.getSessionIdFromCookie(req.getCookies());
        sessionService.deleteSessionById(sessionIdFromCookie);
        CookieUtil.deleteSessionIdCookie(resp);
        resp.sendRedirect("/WeatherApp-1.0/main-page");
    }
}