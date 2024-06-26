package avlyakulov.timur.filter;

import avlyakulov.timur.custom_exception.CookieNotExistException;
import avlyakulov.timur.custom_exception.SessionNotValidException;
import avlyakulov.timur.dao.SessionDao;
import avlyakulov.timur.dto.UserDto;
import avlyakulov.timur.model.Session;
import avlyakulov.timur.service.SessionService;
import avlyakulov.timur.util.CookieUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/weather/*")
public class CookieSessionFilter implements Filter {

    private SessionService sessionService;



    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        sessionService = new SessionService(new SessionDao());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        try {
            String sessionIdFromCookie = CookieUtil.getSessionIdFromCookie(req.getCookies());
            Session userSession = sessionService.getUserSessionIfItNotExpired(sessionIdFromCookie);
            UserDto userLogin = sessionService.getUserDtoByHisSession(userSession);
            req.setAttribute("userLogin", userLogin);
            filterChain.doFilter(req, resp);
        } catch (CookieNotExistException e) {
            log.warn("User doesn't have active cookie");
            Cookie cookie = new Cookie("cookie_session_error", "");
            cookie.setMaxAge(1);
            resp.addCookie(cookie);
            resp.sendRedirect("/WeatherApp-1.0/login");
        } catch (SessionNotValidException e) {
            log.warn("User's session was expired");
            CookieUtil.deleteSessionIdCookie(resp);
            Cookie cookie = new Cookie("cookie_session_error", "");
            cookie.setMaxAge(1);
            resp.addCookie(cookie);
            resp.sendRedirect("/WeatherApp-1.0/login");
        }
    }
}