package at.technikum.application.echo;

import at.technikum.application.common.Application;
import at.technikum.server.http.ContentType;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;

public class EchoApplication implements Application {

    @Override
    public Response handle(Request request) {
        System.out.println(request);

        Response response = new Response();
        response.setStatus(Status.OK);
        response.setContentType(ContentType.TEXT_PLAIN);

        String firstLine = request.getMethod() + " " + request.getPath();
        response.setBody("ECHO: " + firstLine);

        return response;
    }
}