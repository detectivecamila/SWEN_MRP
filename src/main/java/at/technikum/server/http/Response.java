package at.technikum.server.http;

public class Response {

    private Status status;
    private ContentType contentType;
    private String body;

    public void setStatus(Status status) {
        this.status = status;
    }

    // safe: wenn kein Status gesetzt ist, OK zurückgeben
    public int getStatusCode() {
        return status != null ? status.getCode() : Status.OK.getCode();
    }

    public String getStatusMessage() {
        return status != null ? status.getMessage() : Status.OK.getMessage();
    }

    // safe: wenn kein ContentType gesetzt ist, text/plain zurückgeben
    public String getContentType() {
        return (contentType != null) ? contentType.getMimeType() : ContentType.TEXT_PLAIN.getMimeType();
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    // safe: leeren Body zurückgeben statt null
    public String getBody() {
        return body != null ? body : "";
    }

    public void setBody(String body) {
        this.body = body;
    }
}