package at.technikum.server.http;

public enum Status{

    OK(200, "OK"),
    CREATED(201, "Created"),
    BAD_REQUEST(400, "Bad request"),
    UNAUTHORIZED(401, "Unauthorized"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed");

    private final int code;
    private final String message;

    Status(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode(){
        return code;
    }
}