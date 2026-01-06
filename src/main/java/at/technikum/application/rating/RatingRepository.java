package at.technikum.application.rating;

import at.technikum.application.common.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RatingRepository {

    //Create
    public Rating save(Rating rating) {
        String sql = "INSERT INTO ratings (mediaId, userId, stars, comment, confirmed) " +
                "VALUES (?, ?, ?, ?, ?) RETURNING ratingId";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, rating.getMediaId());
            ps.setInt(2, rating.getUserId());
            ps.setInt(3, rating.getStars());
            ps.setString(4, rating.getComment());
            ps.setBoolean(5, rating.isConfirmed());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rating.setRatingId(rs.getInt("ratingId"));
            }
            return rating;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Find by User
    public List<Rating> findByUserId(int userId) {
        String sql = "SELECT * FROM ratings WHERE userId = ?";
        List<Rating> ratings = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ratings.add(mapRowToRating(rs));
            }
            return ratings;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Update
    public boolean update(Rating rating) {
        String sql = "UPDATE ratings SET stars=?, comment=? WHERE ratingId=? AND userId=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, rating.getStars());
            ps.setString(2, rating.getComment());
            ps.setInt(3, rating.getRatingId());
            ps.setInt(4, rating.getUserId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Like
    public boolean like(int ratingId, int userId) {
        String sql = "INSERT INTO rating_likes (ratingId, userId) VALUES (?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ratingId);
            ps.setInt(2, userId);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            return false; // Duplicate like
        }
    }

    //Confirm
    public boolean confirm(int ratingId, int userId) {
        String sql = "UPDATE ratings SET confirmed=true WHERE ratingId=? AND userId=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, ratingId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Mapper
    private Rating mapRowToRating(ResultSet rs) throws SQLException {
        return new Rating(
                rs.getInt("ratingId"),
                rs.getInt("mediaId"),
                rs.getInt("userId"),
                rs.getInt("stars"),
                rs.getString("comment"),
                rs.getBoolean("confirmed")
        );
    }
}