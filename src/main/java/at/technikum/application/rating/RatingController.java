package at.technikum.application.rating;

import at.technikum.application.auth.AuthService;
import at.technikum.application.common.Controller;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;

public class RatingController implements Controller {

    private final RatingService ratingService;

    //Normaler Konstruktor
    public RatingController() {
        this.ratingService = new RatingService();
    }

    //Test-Konstruktor
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Override
    public Response handle(Request request) {
        Response response = new Response();
        try {
            String path = request.getPath(); //z.B: /ratings/3/Player1-mrpToken/rate
            String[] parts = path.split("/");

            if ("POST".equalsIgnoreCase(request.getMethod()) && parts.length == 5 && "rate".equals(parts[4])) {
                //Create Rating
                int mediaId = Integer.parseInt(parts[2]);
                String token = parts[3];
                Integer userId = AuthService.getUserIdByToken(token);

                if (userId == null) {
                    response.setStatus(Status.UNAUTHORIZED);
                    response.setBody("{\"error\":\"Invalid token\"}");
                    return response;
                }

                //JSON Parsen
                String body = request.getBody();
                int stars = getJsonInt(body, "stars");
                if (stars < 1 || stars > 5) {
                    response.setStatus(Status.BAD_REQUEST);
                    response.setBody("{\"error\":\"Stars must be between 1 and 5\"}");
                    return response;
                }
                String comment = getJsonStringOrEmpty(body, "comment");

                Rating rating = new Rating();
                rating.setMediaId(mediaId);
                rating.setUserId(userId);
                rating.setStars(stars);
                rating.setComment(comment);
                rating.setConfirmed(false);

                Rating created = ratingService.createRating(rating);
                response.setStatus(Status.OK);
                response.setBody("{\"message\":\"Rating created\",\"ratingId\":" + created.getRatingId() + "}");
                return response;
            }

            //POST Like
            if ("POST".equalsIgnoreCase(request.getMethod()) && parts.length == 5 && "like".equals(parts[4])) {
                int ratingId = Integer.parseInt(parts[2]);
                String token = parts[3];
                Integer userId = AuthService.getUserIdByToken(token);

                if (userId == null) {
                    response.setStatus(Status.UNAUTHORIZED);
                    response.setBody("{\"error\":\"Invalid token\"}");
                    return response;
                }

                boolean liked = ratingService.likeRating(ratingId, userId);
                if (!liked) {
                    response.setStatus(Status.BAD_REQUEST);
                    response.setBody("{\"error\":\"Cannot like rating\"}");
                    return response;
                }

                response.setStatus(Status.OK);
                response.setBody("{\"message\":\"Liked\"}");
                return response;
            }

            //PUT Update Rating
            if ("PUT".equalsIgnoreCase(request.getMethod()) && parts.length == 4) {
                int ratingId = Integer.parseInt(parts[2]);
                String token = parts[3];
                Integer userId = AuthService.getUserIdByToken(token);
                if (userId == null) {
                    response.setStatus(Status.UNAUTHORIZED);
                    response.setBody("{\"error\":\"Invalid token\"}");
                    return response;
                }

                String body = request.getBody();
                int stars = getJsonInt(body, "stars");
                if (stars < 1 || stars > 5) {
                    response.setStatus(Status.BAD_REQUEST);
                    response.setBody("{\"error\":\"Stars must be between 1 and 5\"}");
                    return response;
                }
                String comment = getJsonStringOrEmpty(body, "comment");

                Rating rating = new Rating();
                rating.setRatingId(ratingId);
                rating.setUserId(userId);
                rating.setStars(stars);
                rating.setComment(comment);

                boolean updated = ratingService.updateRating(rating);
                if (!updated) {
                    response.setStatus(Status.NOT_FOUND);
                    response.setBody("{\"error\":\"Rating not found\"}");
                    return response;
                }

                response.setStatus(Status.OK);
                response.setBody("{\"message\":\"Rating updated\"}");
                return response;
            }

            //POST Confirm Comment
            if ("POST".equalsIgnoreCase(request.getMethod()) && parts.length == 5 && "confirm".equals(parts[4])) {
                int ratingId = Integer.parseInt(parts[2]);
                String token = parts[3];
                Integer userId = AuthService.getUserIdByToken(token);
                if (userId == null) {
                    response.setStatus(Status.UNAUTHORIZED);
                    response.setBody("{\"error\":\"Invalid token\"}");
                    return response;
                }

                boolean confirmed = ratingService.confirmComment(ratingId, userId);
                if (!confirmed) {
                    response.setStatus(Status.BAD_REQUEST);
                    response.setBody("{\"error\":\"Cannot confirm comment\"}");
                    return response;
                }

                response.setStatus(Status.OK);
                response.setBody("{\"message\":\"Comment confirmed\"}");
                return response;
            }

            response.setStatus(Status.BAD_REQUEST);
            response.setBody("{\"error\":\"Unsupported method or path\"}");
            return response;

        } catch (Exception e) {
            response.setStatus(Status.BAD_REQUEST);
            response.setBody("{\"error\":\"" + e.getMessage() + "\"}");
            return response;
        }
    }

    //JSON Helper
    private int getJsonInt(String json, String key) {
        String pattern = "\"" + key + "\":";
        int start = json.indexOf(pattern);
        if (start == -1) throw new RuntimeException("Key not found: " + key);
        start += pattern.length();
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("}", start);
        return Integer.parseInt(json.substring(start, end).trim());
    }

    private String getJsonStringOrEmpty(String json, String key) {
        String pattern = "\"" + key + "\":";
        int start = json.indexOf(pattern);
        if (start == -1) return "";
        start += pattern.length();
        while (json.charAt(start) == ' ' || json.charAt(start) == '\n') start++;
        if (json.charAt(start) == '"') start++;
        int end = json.indexOf("\"", start);
        if (end == -1) return "";
        return json.substring(start, end);
    }
}