package avlyakulov.timur.util.thymeleaf;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class ThymeleafUtil {

    private ThymeleafUtil() {
    }

    private static ClassLoaderTemplateResolver configureHtmlResolver() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCacheTTLMs(0L);
//        resolver.setCacheable(true);
//        resolver.setCacheTTLMs(3600000L);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");
        return resolver;
    }

    public static String getHtmlPage(String htmlPage, Context context) {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(configureHtmlResolver());
        return templateEngine.process(htmlPage, context);
    }
}