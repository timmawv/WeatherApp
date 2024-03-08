package avlyakulov.timur.servlet.util;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@WebServlet(urlPatterns = "/styles/css/*")
public class ResourceCSSController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String url = req.getRequestURL().toString();
        String cssFile = url.substring(url.lastIndexOf("/"));
        InputStream is = getClass().getClassLoader().getResourceAsStream("static/css/" + cssFile);
        BufferedReader bf = new BufferedReader(new InputStreamReader(is));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = bf.readLine()) != null) {
            sb.append(line).append("\n");
        }
        resp.setContentType("text/css; charset=UTF-8");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(sb.toString());
    }
}