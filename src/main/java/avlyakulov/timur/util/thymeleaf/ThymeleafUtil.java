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
        resolver.setCharacterEncoding("UTF-8");
        resolver.setPrefix("/templates/");
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
