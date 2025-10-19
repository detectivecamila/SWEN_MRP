package at.technikum.application.controller;

import at.technikum.application.common.Controller;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;
import at.technikum.server.http.ContentType;

public class MediaController implements Controller {

    @Override
    public Response handle(Request request) {
        String mediaId = request.getAttribute("mediaId");
        String path = request.getPath();

        Response response = new Response();
        response.setContentType(ContentType.TEXT_PLAIN);

        if (path.matches("/media/\\d+")) {
            response.setStatus(Status.OK);
            response.setBody("MediaId: " + mediaId + " (Pfad: " + path + ") - Hat funktioniert");
        } else {
            response.setStatus(Status.NOT_FOUND);
            response.setBody("error: Unbekannter Media-Pfad: " + path);
        }
        return response;
    }
}