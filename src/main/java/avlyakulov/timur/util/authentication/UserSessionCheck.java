package avlyakulov.timur.util.authentication;

import avlyakulov.timur.custom_exception.CookieNotExistException;
import avlyakulov.timur.custom_exception.SessionNotValidException;
import avlyakulov.timur.service.SessionService;
import avlyakulov.timur.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class UserSessionCheck {

    public static void validateUserSession(SessionService sessionService, HttpServletResponse resp, Cookie[] cookies) throws IOException {
        try {
            String sessionIdFromCookie = CookieUtil.getSessionIdFromCookie(cookies);
            if (sessionService.isUserSessionValid(sessionIdFromCookie)) {
                resp.sendRedirect("/WeatherApp-1.0/weather");
            } else {
                CookieUtil.deleteSessionIdCookie(resp);
                sessionService.deleteSessionById(sessionIdFromCookie);
            }
        } catch (CookieNotExistException ignored) {

        } catch (SessionNotValidException e) {
            CookieUtil.deleteSessionIdCookie(resp);
        }
    }
}
