package vanilla.api;

import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import vanilla.app.App;
import vanilla.app.config.RuntimeConfig.RunMode;
import vanilla.app.dependency.DefaultDependencyFactory;
import vanilla.app.dependency.DependencyFactory;

public class BaseApiTest {

    protected App app;
    protected AppClient client;
    protected DependencyFactory dependencyFactory;
    // TODO read port from test.resources
    final int PORT = 9090;
    final RunMode MODE = RunMode.TEST;
    protected final String baseAddress = "http://localhost:%d".formatted(PORT);

    @BeforeEach
    public void initializeApp() throws IOException {
        dependencyFactory = new DefaultDependencyFactory();
        app = new App(PORT, MODE);
        app.init(dependencyFactory);
        app.start();
        client = new AppClient(baseAddress);
    }

    @AfterEach
    public void stopApp() {
        dependencyFactory = null;
        app.stop(0);
        app = null;
        client = null;
    }
}
