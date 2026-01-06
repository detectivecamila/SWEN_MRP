import at.technikum.application.auth.AuthService;
import at.technikum.application.auth.InMemoryAuthRepository;
import at.technikum.application.user.InMemoryUserRepository;
import at.technikum.application.user.InMemoryUserStore;
import at.technikum.application.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    private InMemoryUserRepository userRepo;
    private InMemoryAuthRepository authRepo;
    private AuthService authService;

    @BeforeEach
    void setup() {
        InMemoryUserStore.clear();
        InMemoryAuthRepository.clear();

        userRepo = new InMemoryUserRepository();
        authRepo = new InMemoryAuthRepository();

        authService = new AuthService(authRepo, userRepo);
    }

    @Test
    void givenWrongPassword_whenLogin_thenShouldFail() {
        authService.register("max", "secret");

        assertFalse(authRepo.validateCredentials("max", "wrong"));
    }

    @Test
    void givenUnknownUsername_whenLogin_thenShouldFail() {
        assertFalse(authRepo.validateCredentials("unknown", "any"));
    }

    @Test
    void givenCorrectCredentials_whenLogin_thenShouldSucceed() {
        authService.register("anna", "mypassword");

        assertTrue(authRepo.validateCredentials("anna", "mypassword"));
    }

    @Test
    void givenNewUsername_whenRegister_thenShouldReturnUser() {
        User user = authService.register("lisa", "pass123");

        assertNotNull(user);
        assertEquals("lisa", user.getUsername());
    }

    @Test
    void givenExistingUsername_whenRegister_thenShouldReturnNull() {
        authService.register("max", "secret");
        User duplicate = authService.register("max", "otherpass");

        assertNull(duplicate);
    }

    @Test
    void givenCorrectCredentials_whenLogin_thenShouldReturnToken() {
        authService.register("max", "secret");

        String token = authService.login("max", "secret");

        assertNotNull(token);
        assertEquals("max-mrpToken", token);
        assertTrue(authService.isAuthenticated(token));
    }
}