package at.technikum.application.controller;

import at.technikum.application.common.Controller;
import at.technikum.application.service.AuthService;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;
import at.technikum.server.http.ContentType;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class AuthController implements Controller {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Response handle(Request request) {
        Response response = new Response();
        response.setContentType(ContentType.TEXT_PLAIN);

        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(Status.METHOD_NOT_ALLOWED);
            response.setBody("error: Nur POST erlaubt");
            return response;
        }

        String body = request.getBody();
        if (body == null || body.trim().isEmpty()) {
            response.setStatus(Status.BAD_REQUEST);
            response.setBody("error: Body fehlt");
            return response;
        }

        Map<String, String> data;
        String trimmed = body.trim();
        if (trimmed.startsWith("{")) {
            data = parseSimpleJson(trimmed);
        } else {
            data = parseFormEncoded(trimmed);
        }

        String username = data.get("username");
        String password = data.get("password");

        if (username == null || password == null) {
            response.setStatus(Status.BAD_REQUEST);
            response.setBody("error: Erforderlich: username und password");
            return response;
        }

        try {
            authService.registerUser(username, password);
        } catch (IllegalArgumentException e) {
            response.setStatus(Status.BAD_REQUEST);
            response.setBody("error: Registrierung fehlgeschlagen: " + e.getMessage());
            return response;
        }

        response.setStatus(Status.OK);
        response.setBody("Registered: " + username + " - Hat funktioniert");
        return response;
    }

    // sehr einfacher, toleranter Parser für flaches JSON {"k":"v", ...}
    private Map<String, String> parseSimpleJson(String json) {
        Map<String, String> map = new HashMap<>();
        String s = json.trim();
        if (s.startsWith("{") && s.endsWith("}")) s = s.substring(1, s.length() - 1);
        if (s.trim().isEmpty()) return map;

        String[] pairs = s.split(",");
        for (String p : pairs) {
            String[] kv = p.split(":", 2);
            if (kv.length != 2) continue;
            String key = stripQuotes(kv[0].trim());
            String val = stripQuotes(kv[1].trim());
            map.put(key, val);
        }
        return map;
    }

    private Map<String, String> parseFormEncoded(String body) {
        Map<String, String> map = new HashMap<>();
        String[] pairs = body.split("&");
        for (String p : pairs) {
            String[] kv = p.split("=", 2);
            if (kv.length != 2) continue;
            String key = urlDecode(kv[0]);
            String val = urlDecode(kv[1]);
            map.put(key, val);
        }
        return map;
    }

    private String stripQuotes(String s) {
        if (s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }

    private String urlDecode(String s) {
        try {
            return URLDecoder.decode(s, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            return s;
        }
    }
}