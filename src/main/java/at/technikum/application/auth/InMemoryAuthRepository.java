package at.technikum.application.auth;

import at.technikum.application.user.User;

import java.util.HashMap;
import java.util.Map;

public class InMemoryAuthRepository implements IAuthRepository {

    private static final Map<String, User> users = new HashMap<>();
    private static int idCounter = 1;

    @Override
    public void save(User user) {
        user.setId(idCounter++);
        users.put(user.getUsername(), user);
    }

    public static void clear() {
        users.clear();
        idCounter = 1;
    }

    @Override
    public boolean userExists(String username) {
        return users.containsKey(username);
    }

    @Override
    public User findByUsername(String username) {
        return users.get(username);
    }

    @Override
    public boolean validateCredentials(String username, String password) {
        User user = users.get(username);
        return user != null && user.getPassword().equals(password);
    }
}