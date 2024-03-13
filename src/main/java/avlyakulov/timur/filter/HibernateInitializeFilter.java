package avlyakulov.timur.filter;

import avlyakulov.timur.util.HibernateSingletonUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/*")
public class HibernateInitializeFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        HibernateSingletonUtil.initSessionFactory();
        log.info("HibernateInitializeFilter filter was created and initialize Hibernate connection");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);
    }


    @Override
    public void destroy() {
        HibernateSingletonUtil.closeSessionFactory();
        log.info("HibernateInitializeFilter filter was destroyed and closed Hibernate connection");
    }
}