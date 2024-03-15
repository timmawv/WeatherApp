package avlyakulov.timur.servlet;

import avlyakulov.timur.dto.UserDto;
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
import java.util.Optional;

@WebServlet(urlPatterns = "/weather")
public class MainPageLoggedController extends HttpServlet {

    private final SessionService sessionService = new SessionService();

    private final String htmlPageLogged = "pages/main-page-logged";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        Optional<String> sessionIdFromCookie = CookieUtil.getSessionIdFromCookie(req.getCookies());
        if (sessionIdFromCookie.isPresent()) {
            UserDto userLogin = sessionService.getUserByHisSession(sessionIdFromCookie.get());
            context.setVariable("login", userLogin);
        }
        ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageLogged, context, resp);
    }
}