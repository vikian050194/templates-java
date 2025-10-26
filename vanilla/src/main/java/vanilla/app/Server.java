package vanilla.app;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import vanilla.app.api.Handler;
import vanilla.app.api.hello.HelloHandler;
import vanilla.app.api.ping.PingHandler;
import vanilla.app.api.time.TimeHandler;
import vanilla.app.api.user.RegistrationHandler;
import vanilla.app.auth.AuthenticatorFactory;
import vanilla.app.auth.UserRoleAuthenticator;
import vanilla.app.config.LoggerConfig;
import vanilla.app.config.RuntimeConfig.RunMode;
import vanilla.app.dependency.DependencyFactory;
import vanilla.domain.user.User;

public final class Server {

    private final HttpServer server;
    private final RunMode mode;

    public Server(int httpPort, RunMode mode) throws IOException {
        this.mode = mode;
        if (mode == RunMode.PROD) {
            server = HttpServer.create(new InetSocketAddress("0.0.0.0", httpPort), 0);
        } else {
            server = HttpServer.create(new InetSocketAddress("127.0.0.1", httpPort), 0);
        }
    }

    private void setAuthenticator(HttpContext c, UserRoleAuthenticator authenticator) {
        if (((Handler) c.getHandler()).auth() == false) {
            return;
        }

        c.setAuthenticator(authenticator);
    }

    public void init(DependencyFactory df) {
        var userRoleBasicAuthenticator = AuthenticatorFactory.createBasicAuthenticator(df.getUserRepository());

        var handlers = new ArrayList<Handler>();
        handlers.add(new PingHandler(df.getObjectMapper(),
                df.getErrorHandler()));
        handlers.add(new TimeHandler(df.getObjectMapper(),
                df.getErrorHandler()));
        handlers.add(new HelloHandler(df.getObjectMapper(),
                df.getErrorHandler()));
        handlers.add(new RegistrationHandler(df.getUserService(), df.getObjectMapper(),
                df.getErrorHandler()));

        var contexts = handlers.stream().map(h -> server.createContext(h.getUrl(), h));

        contexts.forEach(c -> setAuthenticator(c, userRoleBasicAuthenticator));

        if (!mode.equals(RunMode.TEST)) {
            seed(df);
        }
    }

    public InetSocketAddress start() {
        server.start();
        // TODO is it best String formatting option?
        LoggerConfig.getApplicationLogger().info("Server started on %s".formatted(server.getAddress()));
        return server.getAddress();
    }

    public void stop(int delay) {
        server.stop(delay);
        // TODO is it best String formatting option?
        LoggerConfig.getDebugLogger().info("Server stopped initiated with delay %s sec".formatted(delay));
    }

    private void seed(DependencyFactory dependencyFactory) {
        var users = dependencyFactory.getUserRepository();
        for (int i = 1; i <= 8; i++) {
            var username = String.format("user%d", i);
            var password = String.format("pass%d", i);
            // TODO is it better to use single class instance as create method argument?
            users.create(new User(username, password));
        }
    }
}
