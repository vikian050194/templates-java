package vanilla.app.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import vanilla.app.errors.ApplicationExceptions;
import vanilla.app.errors.GlobalExceptionHandler;

public abstract class Handler {

    private final ObjectMapper objectMapper;
    private final GlobalExceptionHandler exceptionHandler;
    private static final String URL_PREFIX = "/";

    public Handler(ObjectMapper objectMapper, GlobalExceptionHandler exceptionHandler) {
        this.objectMapper = objectMapper;
        this.exceptionHandler = exceptionHandler;
    }

    public abstract String url();

    public String getUrl() {
        return URL_PREFIX + url();
    }

    public void handle(HttpExchange exchange) {
        try {
            execute(exchange);
        } catch (Exception ex) {
            exceptionHandler.handle(ex, exchange);
        }
    }

    protected void execute(HttpExchange exchange) throws Exception {
        byte[] response;
        ResponseEntity e;
        var uri = exchange.getRequestURI();
        // TODO split uri by /
        // TODO extract query params
        var method = exchange.getRequestMethod();
        // TODO classic switch vs. switch rule
        // TODO make mathod values to enum
        // TODO extract all required data from URI and pass this object to sub-handlers
        switch (method) {
            case "GET": {
                e = doGet(uri);
                break;
            }
            case "POST": {
                e = doPost(uri, exchange.getRequestBody());
                break;
            }
            default: {
                throw ApplicationExceptions.methodNotAllowed(getMessage(method, uri)).get();
            }
        }

        var contentType = e.getHeaders().getFirst(Constants.CONTENT_TYPE);

        if (contentType == null) {
            response = new byte[0];
        } else {
            switch (contentType) {
                case Constants.APPLICATION_JSON: {
                    response = writeResponse(e.getBody());
                    break;
                }
                case Constants.TEXT_HTML: {
                    response = e.getBody().toString().getBytes();
                    break;
                }
                default: {
                    throw new Exception(String.format("unsupported content type - %s", contentType));
                }
            }
        }

        exchange.getResponseHeaders().putAll(e.getHeaders());
        exchange.sendResponseHeaders(e.getStatusCode().getCode(), response.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }

    private String getMessage(String method, URI uri) {
        return String.format("Method %s is not allowed for %s", method, uri);
    }

    protected <T> ResponseEntity<T> doGet(URI uri) {
        throw ApplicationExceptions.methodNotAllowed(getMessage("GET", uri)).get();
    }

    // TODO convert InputStream to DTO before call
    protected <T> ResponseEntity<T> doPost(URI uri, InputStream is) {
        throw ApplicationExceptions.methodNotAllowed(getMessage("POST", uri)).get();
    }

    protected <T> T readRequest(InputStream is, Class<T> type) {
        try {
            return objectMapper.readValue(is, type);
        } catch (IOException ex) {
            throw ApplicationExceptions.invalidRequest().apply(ex);
        }
    }

    protected <T> byte[] writeResponse(T response) {
        try {
            return objectMapper.writeValueAsBytes(response);
        } catch (JsonProcessingException ex) {
            throw ApplicationExceptions.invalidRequest().apply(ex);
        }
    }

    protected static Headers getHeaders(String key, String value) {
        var headers = new Headers();
        headers.set(key, value);
        return headers;
    }

    protected static Map<String, List<String>> splitQuery(String query) {
        if (query == null || query.isEmpty()) {
            return Collections.emptyMap();
        }

        return Pattern.compile("&").splitAsStream(query)
                .map(s -> Arrays.copyOf(s.split("="), 2))
                .collect(groupingBy(s -> decode(s[0]), mapping(s -> decode(s[1]), toList())));
    }

    private static String decode(final String encoded) {
        try {
            return encoded == null ? null : URLDecoder.decode(encoded, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 is a required encoding", e);
        }
    }
}
