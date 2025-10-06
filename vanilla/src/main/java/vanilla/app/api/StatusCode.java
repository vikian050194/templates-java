package vanilla.app.api;

public enum StatusCode {
    OK(200),
    CREATED(201),
    ACCEPTED(202),
    BAD_REQUEST(400),
    METHOD_NOT_ALLOWED(405);

    private final int code;

    StatusCode(int c) {
        code = c;
    }

    public int getCode() {
        return code;
    }
}
