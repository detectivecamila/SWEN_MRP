package at.technikum.application.auth;

import at.technikum.application.common.DBConnection;
import at.technikum.application.user.User;

import java.sql.*;

public class AuthRepository implements IAuthRepository {

    @Override
    public void save(User user) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?) RETURNING userId";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user.setId(rs.getInt("userId"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean userExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT userId, username, password FROM users WHERE username = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return null;

            return new User(
                    rs.getInt("userId"),
                    rs.getString("username"),
                    rs.getString("password")
            );

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean validateCredentials(String username, String password) {
        User user = findByUsername(username);
        return user != null && user.getPassword().equals(password);
    }
}