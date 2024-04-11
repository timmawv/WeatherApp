package avlyakulov.timur.servlet;

import avlyakulov.timur.dao.SessionDao;
import avlyakulov.timur.service.SessionService;
import avlyakulov.timur.util.authentication.UserSessionCheck;
import avlyakulov.timur.util.thymeleaf.ThymeleafUtilRespondHtmlView;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.Context;

import java.io.IOException;

@WebServlet(urlPatterns = "/main-page")
public class MainPageController extends HttpServlet {

    private final String htmlPageMain = "pages/main-page";

    private final SessionService sessionService = new SessionService(new SessionDao());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        boolean hasUserValidSession = UserSessionCheck.hasUserValidSession(sessionService, resp, req.getCookies());
        if (hasUserValidSession) {
            resp.sendRedirect("/WeatherApp-1.0/weather");
        }
        ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageMain, context, resp);
    }
}