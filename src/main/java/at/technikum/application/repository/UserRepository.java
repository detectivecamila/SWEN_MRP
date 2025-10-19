package at.technikum.application.repository;

import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UserRepository {
    private final Map<Integer, String> users = new HashMap<>(); // id -> username
    private final AtomicInteger idCounter = new AtomicInteger(0);

    public int create(String username) {
        int id = idCounter.incrementAndGet();
        users.put(id, username);
        return id;
    }

    public String findUsernameById(int id) {
        return users.get(id);
    }

    public Map<String, String> findProfileById(int id) {
        String username = findUsernameById(id);
        if (username == null) return null;
        Map<String,String> profile = new HashMap<>();
        profile.put("id", String.valueOf(id));
        profile.put("username", username);
        return profile;
    }
}