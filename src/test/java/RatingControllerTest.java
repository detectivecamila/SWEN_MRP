import at.technikum.application.auth.AuthService;
import at.technikum.application.rating.*;
import at.technikum.server.http.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RatingControllerTest {

    private RatingController controller;
    private InMemoryRatingRepository inMemoryRepository;

    @BeforeEach
    void setup() {
        inMemoryRepository = new InMemoryRatingRepository();
        RatingService service = new RatingService(inMemoryRepository);
        controller = new RatingController(service);

        AuthService.addTestToken("testToken", 1);
    }

    @Test
    void givenStarsBelowOne_whenPostRating_thenShouldReturnBadRequest() {
        Request request = new Request();
        request.setMethod("POST");
        request.setPath("/ratings/1/testToken/rate");
        request.setBody("{\"stars\":0}");

        Response response = controller.handle(request);

        assertEquals(Status.BAD_REQUEST, response.getStatus());
    }

    @Test
    void givenStarsAboveFive_whenPostRating_thenShouldReturnBadRequest() {
        Request request = new Request();
        request.setMethod("POST");
        request.setPath("/ratings/1/testToken/rate");
        request.setBody("{\"stars\":6}");

        Response response = controller.handle(request);

        assertEquals(Status.BAD_REQUEST, response.getStatus());
    }

    @Test
    void givenOneStar_whenPostRating_thenShouldReturnOk() {
        Request request = new Request();
        request.setMethod("POST");
        request.setPath("/ratings/1/testToken/rate");
        request.setBody("{\"stars\":1}");

        Response response = controller.handle(request);

        assertEquals(Status.OK, response.getStatus());
    }

    @Test
    void givenFiveStars_whenPostRating_thenShouldReturnOk() {
        Request request = new Request();
        request.setMethod("POST");
        request.setPath("/ratings/1/testToken/rate");
        request.setBody("{\"stars\":5}");

        Response response = controller.handle(request);

        assertEquals(Status.OK, response.getStatus());
    }

    @Test
    void givenStarsBetweenOneAndFive_whenPostRating_thenShouldReturnOk() {
        Request request = new Request();
        request.setMethod("POST");
        request.setPath("/ratings/1/testToken/rate");
        request.setBody("{\"stars\":3}");

        Response response = controller.handle(request);

        assertEquals(Status.OK, response.getStatus());
    }
}