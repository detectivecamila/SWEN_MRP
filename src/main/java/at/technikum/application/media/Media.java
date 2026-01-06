package at.technikum.application.media;

import java.util.List;
import java.util.Objects;

public class Media {
    private int mediaId;        //Entspricht primary key in DB
    private int userId;         //Foreign key zu User
    private String title;
    private String description;
    private String mediaType;
    private int releaseYear;
    private List<String> genres;
    private int ageRestriction;

    public Media() {} //Leerer Konstruktor für JSON Parsing

    public Media(int mediaId, int userId, String title, String description, String mediaType,
                 int releaseYear, List<String> genres, int ageRestriction) {
        this.mediaId = mediaId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.mediaType = mediaType;
        this.releaseYear = releaseYear;
        this.genres = genres;
        this.ageRestriction = ageRestriction;
    }

    //Getter & Setter
    public int getMediaId() { return mediaId; }
    public void setMediaId(int mediaId) { this.mediaId = mediaId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getMediaType() { return mediaType; }
    public void setMediaType(String mediaType) { this.mediaType = mediaType; }

    public int getReleaseYear() { return releaseYear; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }

    public List<String> getGenres() { return genres; }
    public void setGenres(List<String> genres) { this.genres = genres; }

    public int getAgeRestriction() { return ageRestriction; }
    public void setAgeRestriction(int ageRestriction) { this.ageRestriction = ageRestriction; }

    //equals & hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Media)) return false;
        Media media = (Media) o;
        return mediaId == media.mediaId &&
                userId == media.userId &&
                releaseYear == media.releaseYear &&
                ageRestriction == media.ageRestriction &&
                Objects.equals(title, media.title) &&
                Objects.equals(description, media.description) &&
                Objects.equals(mediaType, media.mediaType) &&
                Objects.equals(genres, media.genres);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mediaId, userId, title, description, mediaType, releaseYear, genres, ageRestriction);
    }
}