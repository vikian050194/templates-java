package vanilla.app.api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import java.io.InputStream;
import java.net.URI;
import vanilla.app.api.Constants;
import vanilla.app.api.Handler;
import vanilla.app.api.ResponseEntity;
import vanilla.app.api.StatusCode;
import vanilla.app.errors.GlobalExceptionHandler;
import vanilla.domain.user.User;
import vanilla.domain.user.UserService;

public class RegistrationHandler extends Handler {

    private final UserService userService;

    public RegistrationHandler(UserService userService, ObjectMapper objectMapper,
            GlobalExceptionHandler exceptionHandler) {
        super(objectMapper, exceptionHandler);
        this.userService = userService;
    }

    @Override
    public String url() {
        // TODO register+username or signup+signin
        return "register";
    }

    @Override
    public boolean auth() {
        return false;
    }

    @Override
    protected ResponseEntity<RegistrationResponse> doPost(URI uri, InputStream is) {
        RegistrationRequest registerRequest = super.readRequest(is, RegistrationRequest.class);

        if (registerRequest.getUsername() == null) {
            return new ResponseEntity(null, new Headers(), StatusCode.BAD_REQUEST);
        }

        if (registerRequest.getPassword() == null) {
            return new ResponseEntity(null, new Headers(), StatusCode.BAD_REQUEST);
        }

        if (userService.exists(registerRequest.getUsername())) {
            return new ResponseEntity(null, new Headers(), StatusCode.BAD_REQUEST);
        }

        var user = new User(registerRequest.getUsername(), PasswordEncoder.encode(registerRequest.getPassword()));
        user = userService.create(user);
        RegistrationResponse response = new RegistrationResponse(user.id);

        return new ResponseEntity<>(response,
                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
    }
}
