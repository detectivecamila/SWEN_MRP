package at.technikum.server.http;

import java.util.HashMap;
import java.util.Map;

public class Request {

    private String path;
    private String method;
    private String body;

    private final Map<String, String> headers = new HashMap<>();
    private Map<String, String> attributes = new HashMap<>();

    //Basic
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getBody() {
        return body;

    }
    public void setBody(String body) {
        this.body = body;
    }

    //Attributes
    public void setAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    @Override
    public String toString() {
        return "Request {\n" +
                "  Methode: " + method + "\n" +
                "  Pfad: " + path + "\n" +
                "  Header: " + headers + "\n" +
                "  Body: " + (body != null ? body : "<leer>") + "\n" +
                "}";
    }
}