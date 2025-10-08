package vanilla.app.auth;

import com.sun.net.httpserver.Authenticator;
import com.sun.net.httpserver.HttpExchange;
import java.util.List;
import java.util.Objects;

public abstract class UserRoleAuthenticator extends Authenticator {

    final Authenticator authenticator;

    protected UserRoleAuthenticator(Authenticator authenticator) {
        Objects.requireNonNull(authenticator);
        this.authenticator = authenticator;
    }

    public abstract List<String> getUserRoles(String username);

    @Override
    public Result authenticate(HttpExchange exchange) {
        var result = this.authenticator.authenticate(exchange);

        if (result instanceof Success s) {
            var p = s.getPrincipal();
            var userRolePrincipal = new UserRolePrincipal(p.getUsername(), p.getRealm(), getUserRoles(p.getUsername()));
            return new Success(userRolePrincipal);
        }

        return result;
    }
}
