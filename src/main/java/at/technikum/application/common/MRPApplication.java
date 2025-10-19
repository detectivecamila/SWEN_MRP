package at.technikum.application.common;

import at.technikum.application.common.Application;
import at.technikum.application.common.Router;
import at.technikum.application.controller.UserController;
import at.technikum.application.repository.UserRepository;
import at.technikum.application.service.UserService;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class MRPApplication implements Application {
    private final Router router;
    private final UserRepository userRepository = new UserRepository();

    private final UserService userService = new UserService(userRepository);

    public MRPApplication() {
        this.router = new Router();

        // Controller mit Services initialisieren
        this.router.addRoute("/users", new UserController(userService));
    }

    @Override
    public Response handle(Request request) {
        return router.route(request);
    }
}