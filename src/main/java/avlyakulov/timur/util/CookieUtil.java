package avlyakulov.timur.util;

import jakarta.servlet.http.Cookie;

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
}
