package avlyakulov.timur.util.authentication;

import avlyakulov.timur.custom_exception.CookieNotExistException;
import avlyakulov.timur.service.SessionService;
import avlyakulov.timur.util.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class UserSessionCheck {

    public static boolean hasUserValidSession(SessionService sessionService, HttpServletResponse resp, Cookie[] cookies) throws IOException {
        try {
            String sessionIdFromCookie = CookieUtil.getSessionIdFromCookie(cookies);
            if (sessionService.isUserSessionValid(sessionIdFromCookie)) {
                return true;
            }
            CookieUtil.deleteSessionIdCookie(resp);
            sessionService.deleteSessionById(sessionIdFromCookie);
            return false;
        } catch (CookieNotExistException e) {
            return false;
        }
    }
}