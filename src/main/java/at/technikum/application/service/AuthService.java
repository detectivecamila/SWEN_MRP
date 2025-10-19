package at.technikum.application.service;

import at.technikum.application.repository.AuthRepository;

public class AuthService {
    private final AuthRepository repo;

    public AuthService(AuthRepository repo) {
        this.repo = repo;
    }

    /**
     * Registriert einen User. Wirft IllegalArgumentException wenn username fehlt/leer
     * oder wenn username bereits existiert.
     */
    public void registerUser(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("username darf nicht leer sein");
        }
        if (password == null) {
            password = "";
        }
        if (repo.existsByUsername(username)) {
            throw new IllegalArgumentException("Benutzer existiert bereits");
        }
        // einfache Speicherung (kein Hashing hier)
        repo.save(username, password);
    }

    // optional: einfache Auth-Methode
    public boolean authenticate(String username, String password) {
        String stored = repo.findPasswordByUsername(username);
        return stored != null && stored.equals(password);
    }
}