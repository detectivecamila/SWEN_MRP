package at.technikum.application.controller;

import at.technikum.application.common.Controller;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;
import at.technikum.server.http.ContentType;

public class RatingController implements Controller {

    @Override
    public Response handle(Request request) {
        String mediaId = request.getAttribute("mediaId");
        String ratingId = request.getAttribute("ratingId");
        String path = request.getPath();

        Response response = new Response();
        response.setContentType(ContentType.TEXT_PLAIN);

        if (mediaId != null && path.matches("/media/\\d+/rate")) {
            response.setStatus(Status.OK);
            response.setBody("MediaId (fuer Bewertung): " + mediaId + " (Pfad: " + path + ") - Hat funktioniert");
        } else if (ratingId != null && path.matches("/ratings/\\d+/like")) {
            response.setStatus(Status.OK);
            response.setBody("RatingId (fuer Like): " + ratingId + " (Pfad: " + path + ") - Hat funktioniert");
        } else {
            response.setStatus(Status.NOT_FOUND);
            response.setBody("error: Unbekannter Rating-Pfad: " + path);
        }
        return response;
    }
}