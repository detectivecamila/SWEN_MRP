package at.technikum.application.service;

import at.technikum.application.repository.UserRepository;
import java.util.Map;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Map<String, String> getProfile(int userId) {
        return userRepository.findProfileById(userId);
    }

    public int createUser(String username) {
        return userRepository.create(username);
    }

    public String updateProfile(int userId, String body) {
        //Speicherung (noch simpel)
        return "Neues Profil: " + body;
    }

    public void registerUser(int userId, String firstname, String lastname, String email) {
        //Speicherung
    }
}