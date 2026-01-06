package at.technikum.application.media;

import at.technikum.application.common.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MediaRepository implements IMediaRepository {

    //Speichert Daten in die Datenbank mit SQL Statements

    @Override
    public Media save(Media media) {
        String sql = "INSERT INTO media (userId, title, description, mediaType, releaseYear, genres, ageRestriction) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING mediaId";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, media.getUserId());
            ps.setString(2, media.getTitle());
            ps.setString(3, media.getDescription());
            ps.setString(4, media.getMediaType());
            ps.setInt(5, media.getReleaseYear());
            ps.setArray(6, con.createArrayOf("text", media.getGenres().toArray(new String[0])));
            ps.setInt(7, media.getAgeRestriction());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                media.setMediaId(rs.getInt("mediaId"));
            }

            return media;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Media findById(int mediaId) {
        String sql = "SELECT * FROM media WHERE mediaId = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, mediaId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;

            return mapRowToMedia(rs);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Media> findAll() {
        String sql = "SELECT * FROM media";
        List<Media> result = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(mapRowToMedia(rs));
            }
            return result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(Media media, int userId) {
        if (media.getUserId() != userId) return false;

        String sql = "UPDATE media SET title=?, description=? WHERE mediaId=? AND userId=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, media.getTitle());
            ps.setString(2, media.getDescription());
            ps.setInt(3, media.getMediaId());
            ps.setInt(4, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(int mediaId, int userId) {
        String sql = "DELETE FROM media WHERE mediaId=? AND userId=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, mediaId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Media mapRowToMedia(ResultSet rs) throws SQLException {
        Array sqlArray = rs.getArray("genres");
        String[] genresArray = (sqlArray != null) ? (String[]) sqlArray.getArray() : new String[0];

        return new Media(
                rs.getInt("mediaId"),
                rs.getInt("userId"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("mediaType"),
                rs.getInt("releaseYear"),
                Arrays.asList(genresArray),
                rs.getInt("ageRestriction")
        );
    }
}