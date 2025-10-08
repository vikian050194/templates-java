package vanilla.data.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import vanilla.domain.user.User;
import vanilla.domain.user.UserRepository;

public class InMemoryUserRepository implements UserRepository {

    private static final int INITIAL_ID = 1;

    private final Map<Integer, User> USERS_STORE = new HashMap<>();

    private int lastId = INITIAL_ID;

    private int getId() {
        return lastId++;
    }

    @Override
    public User create(User user) {
        if (USERS_STORE.values().stream().anyMatch((u) -> u.username.equals(user.username))) {
            return null;
        }

        var id = getId();
        user.id = id;
        USERS_STORE.put(id, user);
        return user;
    }

    @Override
    public User read(int userId) {
        if (USERS_STORE.containsKey(userId)) {
            return USERS_STORE.get(userId);
        }

        return null;
    }

    @Override
    public List<User> readAll() {
        return USERS_STORE.values().stream().toList();
    }

    public boolean checkCredentials(String username, String password) {
        return USERS_STORE.values().stream().anyMatch((u) -> u.username.equals(username) && u.password.equals(password));
    }

    public List<String> fetchRoleList(String username) {
        return List.of("USER");
    }
}
