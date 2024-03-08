package avlyakulov.timur.servlet;

import avlyakulov.timur.util.authentication.LoginRegistrationValidation;
import avlyakulov.timur.util.thymeleaf.ThymeleafUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.Context;

import java.io.IOException;

@WebServlet(urlPatterns = "/login")
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        respondHtmlPage(context, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        if (LoginRegistrationValidation.isFieldEmpty(context, username, password)) {
            respondHtmlPage(context, resp);
        } else {
            //todo make login
        }
    }

    private void respondHtmlPage(Context context, HttpServletResponse resp) throws IOException {
        final String htmlPageRegister = "login";
        resp.getWriter().write(ThymeleafUtil.getHtmlPage(htmlPageRegister, context));
    }
}
