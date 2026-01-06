package at.technikum.application.auth;

import at.technikum.application.user.User;
import at.technikum.application.user.UserRepository;

import java.util.HashMap;
import java.util.Map;

public class AuthService {

    private final IAuthRepository authRepository;
    private final UserRepository userRepository;

    private static final Map<String, Integer> tokenStore = new HashMap<>();

    //Konstruktor für Produktion & Tests
    public AuthService(IAuthRepository authRepository, UserRepository userRepository) {
        this.authRepository = authRepository;
        this.userRepository = userRepository;
    }

    public User register(String username, String password) {
        if (authRepository.userExists(username)) return null;

        User user = new User(0, username, password);
        userRepository.save(user);
        authRepository.save(user);  //Damit AuthRepo die Daten kennt
        return user;
    }

    public String login(String username, String password) {
        if (!authRepository.validateCredentials(username, password)) {
            return null;
        }

        User user = authRepository.findByUsername(username);
        String token = username + "-mrpToken";

        tokenStore.put(token, user.getId());

        return token;
    }

    public static Integer getUserIdByToken(String token) {
        return tokenStore.get(token);
    }

    //Nur für Tests
    public static void addTestToken(String token, int userId) {
        tokenStore.put(token, userId);
    }

    public boolean isAuthenticated(String token) {
        return tokenStore.containsKey(token);
    }
}