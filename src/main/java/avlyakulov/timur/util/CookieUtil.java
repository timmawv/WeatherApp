package avlyakulov.timur.util;

import avlyakulov.timur.custom_exception.CookieNotExistException;
import avlyakulov.timur.model.User;
import avlyakulov.timur.service.SessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public class CookieUtil {

    public static String getSessionIdFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            throw new CookieNotExistException("Cookie doesn't exist");
        } else {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("session_id")) {
                    return cookie.getValue();
                }
            }
        }
        throw new CookieNotExistException("Cookie doesn't exist");
    }

    public static void deleteSessionIdCookie(HttpServletResponse resp) {
        Cookie cookie = new Cookie("session_id", "");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
    }
}