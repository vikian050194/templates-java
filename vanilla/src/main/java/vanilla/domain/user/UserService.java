package vanilla.domain.user;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository ur) {
        userRepository = ur;
    }

    public User create(User credentials) {
        return userRepository.create(credentials);
    }

    public User read(int userId) {
        return userRepository.read(userId);
    }

    public boolean exists(String username) {
        return userRepository.exists(username);
    }

}
