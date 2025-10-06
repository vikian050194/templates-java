package vanilla.app.api;

public class ErrorResponse {

    private final int code;
    private final String message;

    public ErrorResponse(int c, String m) {
        code = c;
        message = m;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
