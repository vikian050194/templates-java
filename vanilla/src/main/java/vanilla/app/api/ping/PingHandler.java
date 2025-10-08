package vanilla.app.api.ping;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import vanilla.app.api.Constants;
import vanilla.app.api.Handler;
import vanilla.app.api.ResponseEntity;
import vanilla.app.api.StatusCode;
import vanilla.app.errors.GlobalExceptionHandler;

public class PingHandler extends Handler {

    public PingHandler(ObjectMapper objectMapper,
            GlobalExceptionHandler exceptionHandler) {
        super(objectMapper, exceptionHandler);
    }

    @Override
    public String url() {
        return "ping";
    }

    @Override
    protected ResponseEntity<String> doGet(URI uri) {
        var response = "pong";
        // TODO make some ResponseEntity builder
        return new ResponseEntity<>(response,
                getHeaders(Constants.CONTENT_TYPE, Constants.TEXT_HTML), StatusCode.OK);
    }

    @Override
    public boolean auth() {
        return true;
    }
}
