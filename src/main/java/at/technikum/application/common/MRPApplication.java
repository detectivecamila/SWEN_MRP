package at.technikum.application.common;

import at.technikum.application.auth.AuthController;
import at.technikum.application.auth.AuthRepository;
import at.technikum.application.auth.AuthService;
import at.technikum.application.media.MediaController;
import at.technikum.application.rating.RatingController;
import at.technikum.application.user.UserController;
import at.technikum.application.user.UserRepository;
import at.technikum.application.user.UserService;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public class MRPApplication implements Controller {
    private final Router router;

    public MRPApplication() {
        this.router = new Router();

        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository);

        AuthRepository authRepository = new AuthRepository();
        AuthService authService = new AuthService(authRepository, userRepository);

        UserController userController = new UserController(userService);
        MediaController mediaController = new MediaController();
        RatingController ratingController = new RatingController();
        AuthController authController = new AuthController(authService);

        //User
        router.addRoute("/users/{userId}/profile", userController);
        router.addRoute("/users/{token}/ratings", userController);
        router.addRoute("/users/{userId}/favorites", userController);

        //Media
        router.addRoute("/media/create/{token}", mediaController);
        router.addRoute("/media/{mediaId}/{token}", mediaController);
        router.addRoute("/media/{mediaId}", mediaController);

        //Rating
        router.addRoute("/ratings/{mediaId}/{token}/rate", ratingController);
        router.addRoute("/ratings/{ratingId}/{token}/like", ratingController);
        router.addRoute("/ratings/{ratingId}/{token}", ratingController);
        router.addRoute("/ratings/{ratingId}/{token}/confirm", ratingController);

        //Auth
        router.addRoute("/api/users/register", authController);
        router.addRoute("/api/users/login", authController);
    }

    @Override
    public Response handle(Request request) {
        return router.route(request);
    }
}