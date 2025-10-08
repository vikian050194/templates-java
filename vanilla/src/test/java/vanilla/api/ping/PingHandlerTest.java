package vanilla.api.ping;

import java.io.IOException;
import java.net.URISyntaxException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import vanilla.api.BaseApiTest;
import vanilla.app.api.user.RegistrationRequest;

class PingHandlerTest extends BaseApiTest {

    @Test
    void testCall() throws IOException, InterruptedException, URISyntaxException {
        // Arrange
        var expectedStatus = 200;
        var expectedBody = "pong";

        client.register(new RegistrationRequest("user", "pass"));
        client.login("user", "pass");

        // Act
        var response = client.ping();

        // Assert
        assertEquals(expectedStatus, response.statusCode());
        assertEquals(expectedBody, response.body());
    }

}
