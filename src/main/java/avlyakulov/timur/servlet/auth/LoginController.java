package avlyakulov.timur.servlet.auth;

import avlyakulov.timur.custom_exception.UserCredentialsException;
import avlyakulov.timur.dao.SessionDao;
import avlyakulov.timur.dao.UserDao;
import avlyakulov.timur.model.User;
import avlyakulov.timur.service.SessionService;
import avlyakulov.timur.service.UserService;
import avlyakulov.timur.util.ContextUtil;
import avlyakulov.timur.util.CookieUtil;
import avlyakulov.timur.util.authentication.LoginRegistrationValidation;
import avlyakulov.timur.util.authentication.UserSessionCheck;
import avlyakulov.timur.util.thymeleaf.ThymeleafUtilRespondHtmlView;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.Context;

import java.io.IOException;

@WebServlet(urlPatterns = "/login")
public class LoginController extends HttpServlet {

    private UserService userService;

    private SessionService sessionService;

    private final String htmlPageLogin = "auth/login";

    @Override
    public void init() throws ServletException {
        userService = new UserService(new UserDao());
        sessionService = new SessionService(new SessionDao());
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        String cookieSessionErrorMessage = req.getAttribute("cookie_session_error").toString();
        if (cookieSessionErrorMessage != null) {
            context.setVariable("error_field", cookieSessionErrorMessage);
            ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageLogin, context, resp);
        } else {
            boolean hasUserValidSession = UserSessionCheck.hasUserValidSession(sessionService, resp, req.getCookies());
            if (hasUserValidSession) {
                resp.sendRedirect("/WeatherApp-1.0/weather");
            }
            ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageLogin, context, resp);
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
}