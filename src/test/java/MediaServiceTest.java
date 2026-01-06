import at.technikum.application.media.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MediaServiceTest {

    private MediaService mediaService;
    private InMemoryMediaRepository mediaRepo;

    @BeforeEach
    void setup() {
        mediaRepo = new InMemoryMediaRepository();
        mediaRepo.clear();
        mediaService = new MediaService(mediaRepo);
    }

    @Test
    void givenMedia_whenCreate_thenShouldReturnMediaWithId() {
        Media media = new Media(0, 1, "My Title", "Desc", "Movie", 2023,
                Arrays.asList("Action", "Comedy"), 12);

        Media saved = mediaService.createMedia(media);

        assertNotNull(saved);
        assertTrue(saved.getMediaId() > 0);
        assertEquals("My Title", saved.getTitle());
    }

    @Test
    void givenExistingMediaId_whenGetMediaById_thenShouldReturnMedia() {
        Media media = mediaService.createMedia(new Media(0, 1, "Test", "Desc",
                "Movie", 2023, Arrays.asList("Drama"), 16));

        Media found = mediaService.getMediaById(media.getMediaId());

        assertNotNull(found);
        assertEquals("Test", found.getTitle());
    }

    @Test
    void givenNonExistingMediaId_whenGetMediaById_thenShouldReturnNull() {
        assertNull(mediaService.getMediaById(999));
    }

    @Test
    void givenMultipleMedia_whenGetAllMedia_thenShouldReturnAll() {
        Media m1 = mediaService.createMedia(new Media(0, 1, "A", "Desc", "Movie", 2021,
                Arrays.asList("Action"), 12));
        Media m2 = mediaService.createMedia(new Media(0, 2, "B", "Desc", "Series", 2022,
                Arrays.asList("Comedy"), 6));

        List<Media> all = mediaService.getAllMedia();

        assertEquals(2, all.size());
        assertTrue(all.contains(m1));
        assertTrue(all.contains(m2));
    }

    @Test
    void givenOwner_whenUpdateMedia_thenShouldReturnTrue() {
        Media media = mediaService.createMedia(new Media(0, 1, "Old", "Desc", "Movie", 2020,
                Arrays.asList("Action"), 12));
        media.setTitle("New Title");

        assertTrue(mediaService.updateMedia(media, 1));
        assertEquals("New Title",
                mediaService.getMediaById(media.getMediaId()).getTitle());
    }

    @Test
    void givenNonOwner_whenUpdateMedia_thenShouldReturnFalse() {
        Media media = mediaService.createMedia(new Media(0, 1, "Old", "Desc", "Movie", 2020,
                Arrays.asList("Action"), 12));
        media.setTitle("Hacker");

        assertFalse(mediaService.updateMedia(media, 2));
    }

    @Test
    void givenOwner_whenDeleteMedia_thenShouldReturnTrue() {
        Media media = mediaService.createMedia(new Media(0, 1, "Delete", "Desc", "Movie", 2020,
                Arrays.asList("Action"), 12));

        assertTrue(mediaService.deleteMedia(media.getMediaId(), 1));
        assertNull(mediaService.getMediaById(media.getMediaId()));
    }

    @Test
    void givenNonOwner_whenDeleteMedia_thenShouldReturnFalse() {
        Media media = mediaService.createMedia(new Media(0, 1, "Delete", "Desc", "Movie", 2020,
                Arrays.asList("Action"), 12));

        assertFalse(mediaService.deleteMedia(media.getMediaId(), 2));
        assertNotNull(mediaService.getMediaById(media.getMediaId()));
    }
}