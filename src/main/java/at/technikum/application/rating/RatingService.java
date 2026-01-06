package at.technikum.application.rating;

import java.util.List;

public class RatingService {

    private RatingRepository ratingRepository = new RatingRepository();

    //Normaler Konstruktor
    public RatingService() {
        this.ratingRepository = new RatingRepository();
    }

    //Test-Konstruktor
    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public Rating createRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    public boolean likeRating(int ratingId, int userId) {
        return ratingRepository.like(ratingId, userId);
    }

    public boolean updateRating(Rating rating) {
        return ratingRepository.update(rating);
    }

    public boolean confirmComment(int ratingId, int userId) {
        return ratingRepository.confirm(ratingId, userId);
    }

    public List<Rating> getRatingsByUser(int userId) {
        return ratingRepository.findByUserId(userId);
    }
}