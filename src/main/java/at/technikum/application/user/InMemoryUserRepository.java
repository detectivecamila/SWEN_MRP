package at.technikum.application.user;

public class InMemoryUserRepository extends UserRepository{

    public void save(User user) {
        user.setId(InMemoryUserStore.nextId++);
        InMemoryUserStore.usersById.put(user.getId(), user);
        InMemoryUserStore.usersByUsername.put(user.getUsername(), user);
    }

    public User findById(int id) {
        return InMemoryUserStore.usersById.get(id);
    }

    public boolean usernameExists(String username) {
        return InMemoryUserStore.usersByUsername.containsKey(username);
    }

    public boolean updateUsername(int id, String newUsername) {
        if (usernameExists(newUsername)) return false;

        User user = findById(id);
        if (user == null) return false;

        InMemoryUserStore.usersByUsername.remove(user.getUsername());
        user.setUsername(newUsername);
        InMemoryUserStore.usersByUsername.put(newUsername, user);

        return true;
    }
}