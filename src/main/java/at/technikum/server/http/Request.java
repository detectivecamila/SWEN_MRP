package at.technikum.server.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.stream.Collectors;

public class Request {
    
    private String path;
    private String method;
    private String body;

    private Map<String, String> attributes = new HashMap<>();

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

    public void setAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public String getAttributesStringIfAny() {
        if (attributes.isEmpty()) return "";
        return attributes.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining(", "));
    }

    public String toString() {
        return "Request {\n" +
                "  Methode: " + method + "\n" +
                "  Pfad: " + path + "\n" +
                "  Body: " + (body != null ? body : "<leer>") + "\n" +
                "}";
    }
}