package vanilla.api;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.function.Supplier;
import vanilla.app.api.user.RegistrationRequest;
import vanilla.app.api.user.RegistrationResponse;

public class AppClient extends BaseAppClient {

    public AppClient(String ba) {
        super(ba);
    }

    public HttpResponse<String> ping() throws IOException, InterruptedException, URISyntaxException {
        return callGet("ping");
    }

    public HttpResponse<String> hello() throws IOException, InterruptedException, URISyntaxException {
        return callGet("hello");
    }

    public HttpResponse<String> hello(String name) throws IOException, InterruptedException, URISyntaxException {
        return callGet("hello", "name", name);
    }

    public HttpResponse<Supplier<RegistrationResponse>> register(RegistrationRequest data) throws IOException, InterruptedException, URISyntaxException {
        return callPost("register", data, RegistrationResponse.class);
    }
}
