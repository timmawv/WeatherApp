package avlyakulov.timur.util.thymeleaf;

import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.Context;

import java.io.IOException;

public class ThymeleafUtilRespondHtmlView {

    public static void respondHtmlPage(String htmlPage, Context context, HttpServletResponse resp) throws IOException {
        resp.getWriter().write(ThymeleafUtil.getHtmlPage(htmlPage, context));
    }
}