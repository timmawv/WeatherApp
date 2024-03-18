package avlyakulov.timur.servlet.auth;

import avlyakulov.timur.custom_exception.ModelAlreadyExistsException;
import avlyakulov.timur.model.User;
import avlyakulov.timur.service.UserService;
import avlyakulov.timur.util.BCryptUtil;
import avlyakulov.timur.util.ContextUtil;
import avlyakulov.timur.util.authentication.LoginRegistrationValidation;
import avlyakulov.timur.util.thymeleaf.ThymeleafUtilRespondHtmlView;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.Context;

import java.io.IOException;

@WebServlet(urlPatterns = "/registration")
public class RegisterController extends HttpServlet {

    private final UserService userService = new UserService();
    private final String htmlPageRegister = "auth/register";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageRegister, context, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirm_password");
        if (LoginRegistrationValidation.isFieldEmpty(context, login, password, confirmPassword)) {
            ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageRegister, context, resp);
        } else {
            if (LoginRegistrationValidation.isPasswordTheSameAndStrong(password, confirmPassword, context)) {
                User user = new User(login, BCryptUtil.encryptPassword(password));
                try {
                    userService.createUser(user);
                } catch (ModelAlreadyExistsException e) {
                    ContextUtil.setErrorToContext(context, e.getMessage());
                    ThymeleafUtilRespondHtmlView.respondHtmlPage("auth/register", context, resp);
                    return;
                }
                resp.sendRedirect("/WeatherApp-1.0/main-page");
            } else {
                ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageRegister, context, resp);
            }
        }
    }
}