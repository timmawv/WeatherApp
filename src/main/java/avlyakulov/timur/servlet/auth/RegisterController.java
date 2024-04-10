package avlyakulov.timur.servlet.auth;

import avlyakulov.timur.custom_exception.ModelAlreadyExistsException;
import avlyakulov.timur.dao.SessionDao;
import avlyakulov.timur.dao.UserDao;
import avlyakulov.timur.model.User;
import avlyakulov.timur.service.SessionService;
import avlyakulov.timur.service.UserService;
import avlyakulov.timur.util.ContextUtil;
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

@WebServlet(urlPatterns = "/register")
public class RegisterController extends HttpServlet {

    private UserService userService;

    private SessionService sessionService;

    private final String htmlPageRegister = "auth/register";

    @Override
    public void init() throws ServletException {
        userService = new UserService(new UserDao());
        sessionService = new SessionService(new SessionDao());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        context.setVariable("success_registration", false);
        boolean hasUserValidSession = UserSessionCheck.hasUserValidSession(sessionService, resp, req.getCookies());
        if (hasUserValidSession) {
            resp.sendRedirect("/WeatherApp-1.0/weather");
        }
        ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageRegister, context, resp);
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