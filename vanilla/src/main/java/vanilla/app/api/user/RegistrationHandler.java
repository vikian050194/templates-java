package vanilla.app.api.user;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        // TODO register+login or signup+signin
        return "register";
    }
//
//    @Override
//    protected ResponseEntity<RegistrationResponse> doGet(URI uri) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }

    @Override
    protected ResponseEntity<RegistrationResponse> doPost(URI uri, InputStream is) {
        RegistrationRequest registerRequest = super.readRequest(is, RegistrationRequest.class);
        var user = new User(registerRequest.getLogin(), PasswordEncoder.encode(registerRequest.getPassword()));
        user = userService.create(user);
        RegistrationResponse response = new RegistrationResponse(user.id);

        return new ResponseEntity<>(response,
                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
    }
}
