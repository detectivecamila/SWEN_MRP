package at.technikum.application.common;

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;

import java.util.*;
import java.util.stream.Collectors;

public class Router {
    private final Map<String, Controller> routes = new HashMap<>();

    public void addRoute(String pathPattern, Controller controller) {
        routes.put(pathPattern, controller);
    }

    public Response route(Request request) {
        String path = request.getPath();

        // 1) exakte Route
        Controller exact = routes.get(path);
        if (exact != null) {
            return exact.handle(request);
        }

        // 2) dynamische Routen: sortiere nach Länge (spezifischere zuerst)
        List<Map.Entry<String, Controller>> sorted = routes.entrySet().stream()
                .sorted(Comparator.comparingInt((Map.Entry<String, Controller> e) -> e.getKey().length()).reversed())
                .collect(Collectors.toList());

        for (Map.Entry<String, Controller> entry : sorted) {
            String pattern = entry.getKey();
            if (matchAndExtract(pattern, path, request)) {
                return entry.getValue().handle(request);
            }
        }

        // Default: 404-like Echo (Controller kann trotzdem erzeugt werden, hier einfach Response)
        Response echo = new Response();
        echo.setStatus(at.technikum.server.http.Status.NOT_FOUND);
        echo.setContentType(at.technikum.server.http.ContentType.TEXT_PLAIN);
        echo.setBody("error: Not Found: " + path);
        return echo;
    }

    // Pattern matching: "/users/{userId}/profile" etc.
    private boolean matchAndExtract(String pattern, String path, Request request) {
        String[] pSegments = pattern.split("/");
        String[] pathSegments = path.split("/");

        // handle leading slash producing empty segment
        List<String> pList = Arrays.stream(pSegments).filter(s -> !s.isEmpty()).collect(Collectors.toList());
        List<String> pathList = Arrays.stream(pathSegments).filter(s -> !s.isEmpty()).collect(Collectors.toList());

        if (pList.size() != pathList.size()) return false;

        for (int i = 0; i < pList.size(); i++) {
            String pSeg = pList.get(i);
            String pathSeg = pathList.get(i);
            if (pSeg.startsWith("{") && pSeg.endsWith("}")) {
                String name = pSeg.substring(1, pSeg.length() - 1);
                request.setAttribute(name, pathSeg);
            } else {
                if (!pSeg.equals(pathSeg)) {
                    return false;
                }
            }
        }
        return true;
    }
}