package avlyakulov.timur.servlet;

import avlyakulov.timur.util.thymeleaf.ThymeleafUtilRespondHtmlView;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.Context;

import java.io.IOException;

@WebServlet(urlPatterns = "/main-page")
public class MainPageController extends HttpServlet {
    private final String htmlPageMain = "pages/main-page";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            System.out.println("cookie is null");
        } else {
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName() + cookie.getValue());
            }
        }
        ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageMain, context, resp);
    }
}