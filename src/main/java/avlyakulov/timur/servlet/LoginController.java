package avlyakulov.timur.servlet;

import avlyakulov.timur.custom_exception.UserNotFoundException;
import avlyakulov.timur.service.UserService;
import avlyakulov.timur.util.ContextUtil;
import avlyakulov.timur.util.authentication.LoginRegistrationValidation;
import avlyakulov.timur.util.thymeleaf.ThymeleafUtil;
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
    private final String htmlPageLogin = "auth/login";

    private final UserService userService = new UserService();

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
        if (LoginRegistrationValidation.isFieldEmpty(context, login, password)) {
            ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageLogin, context, resp);
        } else {
            try {
                userService.logUserByCredentials(login, password);
            } catch (UserNotFoundException e) {
                ContextUtil.setErrorToContext(context, e.getMessage());
                ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageLogin, context, resp);
                return;
            }
            resp.sendRedirect("/WeatherApp-1.0/main-page");
        }
    }
}