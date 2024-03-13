package avlyakulov.timur.filter;

import avlyakulov.timur.service.SessionService;
import avlyakulov.timur.util.CookieUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@WebFilter(urlPatterns = "/weather/*")
public class AuthenticationFilter implements Filter {

    private final SessionService sessionService = new SessionService();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpServletRequest req = (HttpServletRequest) servletRequest;

        Optional<String> sessionId = CookieUtil.getSessionIdFromCookie(req.getCookies());
        if (sessionId.isPresent()) {
            if (sessionService.isUserSessionExpired(UUID.fromString(sessionId.get()))) {
                log.warn("Unauthorized request");
                CookieUtil.deleteSessionIdCookie(resp);
                resp.sendRedirect("/WeatherApp-1.0/main-page");
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        } else {
            log.warn("Unauthorized request");
            resp.sendRedirect("/WeatherApp-1.0/main-page");
        }
    }
}