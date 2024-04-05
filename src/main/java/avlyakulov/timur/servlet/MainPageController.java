package avlyakulov.timur.servlet;

import avlyakulov.timur.custom_exception.CookieNotExistException;
import avlyakulov.timur.custom_exception.SessionNotValidException;
import avlyakulov.timur.dao.SessionDao;
import avlyakulov.timur.service.SessionService;
import avlyakulov.timur.util.CookieUtil;
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
        //todo вынести в отдельный метод весь этот процес проверки cookie session
        try {
            String sessionIdFromCookie = CookieUtil.getSessionIdFromCookie(req.getCookies());
            if (sessionService.isUserSessionValid(sessionIdFromCookie)) {
                resp.sendRedirect("/WeatherApp-1.0/weather");
            } else {
                CookieUtil.deleteSessionIdCookie(resp);
                ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageMain, context, resp);
            }
        } catch (SessionNotValidException e) {
            CookieUtil.deleteSessionIdCookie(resp);
            ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageMain, context, resp);
        } catch (CookieNotExistException e) {
            ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageMain, context, resp);
        }
    }
}