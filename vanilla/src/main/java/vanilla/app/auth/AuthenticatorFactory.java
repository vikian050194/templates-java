package vanilla.app.auth;

import com.sun.net.httpserver.BasicAuthenticator;
import java.util.List;
import java.util.Objects;
import vanilla.domain.user.UserRepository;

public abstract class AuthenticatorFactory {

  public static UserRoleAuthenticator createBasicAuthenticator(UserRepository repository) {
    Objects.requireNonNull(repository, "If UserRepository is null, authentication cannot work");

    var basicAuth = new BasicAuthenticator("vanilla") {
      @Override
      public boolean checkCredentials(String username, String password) {
        return repository.checkCredentials(username, password);
      }
    };

    return new UserRoleAuthenticator(basicAuth) {    
      @Override
      public List<String> getUserRoles(String username) {
        return repository.fetchRoleList(username);
      }
    };
  }
}

