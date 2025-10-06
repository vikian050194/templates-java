package vanilla.domain.user;

import java.util.List;

public interface UserRepository {

    User create(User user);

    User read(int userId);

    List<User> readAll();
}
