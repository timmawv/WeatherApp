package avlyakulov.timur.util.thymeleaf;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.Map;

public class ThymeleafUtil {
    private ClassLoaderTemplateResolver resolver;

    {
        resolver = configureResolver();
    }

    private ClassLoaderTemplateResolver configureResolver() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");
        return resolver;
    }

    private Context setAttributes(Map<String, ?> attributes) {
        Context context = new Context();
        for (Map.Entry<String, ?> entry : attributes.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            context.setVariable(key, value);
        }
        return context;
    }

    public String getHtmlPage(Context context, String htmlPage) {
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);

        return templateEngine.process(htmlPage, context);
    }
}
