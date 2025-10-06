package vanilla.app.api.hello;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.List;
import vanilla.app.api.Constants;
import vanilla.app.api.Handler;
import vanilla.app.api.ResponseEntity;
import vanilla.app.api.StatusCode;
import vanilla.app.errors.GlobalExceptionHandler;

public class HelloHandler extends Handler {

    public HelloHandler(ObjectMapper objectMapper,
            GlobalExceptionHandler exceptionHandler) {
        super(objectMapper, exceptionHandler);
    }

    @Override
    public String url() {
        return "hello";
    }

    @Override
    protected ResponseEntity<String> doGet(URI uri) {
        var params = splitQuery(uri.getRawQuery());
        var noNameText = "Anonymous";
        var name = params.getOrDefault("name", List.of(noNameText)).stream().findFirst().orElse(noNameText);
        var response = String.format("Hello, %s!", name);

        return new ResponseEntity<>(response,
                getHeaders(Constants.CONTENT_TYPE, Constants.TEXT_HTML), StatusCode.OK);
    }
}
