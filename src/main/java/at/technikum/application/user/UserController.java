package at.technikum.application.user;

import at.technikum.application.auth.AuthService;
import at.technikum.application.common.Controller;
import at.technikum.application.rating.Rating;
import at.technikum.application.rating.RatingService;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;

import java.util.List;

public class UserController implements Controller {

    private final UserService userService;
    private final RatingService ratingService = new RatingService();

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Response handle(Request request) {
        Response response = new Response();

        try {
            String path = request.getPath();
            String[] parts = path.split("/");

            //GET /users/{token}/ratings
            if ("GET".equalsIgnoreCase(request.getMethod())
                    && parts.length == 4
                    && "ratings".equals(parts[3])) {

                String token = parts[2];
                Integer userId = AuthService.getUserIdByToken(token);

                if (userId == null) {
                    response.setStatus(Status.UNAUTHORIZED);
                    response.setBody("{\"error\":\"Invalid token\"}");
                    return response;
                }

                List<Rating> ratings = ratingService.getRatingsByUser(userId);

                response.setStatus(Status.OK);
                response.setBody(ratingsToJson(ratings));
                return response;
            }

            //GET /users/{userId}/profile
            if ("GET".equalsIgnoreCase(request.getMethod())
                    && parts.length == 4
                    && "profile".equals(parts[3])) {

                int userId = Integer.parseInt(parts[2]);
                User user = userService.getUserById(userId);

                if (user == null) {
                    response.setStatus(Status.NOT_FOUND);
                    response.setBody("{\"error\":\"User not found\"}");
                    return response;
                }

                response.setStatus(Status.OK);
                response.setBody(
                        "{ \"userId\": " + user.getId() +
                        ", \"username\": \"" + user.getUsername() + "\" }"
                );
                return response;
            }

            //PUT /users/{userId}/profile
            if ("PUT".equalsIgnoreCase(request.getMethod())
                    && parts.length == 4
                    && "profile".equals(parts[3])) {

                int userId = Integer.parseInt(parts[2]);
                String body = request.getBody();
                String newUsername = extract(body, "username");

                boolean updated = userService.updateUsername(userId, newUsername);
                if (!updated) {
                    response.setStatus(Status.BAD_REQUEST);
                    response.setBody("{\"error\":\"Username already exists or user not found\"}");
                    return response;
                }

                response.setStatus(Status.OK);
                response.setBody("{\"message\":\"Username updated\"}");
                return response;
            }

            response.setStatus(Status.BAD_REQUEST);
            response.setBody("{\"error\":\"Unsupported method or path\"}");
            return response;

        } catch (Exception e) {
            response.setStatus(Status.BAD_REQUEST);
            response.setBody("{\"error\":\"Invalid request\"}");
            return response;
        }
    }

    //JSON Helper
    private String extract(String json, String key) {
        String pattern = "\"" + key + "\":";
        int idx = json.indexOf(pattern);
        if (idx == -1) throw new RuntimeException();

        int start = json.indexOf("\"", idx + pattern.length()) + 1;
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }

    private String ratingsToJson(List<Rating> ratings) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < ratings.size(); i++) {
            Rating r = ratings.get(i);
            sb.append("{")
                    .append("\"ratingId\":").append(r.getRatingId()).append(",")
                    .append("\"mediaId\":").append(r.getMediaId()).append(",")
                    .append("\"stars\":").append(r.getStars()).append(",")
                    .append("\"comment\":\"").append(r.getComment()).append("\",")
                    .append("\"confirmed\":").append(r.isConfirmed())
                    .append("}");
            if (i < ratings.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }
}