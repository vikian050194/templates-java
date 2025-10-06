package vanilla.app.api.time;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.text.DateFormat;
import java.util.Date;
import vanilla.app.api.Constants;
import vanilla.app.api.Handler;
import vanilla.app.api.ResponseEntity;
import vanilla.app.api.StatusCode;
import vanilla.app.errors.GlobalExceptionHandler;

public class TimeHandler extends Handler {

    public TimeHandler(ObjectMapper objectMapper,
            GlobalExceptionHandler exceptionHandler) {
        super(objectMapper, exceptionHandler);
    }

    @Override
    public String url() {
        return "time";
    }

    @Override
    protected ResponseEntity<String> doGet(URI uri) {
        var response = DateFormat.getDateTimeInstance().format(new Date());

        return new ResponseEntity<>(response,
                getHeaders(Constants.CONTENT_TYPE, Constants.TEXT_HTML), StatusCode.OK);
    }
}
