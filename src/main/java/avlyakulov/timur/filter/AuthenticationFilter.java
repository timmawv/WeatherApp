package avlyakulov.timur.filter;

import avlyakulov.timur.util.CookieUtil;
import avlyakulov.timur.util.thymeleaf.ThymeleafUtilRespondHtmlView;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@WebFilter(urlPatterns = "/weather/*")
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpServletRequest req = (HttpServletRequest) servletRequest;

        Optional<String> cookieId = CookieUtil.getSessionIdFromCookie(req.getCookies());
        if (cookieId.isPresent()) {
            log.warn("Unauthorized request");
            resp.sendRedirect("/WeatherApp-1.0/weather");
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}