package at.technikum.application.repository;

import java.util.HashMap;
import java.util.Map;

public class AuthRepository {
    private final Map<String, String> users = new HashMap<>(); // username -> password

    public boolean existsByUsername(String username) {
        return users.containsKey(username);
    }

    public void save(String username, String password) {
        users.put(username, password);
    }

    // optional: for testing/auth
    public String findPasswordByUsername(String username) {
        return users.get(username);
    }
}