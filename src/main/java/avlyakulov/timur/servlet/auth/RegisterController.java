package avlyakulov.timur.servlet.auth;

import avlyakulov.timur.custom_exception.CookieNotExistException;
import avlyakulov.timur.custom_exception.ModelAlreadyExistsException;
import avlyakulov.timur.model.User;
import avlyakulov.timur.service.SessionService;
import avlyakulov.timur.service.UserService;
import avlyakulov.timur.util.BCryptUtil;
import avlyakulov.timur.util.ContextUtil;
import avlyakulov.timur.util.CookieUtil;
import avlyakulov.timur.util.authentication.LoginRegistrationValidation;
import avlyakulov.timur.util.thymeleaf.ThymeleafUtilRespondHtmlView;
import jakarta.persistence.NoResultException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.Context;

import java.io.IOException;

@WebServlet(urlPatterns = "/register")
public class RegisterController extends HttpServlet {

    private final UserService userService = new UserService();

    private final SessionService sessionService = new SessionService();
    private final String htmlPageRegister = "auth/register";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        context.setVariable("success_registration", false);
        try {
            String sessionIdFromCookie = CookieUtil.getSessionIdFromCookie(req.getCookies());
            try {
                if (sessionService.isUserSessionValid(sessionIdFromCookie)) {
                    resp.sendRedirect("/WeatherApp-1.0/weather");
                } else {
                    CookieUtil.deleteSessionIdCookie(resp);
                    ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageRegister, context, resp);
                }
            } catch (NoResultException e) {
                CookieUtil.deleteSessionIdCookie(resp);
                ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageRegister, context, resp);
            }
        } catch (CookieNotExistException e) {
            ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageRegister, context, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirm_password");
        context.setVariable("success_registration", false);
        if (LoginRegistrationValidation.isFieldEmpty(context, login, password, confirmPassword)) {
            ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageRegister, context, resp);
        } else {
            if (LoginRegistrationValidation.isUserLoginValid(login, context)
                    && LoginRegistrationValidation.isPasswordTheSameAndStrong(password, confirmPassword, context)) {
                User user = new User(login, password);
                try {
                    userService.createUser(user);
                } catch (ModelAlreadyExistsException e) {
                    ContextUtil.setErrorToContext(context, e.getMessage());
                    ThymeleafUtilRespondHtmlView.respondHtmlPage("auth/register", context, resp);
                    return;
                }
                context.setVariable("success_registration", true);
                ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageRegister, context, resp);
            } else {
                ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageRegister, context, resp);
            }
        }
    }
}