package at.technikum.application.common;

import at.technikum.application.controller.MediaController;
import at.technikum.application.controller.UserController;
import at.technikum.application.controller.RatingController;
import at.technikum.application.repository.UserRepository;
import at.technikum.application.service.UserService;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;
import at.technikum.server.http.ContentType;

import java.util.HashMap;
import java.util.Map;

public class Router {
    private final Map<String, Controller> routes = new HashMap<>();
    private final UserRepository userRepository = new UserRepository();
    private final UserService userService = new UserService(userRepository);

    public void addRoute(String path, Controller controller) {
        routes.put(path, controller);
    }

    public Router() {
        routes.put("/users", new UserController(userService));
        routes.put("/media", new MediaController());
        routes.put("/ratings", new RatingController());
    }

    public Response route(Request request) {
        String path = request.getPath();

        if (path.startsWith("/users/")) {
            return new UserController(userService).handle(request);
        }
        if (path.startsWith("/media/")) {
            return new MediaController().handle(request);
        }
        if (path.startsWith("/ratings/")) {
            return new RatingController().handle(request);
        }

        Response echo = new Response();
        echo.setStatus(Status.OK);
        echo.setContentType(ContentType.TEXT_PLAIN);
        echo.setBody("Echo: " + path + " - Hat funktioniert");
        return echo;
    }
}