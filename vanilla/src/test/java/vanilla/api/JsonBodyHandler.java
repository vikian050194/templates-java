package vanilla.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.http.HttpResponse;
import java.util.function.Supplier;

public class JsonBodyHandler<T> implements HttpResponse.BodyHandler<Supplier<T>> {

    private final Class<T> bodyClass;

    public JsonBodyHandler(Class<T> bodyClass) {
        this.bodyClass = bodyClass;
    }

    @Override
    public HttpResponse.BodySubscriber<Supplier<T>> apply(HttpResponse.ResponseInfo responseInfo) {
        return asJSON(bodyClass);
    }

    public <T> HttpResponse.BodySubscriber<Supplier<T>> asJSON(Class<T> targetType) {
        HttpResponse.BodySubscriber<InputStream> upstream = HttpResponse.BodySubscribers.ofInputStream();

        return HttpResponse.BodySubscribers.mapping(
                upstream,
                inputStream -> toSupplierOfType(inputStream, targetType));
    }

    public <T> Supplier<T> toSupplierOfType(InputStream inputStream, Class<T> targetType) {
        return () -> {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                // TODO extract and pass ObjectMapper
                return objectMapper.readValue(inputStream, targetType);
            } catch (IOException e) {
                // TODO should I use UncheckedIOException here?
                throw new UncheckedIOException(e);
            }
        };
    }
}
