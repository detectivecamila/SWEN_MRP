package at.technikum.application.rating;

public class Rating {
    private int ratingId;    //Primary key
    private int mediaId;     //Foreign key auf Media
    private int userId;      //Foreign key auf User
    private int stars;       //1-5 stars
    private String comment;  //Optional
    private boolean confirmed; //Ob Kommentar bestätigt wurde

    public Rating() {}

    public Rating(int ratingId, int mediaId, int userId, int stars, String comment, boolean confirmed) {
        this.ratingId = ratingId;
        this.mediaId = mediaId;
        this.userId = userId;
        this.stars = stars;
        this.comment = comment;
        this.confirmed = confirmed;
    }

    // Getter & Setter
    public int getRatingId() {
        return ratingId;
    }
    public void setRatingId(int ratingId) {
        this.ratingId = ratingId;
    }

    public int getMediaId() {
        return mediaId;
    }
    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStars() {
        return stars;
    }
    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isConfirmed() {
        return confirmed;
    }
    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}