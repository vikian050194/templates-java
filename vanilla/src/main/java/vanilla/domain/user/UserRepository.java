package vanilla.domain.user;

import java.util.List;

public interface UserRepository {

    User create(User user);

    User read(int userId);

    List<User> readAll();

    boolean exists(String username);

    boolean checkCredentials(String username, String password);

    List<String> fetchRoleList(String username);
}
