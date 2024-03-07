package avlyakulov.timur.util.thymeleaf;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class ThymeleafUtil {
    private static final ClassLoaderTemplateResolver resolver;

    static {
        resolver = configureResolver();
    }

    private ThymeleafUtil() {
    }

    private static ClassLoaderTemplateResolver configureResolver() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCacheable(true);
        resolver.setCacheTTLMs(3600000L);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setPrefix("/view/");
        resolver.setSuffix(".html");
        return resolver;
    }

    public static String getHtmlPage(String htmlPage, Context context) {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);

        return templateEngine.process(htmlPage, context);
    }

    public static String getHtmlPage(String htmlPage) {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);


        return templateEngine.process(htmlPage, new Context());
    }
}
