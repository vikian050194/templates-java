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
    void testFirstUser() throws IOException, InterruptedException, URISyntaxException {
        // Arrange
        var expectedStatus = 200;
        var expectedBody = new RegistrationResponse(1);

        // Act
        var response = client.register(new RegistrationRequest(null, null));

        // Assert
        assertEquals(expectedStatus, response.statusCode());
        assertEquals(expectedBody.getId(), response.body().get().getId());
    }
}
