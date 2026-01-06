package at.technikum.application.user;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserStore {
    static final Map<Integer, User> usersById = new HashMap<>();
    public static final Map<String, User> usersByUsername = new HashMap<>();
    static int nextId = 1;

    public static void clear() {
        usersById.clear();
        usersByUsername.clear();
        nextId = 1;
    }
}