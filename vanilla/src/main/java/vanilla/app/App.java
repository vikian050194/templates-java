package vanilla.app;

import java.io.IOException;
import vanilla.app.config.LoggerConfig;
import vanilla.app.config.RuntimeConfig;
import vanilla.app.dependency.DefaultDependencyFactory;
import vanilla.app.dependency.DependencyFactory;

public final class App {

    public static void main(String[] args) throws IOException {
        LoggerConfig.setupGlobalLogger();

        var httpPort = RuntimeConfig.port();
        var mode = RuntimeConfig.getInstance().runMode();
        var server = new Server(httpPort, mode);

        DependencyFactory dependencyFactory = new DefaultDependencyFactory();

        server.init(dependencyFactory);
        server.start();
    }
}
