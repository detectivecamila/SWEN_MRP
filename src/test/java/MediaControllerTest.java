import at.technikum.application.media.*;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MediaControllerTest {

    private MediaController controller;
    private MediaService mediaService;
    private InMemoryMediaRepository mediaRepo;

    @BeforeEach
    void setup() {
        mediaRepo = new InMemoryMediaRepository();
        mediaRepo.clear();
        mediaService = new MediaService(mediaRepo);
        controller = new MediaController(mediaService);
    }

    @Test
    void givenInvalidToken_whenCreateMedia_thenShouldReturn401() {
        Request request = new Request();
        request.setMethod("POST");
        request.setPath("/media/create/invalidToken");
        request.setBody("""
            {
              "title":"Test",
              "description":"Desc",
              "mediaType":"Movie",
              "releaseYear":2023,
              "genres":["Action"],
              "ageRestriction":12
            }
        """);

        Response response = controller.handle(request);

        assertEquals(Status.UNAUTHORIZED, response.getStatus());
    }

    @Test
    void givenNonExistingMediaId_whenGetMedia_thenShouldReturn404() {
        Request request = new Request();
        request.setMethod("GET");
        request.setPath("/media/999");
        request.setAttribute("mediaId", "999");

        Response response = controller.handle(request);

        assertEquals(Status.NOT_FOUND, response.getStatus());
    }

    @Test
    void givenInvalidToken_whenDeleteMedia_thenShouldReturn401() {
        Request request = new Request();
        request.setMethod("DELETE");
        request.setPath("/media/1/invalidToken");
        request.setAttribute("mediaId", "1");
        request.setAttribute("token", "invalidToken");

        Response response = controller.handle(request);

        assertEquals(Status.UNAUTHORIZED, response.getStatus());
    }
}