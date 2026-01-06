package at.technikum.application.auth;

import at.technikum.application.common.Controller;
import at.technikum.application.user.User;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;

public class AuthController implements Controller {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Response handle(Request request) {
        Response response = new Response();
        String body = request.getBody();

        try {
            String username = extract(body, "username");
            String password = extract(body, "password");

            if (request.getPath().endsWith("/register")) {
                User user = authService.register(username, password);

                if (user == null) {
                    System.out.println("User ist NULL");
                    response.setStatus(Status.BAD_REQUEST);
                    response.setBody("{\"error\":\"User exists\"}");
                    return response;
                }

                System.out.println("User ist nicht NULL");
                response.setStatus(Status.OK);
                response.setBody(
                        "{ \"userId\": " + user.getId() +
                        ", \"username\": \"" + user.getUsername() + "\" }"
                );
                return response;
            }

            if (request.getPath().endsWith("/login")) {
                String token = authService.login(username, password);

                if (token == null) {
                    response.setStatus(Status.BAD_REQUEST);
                    response.setBody("{\"error\":\"Invalid credentials\"}");
                    return response;
                }

                response.setStatus(Status.OK);
                response.setBody("{\"token\":\"" + token + "\"}");
                return response;
            }

            response.setStatus(Status.NOT_FOUND);
            response.setBody("{\"error\":\"Unknown endpoint\"}");
            return response;

        } catch (Exception e) {
            response.setStatus(Status.BAD_REQUEST);
            System.out.println("Fehler");
            response.setBody("{\"error\":\"Invalid JSON\"}");
            return response;
        }
    }

    private String extract(String json, String key) {
        json = json.trim();

        String quotedKey = "\"" + key + "\"";
        int keyIndex = json.indexOf(quotedKey);
        if (keyIndex == -1) {
            throw new RuntimeException("Key not found: " + key);
        }

        int colonIndex = json.indexOf(":", keyIndex);
        if (colonIndex == -1) {
            throw new RuntimeException("Colon not found for key: " + key);
        }

        int valueStart = json.indexOf("\"", colonIndex) + 1;
        int valueEnd = json.indexOf("\"", valueStart);

        if (valueStart == 0 || valueEnd == -1) {
            throw new RuntimeException("Value not properly quoted for key: " + key);
        }

        return json.substring(valueStart, valueEnd);
    }
}