package at.technikum.application.user;

import at.technikum.application.common.DBConnection;

import java.sql.*;

public class UserRepository {

    //User speichern (nach Registration)
    public void save(User user) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?) RETURNING userId";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user.setId(rs.getInt("userId")); //ID aus DB übernehmen
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //User anhand der userId auslesen
    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE userId = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
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

    //Prüfen, ob Username schon existiert
    public boolean usernameExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            return ps.executeQuery().next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Username updaten
    public boolean updateUsername(int id, String newUsername) {
        if (usernameExists(newUsername)) return false;

        String sql = "UPDATE users SET username = ? WHERE userId = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newUsername);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}