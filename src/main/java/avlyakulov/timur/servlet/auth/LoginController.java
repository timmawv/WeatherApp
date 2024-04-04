package avlyakulov.timur.servlet.auth;

import avlyakulov.timur.custom_exception.CookieNotExistException;
import avlyakulov.timur.custom_exception.ModelNotFoundException;
import avlyakulov.timur.custom_exception.SessionNotValidException;
import avlyakulov.timur.dao.UserDao;
import avlyakulov.timur.model.Session;
import avlyakulov.timur.model.User;
import avlyakulov.timur.service.SessionService;
import avlyakulov.timur.service.UserService;
import avlyakulov.timur.util.ContextUtil;
import avlyakulov.timur.util.CookieUtil;
import avlyakulov.timur.util.authentication.LoginRegistrationValidation;
import avlyakulov.timur.util.thymeleaf.ThymeleafUtilRespondHtmlView;
import jakarta.persistence.NoResultException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@WebServlet(urlPatterns = "/login")
public class LoginController extends HttpServlet {

    private UserService userService;

    private SessionService sessionService;

    private final String htmlPageLogin = "auth/login";

    private final String SESSION_EXPIRE_MESSAGE = "Your session was expired. You need to authorize again";

    @Override
    public void init() throws ServletException {
        userService = new UserService(new UserDao());
        sessionService = new SessionService();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        Optional<String> sessionExpireCookie = CookieUtil.getSessionExpireCookie(req.getCookies());
        if (sessionExpireCookie.isPresent()) {
            context.setVariable("error_field", SESSION_EXPIRE_MESSAGE);
            ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageLogin, context, resp);
        } else {
            try {
                String sessionIdFromCookie = CookieUtil.getSessionIdFromCookie(req.getCookies());
                if (sessionService.isUserSessionValid(sessionIdFromCookie)) {
                    resp.sendRedirect("/WeatherApp-1.0/weather");
                } else {
                    CookieUtil.deleteSessionIdCookie(resp);
                    ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageLogin, context, resp);
                }
            } catch (SessionNotValidException e) {
                CookieUtil.deleteSessionIdCookie(resp);
                ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageLogin, context, resp);
            } catch (CookieNotExistException e) {
                ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageLogin, context, resp);
            }
        }
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
            sessionService.deleteSessionByUserId(user.getId());
            Session session = sessionService.createSession(user);
            CookieUtil.createCookie(session.getId().toString(), resp);
            resp.sendRedirect("/WeatherApp-1.0/weather");
        }
    }
}