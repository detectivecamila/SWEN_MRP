package at.technikum.server.util;

import at.technikum.server.http.Request;
import com.sun.net.httpserver.HttpExchange;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class RequestMapper {

    public Request fromExchange(HttpExchange exchange) {
        Request request = new Request();

        request.setMethod(exchange.getRequestMethod());
        URI uri = exchange.getRequestURI();
        request.setPath(uri.getPath());

        String rawQuery = uri.getRawQuery();
        if (rawQuery != null && !rawQuery.isEmpty()) {
            request.setPath(request.getPath() + "?" + rawQuery);
        }

        try (InputStream is = exchange.getRequestBody();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int r;
            while ((r = is.read(buffer)) != -1) {
                baos.write(buffer, 0, r);
            }
            String body = baos.toString(StandardCharsets.UTF_8);
            if (!body.isEmpty()) {
                request.setBody(body);
            }
        } catch (IOException ignored) {
        }

        return request;
    }
}