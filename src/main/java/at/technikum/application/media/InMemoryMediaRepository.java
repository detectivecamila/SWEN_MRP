package at.technikum.application.media;

import java.util.*;

public class InMemoryMediaRepository implements IMediaRepository {

    //Speicher für Media-Objekte
    private final Map<Integer, Media> store = new HashMap<>();

    //ID-Counter für neue Medien
    private int nextId = 1;

    @Override
    public Media save(Media media) {
        //Neue ID nur vergeben, wenn noch keine gesetzt ist (0 = neu)
        if (media.getMediaId() == 0) {
            media.setMediaId(nextId++);
        } else {
            //Wenn Media schon eine ID hat, ensure nextId immer größer bleibt
            nextId = Math.max(nextId, media.getMediaId() + 1);
        }
        store.put(media.getMediaId(), media);
        return media;
    }

    @Override
    public Media findById(int mediaId) {
        return store.get(mediaId);
    }

    @Override
    public List<Media> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public boolean update(Media media, int userId) {
        Media existing = store.get(media.getMediaId());
        if (existing == null || existing.getUserId() != userId) return false;

        existing.setTitle(media.getTitle());
        existing.setDescription(media.getDescription());
        existing.setMediaType(media.getMediaType());
        existing.setReleaseYear(media.getReleaseYear());
        existing.setGenres(media.getGenres());
        existing.setAgeRestriction(media.getAgeRestriction());
        return true;
    }

    @Override
    public boolean delete(int mediaId, int userId) {
        Media existing = store.get(mediaId);
        if (existing == null || existing.getUserId() != userId) return false;
        store.remove(mediaId);
        return true;
    }

    //Für Unit-Tests: clear() setzt alles zurück
    public void clear() {
        store.clear();
        nextId = 1;
    }
}