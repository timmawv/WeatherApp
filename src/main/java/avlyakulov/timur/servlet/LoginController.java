package avlyakulov.timur.servlet;

import avlyakulov.timur.custom_exception.ModelNotFoundException;
import avlyakulov.timur.model.Session;
import avlyakulov.timur.model.User;
import avlyakulov.timur.service.SessionService;
import avlyakulov.timur.service.UserService;
import avlyakulov.timur.util.ContextUtil;
import avlyakulov.timur.util.authentication.LoginRegistrationValidation;
import avlyakulov.timur.util.thymeleaf.ThymeleafUtilRespondHtmlView;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@WebServlet(urlPatterns = "/login")
public class LoginController extends HttpServlet {
    private final String htmlPageLogin = "auth/login";

    private final UserService userService = new UserService();

    private final SessionService sessionService = new SessionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageLogin, context, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        User user;
        if (LoginRegistrationValidation.isFieldEmpty(context, login, password)) {
            ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageLogin, context, resp);
        } else {
            try {
                user = userService.logUserByCredentials(login, password);
            } catch (ModelNotFoundException e) {
                ContextUtil.setErrorToContext(context, e.getMessage());
                ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageLogin, context, resp);
                return;
            }
            Session session = new Session(UUID.randomUUID(), user);
            sessionService.createSession(session);
            Cookie cookie = new Cookie("session_id", session.getId().toString());
            cookie.setMaxAge(30 * 60);
            resp.addCookie(cookie);
            resp.sendRedirect("/WeatherApp-1.0/weather/main-page");
        }
    }
}