package vanilla.app.auth;

import com.sun.net.httpserver.HttpPrincipal;
import java.util.Collections;
import java.util.List;

public class UserRolePrincipal extends HttpPrincipal {

  private final List<String> roles;

  public UserRolePrincipal(final String username, final String realm, final List<String> roles) {
    super(username, realm);
    // TODO Collections.unmodifiableList vs List.copyOf
    this.roles = Collections.unmodifiableList(roles);
  }
  
  public List<String> getRoles() {
    return roles;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((roles == null) ? 0 : roles.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (!super.equals(obj))
      return false;
    return (obj instanceof UserRolePrincipal other && roles.equals(other.roles));
  }
}
