package vanilla.api.register;

import java.io.IOException;
import java.net.URISyntaxException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import vanilla.api.BaseApiTest;
import vanilla.app.api.user.RegistrationRequest;
import vanilla.app.api.user.RegistrationResponse;

class RegisterHandlerTest extends BaseApiTest {

    @Test
    void testMissedUsername() throws IOException, InterruptedException, URISyntaxException {
        // Arrange
        var expectedStatus = 400;

        // Act
        var response = client.register(new RegistrationRequest(null, "pass"));

        // Assert
        assertEquals(expectedStatus, response.statusCode());
    }

    @Test
    void testMissedPassword() throws IOException, InterruptedException, URISyntaxException {
        // Arrange
        var expectedStatus = 400;

        // Act
        var response = client.register(new RegistrationRequest("user", null));

        // Assert
        assertEquals(expectedStatus, response.statusCode());
    }

    @Test
    void testHappyPath() throws IOException, InterruptedException, URISyntaxException {
        // Arrange
        var expectedStatus = 200;
        var expectedBody = new RegistrationResponse(1);

        // Act
        var response = client.register(new RegistrationRequest("user", "pass"));

        // Assert
        assertEquals(expectedStatus, response.statusCode());
        assertEquals(expectedBody.getId(), response.body().get().getId());
    }

    @Test
    void testDuplicatedUsername() throws IOException, InterruptedException, URISyntaxException {
        // Arrange
        var expectedStatus = 400;
        client.register(new RegistrationRequest("user", "pass1"));

        // Act
        var response = client.register(new RegistrationRequest("user", "pass2"));

        // Assert
        assertEquals(expectedStatus, response.statusCode());
    }
}
