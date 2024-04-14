package avlyakulov.timur.servlet;

import avlyakulov.timur.dao.SessionDao;
import avlyakulov.timur.dto.LocationDto;
import avlyakulov.timur.dto.UserDto;
import avlyakulov.timur.model.Session;
import avlyakulov.timur.service.SessionService;
import avlyakulov.timur.util.CookieUtil;
import avlyakulov.timur.util.authentication.UserSessionCheck;
import avlyakulov.timur.util.thymeleaf.ThymeleafUtilRespondHtmlView;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(urlPatterns = "/forecast")
public class ForecastServlet extends HttpServlet {

    private final SessionService sessionService = new SessionService(new SessionDao());

    private final String htmlPageForecast = "pages/forecast";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        BigDecimal latitude = new BigDecimal(req.getParameter("latitude"));
        BigDecimal longitude = new BigDecimal(req.getParameter("longitude"));
        String cityInfo = req.getParameter("city");
        LocationDto locationDto = new LocationDto(latitude, longitude, cityInfo);
        boolean hasUserValidSession = UserSessionCheck.hasUserValidSession(sessionService, resp, req.getCookies());
        if (hasUserValidSession) {
            String sessionIdFromCookie = CookieUtil.getSessionIdFromCookie(req.getCookies());
            Session session = sessionService.getUserSessionIfItNotExpired(sessionIdFromCookie);
            UserDto userDto = sessionService.getUserDtoByHisSession(session);
            context.setVariable("login", userDto);
        }
        context.setVariable("location", locationDto);
        ThymeleafUtilRespondHtmlView.respondHtmlPage(htmlPageForecast, context, resp);
    }
}