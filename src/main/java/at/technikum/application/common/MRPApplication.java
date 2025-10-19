package at.technikum.application.common;

import at.technikum.application.controller.AuthController;
import at.technikum.application.controller.MediaController;
import at.technikum.application.controller.RatingController;
import at.technikum.application.controller.UserController;
import at.technikum.application.repository.AuthRepository;
import at.technikum.application.repository.UserRepository;
import at.technikum.application.service.AuthService;
import at.technikum.application.service.UserService;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class MRPApplication implements Application {
    private final Router router;

    public MRPApplication() {
        this.router = new Router();

        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository);

        AuthRepository authRepository = new AuthRepository();
        AuthService authService = new AuthService(authRepository);

        UserController userController = new UserController(userService);
        MediaController mediaController = new MediaController();
        RatingController ratingController = new RatingController();
        AuthController authController = new AuthController(authService);

        router.addRoute("/users/{userId}/profile", userController);
        router.addRoute("/media/{mediaId}", mediaController);
        router.addRoute("/media/{mediaId}/rate", ratingController);
        router.addRoute("/ratings/{ratingId}/like", ratingController);
        router.addRoute("/api/users/register", authController);

        router.addRoute("/users", userController);
        router.addRoute("/media", mediaController);
        router.addRoute("/ratings", ratingController);
    }

    @Override
    public Response handle(Request request) {
        return router.route(request);
    }
}