package at.technikum.application.media;

import java.util.List;

public class MediaService {

    private final IMediaRepository repository;

    //Normaler Konstruktor
    public MediaService() {
        this.repository = new MediaRepository();
    }

    //Test-Konstruktor
    public MediaService(IMediaRepository repository) {
        this.repository = repository;
    }

    public Media createMedia(Media media) {
        return repository.save(media);
    }

    public Media getMediaById(int mediaId) {
        return repository.findById(mediaId);
    }

    public List<Media> getAllMedia() {
        return repository.findAll();
    }

    public boolean updateMedia(Media media, int userId) {
        return repository.update(media, userId);
    }

    public boolean deleteMedia(int mediaId, int userId) {
        return repository.delete(mediaId, userId);
    }
}