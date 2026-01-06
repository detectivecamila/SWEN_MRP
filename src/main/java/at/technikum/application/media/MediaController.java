package at.technikum.application.media;

import at.technikum.application.auth.AuthService;
import at.technikum.application.common.Controller;
import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;

import java.util.ArrayList;
import java.util.List;

public class MediaController implements Controller {

    private final MediaService mediaService;

    //Normaler Konstruktor
    public MediaController() {
        this.mediaService = new MediaService();
    }

    //Test-Konstruktor
    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @Override
    public Response handle(Request request) {
        Response response = new Response();

        try {
            String path = request.getPath();
            String[] parts = path.split("/");

            //Create Media
            if ("POST".equalsIgnoreCase(request.getMethod()) && parts.length == 4 && "create".equals(parts[2])) {
                String token = parts[3];
                Integer userId = AuthService.getUserIdByToken(token);

                if (userId == null) {
                    response.setStatus(Status.UNAUTHORIZED);
                    response.setBody("{\"error\":\"Invalid token\"}");
                    return response;
                }

                Media media = parseMedia(request.getBody());
                media.setUserId(userId);

                Media created = mediaService.createMedia(media);

                response.setStatus(Status.OK);
                response.setBody(mediaToJson(created));
                return response;
            }

            //Get Media by id
            else if ("GET".equalsIgnoreCase(request.getMethod()) && request.getAttribute("mediaId") != null) {
                int mediaId = Integer.parseInt(request.getAttribute("mediaId"));
                Media media = mediaService.getMediaById(mediaId);

                if (media == null) {
                    response.setStatus(Status.NOT_FOUND);
                    response.setBody("{\"error\":\"Media not found\"}");
                    return response;
                }

                response.setStatus(Status.OK);
                response.setBody(mediaToJson(media));
                return response;
            }

            //Update Media
            else if ("PUT".equalsIgnoreCase(request.getMethod()) && request.getAttribute("mediaId") != null && request.getAttribute("token") != null) {
                int mediaId = Integer.parseInt(request.getAttribute("mediaId"));
                String token = request.getAttribute("token");
                Integer userId = AuthService.getUserIdByToken(token);

                if (userId == null) {
                    response.setStatus(Status.UNAUTHORIZED);
                    response.setBody("{\"error\":\"Invalid token\"}");
                    return response;
                }

                Media existing = mediaService.getMediaById(mediaId);
                if (existing == null) {
                    response.setStatus(Status.NOT_FOUND);
                    response.setBody("{\"error\":\"Media not found\"}");
                    return response;
                }

                if (existing.getUserId() != userId) {
                    response.setStatus(Status.UNAUTHORIZED);
                    response.setBody("{\"error\":\"Not allowed to update\"}");
                    return response;
                }

                Media updatedData = parseMedia(request.getBody());
                updatedData.setMediaId(mediaId);
                updatedData.setUserId(userId);
                boolean updated = mediaService.updateMedia(updatedData, userId);

                if (!updated) {
                    response.setStatus(Status.BAD_REQUEST);
                    response.setBody("{\"error\":\"Failed to update media\"}");
                    return response;
                }

                response.setStatus(Status.OK);
                response.setBody(mediaToJson(updatedData));
                return response;
            }
            //Delete Media
            else if ("DELETE".equalsIgnoreCase(request.getMethod()) && request.getAttribute("mediaId") != null && request.getAttribute("token") != null) {
                int mediaId = Integer.parseInt(request.getAttribute("mediaId"));
                String token = request.getAttribute("token");
                Integer userId = AuthService.getUserIdByToken(token);

                if (userId == null) {
                    response.setStatus(Status.UNAUTHORIZED);
                    response.setBody("{\"error\":\"Invalid token\"}");
                    return response;
                }

                boolean deleted = mediaService.deleteMedia(mediaId, userId);
                if (!deleted) {
                    response.setStatus(Status.NOT_FOUND);
                    response.setBody("{\"error\":\"Media not found or not allowed\"}");
                    return response;
                }

                response.setStatus(Status.OK);
                response.setBody("{\"message\":\"Media deleted\"}");
                return response;
            }

            response.setStatus(Status.BAD_REQUEST);
            response.setBody("{\"error\":\"Unsupported method or wrong path\"}");
            return response;

        } catch (Exception e) {
            response.setStatus(Status.BAD_REQUEST);
            response.setBody("{\"error\":\"" + e.getMessage() + "\"}");
            return response;
        }
    }

    //JSON Parser
    private Media parseMedia(String json) {
        Media media = new Media();
        media.setTitle(getJsonString(json, "title"));
        media.setDescription(getJsonString(json, "description"));
        media.setMediaType(getJsonString(json, "mediaType"));
        media.setReleaseYear(getJsonInt(json, "releaseYear"));
        media.setAgeRestriction(getJsonInt(json, "ageRestriction"));
        media.setGenres(getJsonArray(json, "genres"));
        return media;
    }

    private String getJsonString(String json, String key) {
        String pattern = "\"" + key + "\":";
        int start = json.indexOf(pattern);
        if (start == -1) throw new RuntimeException("Key not found: " + key);
        start += pattern.length();
        while (json.charAt(start) == ' ' || json.charAt(start) == '\n') start++;
        start++;
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }

    private int getJsonInt(String json, String key) {
        String pattern = "\"" + key + "\":";
        int start = json.indexOf(pattern);
        if (start == -1) throw new RuntimeException("Key not found: " + key);
        start += pattern.length();
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("}", start);
        return Integer.parseInt(json.substring(start, end).trim());
    }

    private List<String> getJsonArray(String json, String key) {
        List<String> list = new ArrayList<>();
        String pattern = "\"" + key + "\":";
        int start = json.indexOf(pattern);
        if (start == -1) return list;
        start += pattern.length();
        start++;
        int end = json.indexOf("]", start);
        if (end == -1) return list;

        String[] items = json.substring(start, end).split(",");
        for (String s : items) {
            if (!s.trim().isEmpty()) {
                list.add(s.trim().replace("\"", ""));
            }
        }
        return list;
    }

    private String mediaToJson(Media media) {
        return "{"
                + "\"mediaId\":" + media.getMediaId() + ","
                + "\"userId\":" + media.getUserId() + ","
                + "\"title\":\"" + media.getTitle() + "\","
                + "\"description\":\"" + media.getDescription() + "\","
                + "\"mediaType\":\"" + media.getMediaType() + "\","
                + "\"releaseYear\":" + media.getReleaseYear() + ","
                + "\"genres\":" + media.getGenres() + ","
                + "\"ageRestriction\":" + media.getAgeRestriction()
                + "}";
    }
}