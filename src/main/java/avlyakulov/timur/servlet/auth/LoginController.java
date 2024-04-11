package avlyakulov.timur.servlet.auth;

import avlyakulov.timur.custom_exception.UserCredentialsException;
import avlyakulov.timur.dao.SessionDao;
import avlyakulov.timur.dao.UserDao;
import avlyakulov.timur.model.User;
import avlyakulov.timur.service.SessionService;
import avlyakulov.timur.service.UserService;
import avlyakulov.timur.util.ContextUtil;
import avlyakulov.timur.util.CookieUtil;
import avlyakulov.timur.util.authentication.CheckAnyEmptyField;
import avlyakulov.timur.util.authentication.LoginRegistrationValidation;
import avlyakulov.timur.util.authentication.UserSessionCheck;
import avlyakulov.timur.util.thymeleaf.ThymeleafUtilRespondHtmlView;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = "/login")
public class LoginController extends HttpServlet {

    private UserService userService;

    private SessionService sessionService;

    private final String htmlPageLogin = "auth/login";

    private final String SESSION_EXPIRE_MESSAGE = "Your session was expired. You need to authorize again";

    @Override
    public void init() throws ServletException {
        userService = new UserService(new UserDao());
        sessionService = new SessionService(new SessionDao());
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        Optional<String> cookieSessionError = CookieUtil.getCookieSessionError(req.getCookies());
        if (cookieSessionError.isPresent()) {
            context.setVariable("error_field", SESSION_EXPIRE_MESSAGE);
        } else {
            boolean hasUserValidSession = UserSessionCheck.hasUserValidSession(sessionService, resp, req.getCookies());
            if (hasUserValidSession) {
                resp.sendRedirect("/WeatherApp-1.0/weather");
            }
        }
        ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageLogin, context, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        User user;
        if (CheckAnyEmptyField.isAnyFieldNullOrEmpty(login, password)) {
            context.setVariable("error_field", "One or more fields are empty. Please avoid empty fields");
            ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageLogin, context, resp);
            return;
        }
        try {
            user = userService.getUserByLoginAndPassword(login, password);
        } catch (UserCredentialsException e) {
            ContextUtil.setErrorToContext(context, e.getMessage());
            ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageLogin, context, resp);
            return;
        }
        sessionService.deleteSessionByUserId(user.getId());
        String sessionId = sessionService.createSession(user);
        CookieUtil.createCookie(sessionId, resp);
        resp.sendRedirect("/WeatherApp-1.0/weather");
    }
}