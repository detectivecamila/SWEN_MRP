package at.technikum.application.media;

import java.util.List;

public interface IMediaRepository {

    Media save(Media media);

    Media findById(int mediaId);

    List<Media> findAll();

    boolean update(Media media, int userId);

    boolean delete(int mediaId, int userId);
}