package avlyakulov.timur.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public class CookieUtil {

    public static Optional<String> getSessionIdFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            return Optional.empty();
        } else {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("session_id")) {
                    return Optional.of(cookie.getValue());
                }
            }
        }
        return Optional.empty();
    }

    public static void deleteSessionIdCookie(HttpServletResponse resp) {
        Cookie cookie = new Cookie("session_id", "");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
    }
}