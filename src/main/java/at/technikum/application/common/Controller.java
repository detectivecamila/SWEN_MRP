package at.technikum.application.common;

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

public interface Controller {
    Response handle(Request request);
}