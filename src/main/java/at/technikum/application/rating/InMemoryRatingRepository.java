package at.technikum.application.rating;

import java.util.*;

public class InMemoryRatingRepository extends RatingRepository {

    private final Map<Integer, Rating> ratings = new HashMap<>();
    private final Set<String> likes = new HashSet<>();
    private int nextId = 1;

    //Create
    @Override
    public Rating save(Rating rating) {
        rating.setRatingId(nextId++);
        ratings.put(rating.getRatingId(), rating);
        return rating;
    }

    //Find by User
    @Override
    public List<Rating> findByUserId(int userId) {
        List<Rating> result = new ArrayList<>();
        for (Rating r : ratings.values()) {
            if (r.getUserId() == userId) {
                result.add(r);
            }
        }
        return result;
    }

    //Update
    @Override
    public boolean update(Rating rating) {
        Rating existing = ratings.get(rating.getRatingId());
        if (existing == null) return false;
        if (existing.getUserId() != rating.getUserId()) return false;

        existing.setStars(rating.getStars());
        existing.setComment(rating.getComment());
        return true;
    }

    //Like
    @Override
    public boolean like(int ratingId, int userId) {
        Rating rating = ratings.get(ratingId);
        if (rating == null) return false;

        String key = ratingId + ":" + userId;
        if (likes.contains(key)) {
            return false; //bereits geliked
        }

        likes.add(key);
        return true;
    }

    //Confirm
    @Override
    public boolean confirm(int ratingId, int userId) {
        Rating rating = ratings.get(ratingId);
        if (rating == null) return false;
        if (rating.getUserId() != userId) return false;

        rating.setConfirmed(true);
        return true;
    }
}