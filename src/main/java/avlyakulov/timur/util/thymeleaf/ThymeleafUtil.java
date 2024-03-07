package avlyakulov.timur.util.thymeleaf;

import jakarta.servlet.ServletContext;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebApplication;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

public class ThymeleafUtil {

    private ThymeleafUtil() {
    }

    private static ITemplateResolver configureResolver(ServletContext servletContext) {
        IWebApplication application = JakartaServletWebApplication.buildApplication(servletContext);
        WebApplicationTemplateResolver resolver = new WebApplicationTemplateResolver(application);
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCacheable(true);
        resolver.setCacheTTLMs(3600000L);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setPrefix("/view/");
        resolver.setSuffix(".html");
        return resolver;
    }

    public static String getHtmlPage(String htmlPage, ServletContext servletContext, Context context) {
        TemplateEngine templateEngine = new TemplateEngine();
        ITemplateResolver iTemplateResolver = configureResolver(servletContext);
        templateEngine.setTemplateResolver(iTemplateResolver);
        return templateEngine.process(htmlPage, context);
    }

}
