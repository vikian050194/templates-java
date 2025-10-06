package vanilla.app.dependency;

import com.fasterxml.jackson.databind.ObjectMapper;
import vanilla.app.errors.GlobalExceptionHandler;
import vanilla.domain.user.UserRepository;
import vanilla.domain.user.UserService;

public interface DependencyFactory {

    public ObjectMapper getObjectMapper();

    public GlobalExceptionHandler getErrorHandler();

    public UserRepository getUserRepository();

    public UserService getUserService();
}
