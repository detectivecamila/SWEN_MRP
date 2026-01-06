package at.technikum.server;

import at.technikum.application.common.Controller;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.util.RequestMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Handler implements HttpHandler {
    private final Controller application;
    private final RequestMapper requestMapper;

    public Handler(Controller application, RequestMapper requestMapper) {
        this.application = application;
        this.requestMapper = requestMapper;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Request request = requestMapper.fromExchange(exchange);

        Response response = application.handle(request);

        byte[] bytes = (response.getBody() == null ? "" : response.getBody()).getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", response.getContentType() == null ? "text/plain" : response.getContentType());
        exchange.sendResponseHeaders(response.getStatusCode(), bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        } catch (IOException ignored) {}
    }
}