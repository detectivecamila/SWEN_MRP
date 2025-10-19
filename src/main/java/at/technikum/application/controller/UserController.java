package at.technikum.application.controller;

import at.technikum.application.common.Controller;
import at.technikum.application.service.UserService;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;
import at.technikum.server.http.ContentType;

public class UserController implements Controller {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Response handle(Request request) {
        int userId = -1;

        String path = request.getPath();
        String method = request.getMethod();

        String userIdStr = request.getAttribute("userId");
        try {
            userId = Integer.parseInt(userIdStr);
        } catch (NumberFormatException e) {
            Response response = new Response();
            response.setStatus(Status.BAD_REQUEST);
            response.setContentType(ContentType.TEXT_PLAIN);
            response.setBody("error: Ungültiger userId: " + userIdStr);
            return response;
        }

        Response response = new Response();
        response.setContentType(ContentType.TEXT_PLAIN);

        String[] parts = path.split("/");
        if (parts.length >= 4) {
            String action = parts[3];

            switch (action) {
                case "profile":
                    if ("GET".equalsIgnoreCase(method)) {
                        System.out.println("GET Profile für User: " + userId);
                        var profile = userService.getProfile(userId);
                        response.setStatus(Status.OK);
                        response.setBody(String.format("Pfad: %s\nProfil von User %d: %s - Hat funktioniert",
                            path, userId, profile));
                    } else if ("PUT".equalsIgnoreCase(method)) {
                        String body = request.getBody();
                        System.out.println("PUT Profile für User: " + userId);
                        var updatedProfile = userService.updateProfile(userId, body);
                        response.setStatus(Status.OK);
                        response.setBody(String.format("Pfad: %s\nProfil von User %d wurde geändert - Hat funktioniert",
                            path, userId));
                    } else {
                        response.setStatus(Status.METHOD_NOT_ALLOWED);
                        response.setBody("error: Methode nicht erlaubt: " + method);
                    }
                    break;

                default:
                    response.setStatus(Status.NOT_FOUND);
                    response.setBody("error: Unbekannte Action: " + action);
            }
        } else {
            response.setStatus(Status.NOT_FOUND);
            response.setBody("error: Ungültiger Pfad: " + path);
        }

        return response;
    }
}