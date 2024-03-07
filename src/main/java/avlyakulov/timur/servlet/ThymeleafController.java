package avlyakulov.timur.servlet;

import avlyakulov.timur.util.thymeleaf.ThymeleafUtil;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.Context;

import java.io.IOException;

@WebServlet(urlPatterns = "/main-page")
public class ThymeleafController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext servletContext = req.getServletContext();
        String htmlPage = ThymeleafUtil.getHtmlPage("main-page", servletContext, new Context());

        resp.getWriter().write(htmlPage);
    }
}