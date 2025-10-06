package vanilla.app;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import vanilla.app.api.Handler;
import vanilla.app.api.hello.HelloHandler;
import vanilla.app.api.ping.PingHandler;
import vanilla.app.api.time.TimeHandler;
import vanilla.app.api.user.RegistrationHandler;
import vanilla.app.config.LoggerConfig;
import vanilla.app.config.RuntimeConfig;
import vanilla.app.config.RuntimeConfig.RunMode;
import vanilla.app.dependency.DefaultDependencyFactory;
import vanilla.app.dependency.DependencyFactory;
import vanilla.domain.user.User;

public final class App {

    private final HttpServer server;
    private final RunMode mode;

    public App(int httpPort, RunMode mode) throws IOException {
        this.mode = mode;
        if (mode == RunMode.PROD) {
            server = HttpServer.create(new InetSocketAddress("0.0.0.0", httpPort), 0);
        } else {
            server = HttpServer.create(new InetSocketAddress("127.0.0.1", httpPort), 0);
        }
    }

    public void init(DependencyFactory df) {
        var handlers = new ArrayList<Handler>();
        handlers.add(new PingHandler(df.getObjectMapper(),
                df.getErrorHandler()));
        handlers.add(new TimeHandler(df.getObjectMapper(),
                df.getErrorHandler()));
        handlers.add(new HelloHandler(df.getObjectMapper(),
                df.getErrorHandler()));
        handlers.add(new RegistrationHandler(df.getUserService(), df.getObjectMapper(),
                df.getErrorHandler()));

        handlers.stream().forEach(h -> server.createContext(h.getUrl(), h::handle));

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

    private static void seed(DependencyFactory dependencyFactory) {
        var users = dependencyFactory.getUserRepository();
        for (int i = 1; i <= 8; i++) {
            var login = String.format("user%d", i);
            var password = String.format("pass%d", i);
            // TODO is it better to use single class instance as create method argument?
            users.create(new User(login, password));
        }
    }

    public static void main(String[] args) throws IOException {
        LoggerConfig.setupGlobalLogger();

        var httpPort = RuntimeConfig.port();
        var mode = RuntimeConfig.getInstance().runMode();
        var app = new App(httpPort, mode);
        DependencyFactory dependencyFactory = new DefaultDependencyFactory();

        seed(dependencyFactory);

        app.init(dependencyFactory);
        app.start();

        LoggerConfig.getApplicationLogger().info("App is running");
    }
}
