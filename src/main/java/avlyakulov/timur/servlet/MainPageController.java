package avlyakulov.timur.servlet;

import avlyakulov.timur.util.CookieUtil;
import avlyakulov.timur.util.thymeleaf.ThymeleafUtilRespondHtmlView;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = "/main-page")
public class MainPageController extends HttpServlet {
    private final String htmlPageMain = "pages/main-page";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        Optional<String> cookieId = CookieUtil.getSessionIdFromCookie(req.getCookies());
        if (cookieId.isPresent()) {
            resp.sendRedirect("/WeatherApp-1.0/weather/main-page");
        } else {
            ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageMain, context, resp);
        }
    }
}