package vanilla.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.function.Supplier;
import vanilla.app.api.user.RegistrationRequest;
import vanilla.app.api.user.RegistrationResponse;
import vanilla.app.errors.ApplicationExceptions;

public class AppClient {

    private final String baseAddress;
    private final HttpClient client;
    private final ObjectMapper om;
    private String token;

    public AppClient(String ba) {
        baseAddress = ba;
        client = HttpClient.newHttpClient();
        om = new ObjectMapper();
    }

    public void login(String username, String password) {
        token = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }

    private URI getUri(String uri) throws URISyntaxException {
        return new URI("%s/%s".formatted(baseAddress, uri));
    }

    private URI getUri(String uri, String key, int value) throws URISyntaxException {
        return new URI("%s/%s?%s=%s".formatted(baseAddress, uri, key, value));
    }

    private URI getUri(String uri, String key, String value) throws URISyntaxException {
        return new URI("%s/%s?%s=%s".formatted(baseAddress, uri, key, value));
    }

    private HttpRequest getRequest(String uri) throws URISyntaxException {
        // TODO extract setHeader call
        return HttpRequest.newBuilder().GET().uri(getUri(uri)).setHeader("Authorization", "Basic %s".formatted(token)).build();
    }

    private HttpRequest getRequest(String uri, String key, int value) throws URISyntaxException {
        return HttpRequest.newBuilder().GET().uri(getUri(uri, key, value)).setHeader("Authorization", "Basic %s".formatted(token)).build();
    }

    private HttpRequest getRequest(String uri, String key, String value) throws URISyntaxException {
        return HttpRequest.newBuilder().GET().uri(getUri(uri, key, value)).setHeader("Authorization", "Basic %s".formatted(token)).build();
    }

    private HttpRequest postRequest(String uri, byte[] data) throws URISyntaxException {
        return HttpRequest.newBuilder().POST(HttpRequest.BodyPublishers.ofByteArray(data)).uri(getUri(uri)).build();
    }

    public HttpResponse<String> callGet(String uri) throws IOException, InterruptedException, URISyntaxException {
        var request = getRequest(uri);
        var bodyHandler = HttpResponse.BodyHandlers.ofString();
        return client.send(request, bodyHandler);
    }

    public HttpResponse<String> callGet(String uri, String key, int value) throws IOException, InterruptedException, URISyntaxException {
        var request = getRequest(uri, key, value);
        var bodyHandler = HttpResponse.BodyHandlers.ofString();
        return client.send(request, bodyHandler);
    }

    public HttpResponse<String> callGet(String uri, String key, String value) throws IOException, InterruptedException, URISyntaxException {
        var request = getRequest(uri, key, value);
        var bodyHandler = HttpResponse.BodyHandlers.ofString();
        return client.send(request, bodyHandler);
    }

    // TODO Supplier vs. Optional vs. T
    public <T> HttpResponse<Supplier<T>> callGet(String uri, Class<T> bodyClass, String key, int value) throws IOException, InterruptedException, URISyntaxException {
        var request = getRequest(uri, key, value);
        var bodyHandler = new JsonBodyHandler(bodyClass);
        return client.send(request, bodyHandler);
    }

    protected <T> byte[] serializeInstance(T instance) {
        try {
            // TODO is it possible to make InputSttream instead of byte array?
            return om.writeValueAsBytes(instance);
        } catch (JsonProcessingException ex) {
            throw ApplicationExceptions.invalidRequest().apply(ex);
        }
    }

    // TODO Supplier vs. Optional vs. T
//    public <T> HttpResponse<Supplier<T>> callPost(String uri, T data, Class<T> bodyClass) throws IOException, InterruptedException, URISyntaxException {
//
//        var request = postRequest(uri, serializeInstance(data));
//        var bodyHandler = new JsonBodyHandler(bodyClass);
//        return client.send(request, bodyHandler);
//    }
    // TODO Supplier vs. Optional vs. T
    public <T, W> HttpResponse<Supplier<W>> callPost(String uri, T data, Class<W> bodyClass) throws IOException, InterruptedException, URISyntaxException {

        var request = postRequest(uri, serializeInstance(data));
        var bodyHandler = new JsonBodyHandler(bodyClass);
        return client.send(request, bodyHandler);
    }

    // TODO Supplier vs. Optional vs. T
    public <T> HttpResponse<String> callPost(String uri, T data) throws IOException, InterruptedException, URISyntaxException {

        var request = postRequest(uri, serializeInstance(data));
        var bodyHandler = HttpResponse.BodyHandlers.ofString();
        return client.send(request, bodyHandler);
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
