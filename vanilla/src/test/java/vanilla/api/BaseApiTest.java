package vanilla.api;

import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import vanilla.app.Server;
import vanilla.app.config.RuntimeConfig.RunMode;
import vanilla.app.dependency.DefaultDependencyFactory;
import vanilla.app.dependency.DependencyFactory;

public class BaseApiTest {

    protected Server server;
    protected AppClient client;
    protected DependencyFactory dependencyFactory;
    // TODO read port from test.resources
    final int PORT = 9090;
    final RunMode MODE = RunMode.TEST;
    protected final String baseAddress = "http://localhost:%d".formatted(PORT);

    @BeforeEach
    public void initializeApp() throws IOException {
        dependencyFactory = new DefaultDependencyFactory();
        server = new Server(PORT, MODE);
        server.init(dependencyFactory);
        server.start();
        client = new AppClient(baseAddress);
    }

    @AfterEach
    public void stopApp() {
        dependencyFactory = null;
        server.stop(0);
        server = null;
        client = null;
    }
}
