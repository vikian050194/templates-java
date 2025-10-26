package vanilla.data.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import vanilla.domain.user.User;
import vanilla.domain.user.UserRepository;

public class InMemoryUserRepository implements UserRepository {

    private static final int INIT_ID = 1;

    private final Map<Integer, User> STORE = new HashMap<>();

    private int lastId = INIT_ID;

    private int getId() {
        return lastId++;
    }

    @Override
    public User create(User user) {
        if (STORE.values().stream().anyMatch((u) -> u.username.equals(user.username))) {
            return null;
        }

        var id = getId();
        user.id = id;
        STORE.put(id, user);
        return user;
    }

    @Override
    public User read(int userId) {
        if (STORE.containsKey(userId)) {
            return STORE.get(userId);
        }

        return null;
    }

    @Override
    public List<User> readAll() {
        return STORE.values().stream().toList();
    }

    @Override
    public boolean checkCredentials(String username, String password) {
        return STORE.values().stream().anyMatch((u) -> u.username.equals(username) && u.password.equals(password));
    }

    @Override
    public List<String> fetchRoleList(String username) {
        return List.of("USER");
    }

    @Override
    public boolean exists(String username) {
        return STORE.values().stream().anyMatch((u) -> u.username.equals(username));
    }
}
