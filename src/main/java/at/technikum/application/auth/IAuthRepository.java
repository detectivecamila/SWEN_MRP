package at.technikum.application.auth;

import at.technikum.application.user.User;

public interface IAuthRepository {

    void save(User user);  //Für AuthService.register() notwendig

    boolean userExists(String username);

    User findByUsername(String username);

    boolean validateCredentials(String username, String password);
}