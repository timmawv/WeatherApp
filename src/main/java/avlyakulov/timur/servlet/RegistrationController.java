package avlyakulov.timur.servlet;

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

@WebServlet(urlPatterns = "/registration")
public class RegistrationController extends HttpServlet {
    private final String htmlPageRegister = "auth/register";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageRegister, context, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirm_password");
        if (LoginRegistrationValidation.isFieldEmpty(context, username, password, confirmPassword)) {
            ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageRegister, context, resp);
        } else {
            if (LoginRegistrationValidation.isPasswordTheSameAndStrong(password, confirmPassword, context)) {
                //todo add service to register user
            } else {
                ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageRegister, context, resp);
            }
        }
    }
}