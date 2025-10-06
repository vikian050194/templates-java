package vanilla.api.hello;

import java.io.IOException;
import java.net.URISyntaxException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import vanilla.api.BaseApiTest;

class HelloHandlerTest extends BaseApiTest {

    @Test
    void testAnonymousCall() throws IOException, InterruptedException, URISyntaxException {
        // Arrange
        var expectedStatus = 200;
        var expectedBody = "Hello, Anonymous!";

        // Act
        var response = client.hello();

        // Assert
        assertEquals(expectedStatus, response.statusCode());
        assertEquals(expectedBody, response.body());
    }

    @Test
    void testAliceCall() throws IOException, InterruptedException, URISyntaxException {
        // Arrange
        var name = "Alice";
        var expectedStatus = 200;
        var expectedBody = "Hello, Alice!";

        // Act
        var response = client.hello(name);

        // Assert
        assertEquals(expectedStatus, response.statusCode());
        assertEquals(expectedBody, response.body());
    }
}
