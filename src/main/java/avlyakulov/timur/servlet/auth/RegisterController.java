package avlyakulov.timur.servlet.auth;

import avlyakulov.timur.custom_exception.ModelAlreadyExistsException;
import avlyakulov.timur.dao.SessionDao;
import avlyakulov.timur.dao.UserDao;
import avlyakulov.timur.dto.UserRegistrationDto;
import avlyakulov.timur.mapper.UserMapper;
import avlyakulov.timur.model.User;
import avlyakulov.timur.service.SessionService;
import avlyakulov.timur.service.UserService;
import avlyakulov.timur.util.ContextUtil;
import avlyakulov.timur.util.authentication.CheckAnyEmptyField;
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

@WebServlet(urlPatterns = "/registration")
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
        if (CheckAnyEmptyField.isAnyFieldNullOrEmpty(login, password, confirmPassword)) {
            context.setVariable("error_field", "One or more fields are empty. Please avoid empty fields");
            ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageRegister, context, resp);
            return;
        }
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto(login, password, confirmPassword);
        boolean isUserLoginAndPasswordAreValid = userService.isUserLoginAndPasswordAreValid(context, userRegistrationDto);
        if (isUserLoginAndPasswordAreValid) {
            User user = new User(userRegistrationDto.getLogin(), userRegistrationDto.getPassword());
            try {
                userService.createUser(user);
            } catch (ModelAlreadyExistsException e) {
                context.setVariable("error_field", e.getMessage());
                ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageRegister, context, resp);
                return;
            }
        }
        context.setVariable("success_registration", true);
        ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageRegister, context, resp);
    }
}