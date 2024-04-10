package avlyakulov.timur.util;

import avlyakulov.timur.custom_exception.CookieNotExistException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

public class CookieUtil {

    public static void createCookie(String sessionId, HttpServletResponse resp) throws IOException {
        Cookie cookie = new Cookie("session_id", sessionId);
        cookie.setMaxAge(30 * 60);
        resp.addCookie(cookie);
    }

    public static Optional<String> getSessionExpireCookie(Cookie[] cookies) {
        if (cookies == null) {
            return Optional.empty();
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("unauthorized_request")) {
                return Optional.of(cookie.getValue());
            }
        }
        return Optional.empty();
    }

    public static String getSessionIdFromCookie(Cookie[] cookies) {
        if (cookies == null) {
            throw new CookieNotExistException("Cookie doesn't exist");
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("session_id")) {
                return cookie.getValue();
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