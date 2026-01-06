package at.technikum.application.user;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(int userId) {
        return userRepository.findById(userId);
    }

    public boolean updateUsername(int userId, String newUsername) {
        return userRepository.updateUsername(userId, newUsername);
    }
}