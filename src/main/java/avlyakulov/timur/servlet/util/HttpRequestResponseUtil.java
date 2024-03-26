package avlyakulov.timur.servlet.util;

import avlyakulov.timur.custom_exception.BadHttpRequest;
import avlyakulov.timur.custom_exception.ServerErrorException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.rmi.ServerError;

@Slf4j
public class HttpRequestResponseUtil {

    public String getBodyOfResponse(String urlWeather) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlWeather))
                .version(HttpClient.Version.HTTP_1_1)
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        log.info("One request to API was made");
        if (response.statusCode() == 400) {
            throw new BadHttpRequest();
        } else if (response.statusCode() >= 500) {
            throw new ServerErrorException("Open Weather doesn't work");
        } else {
            return response.body();
        }
    }
}