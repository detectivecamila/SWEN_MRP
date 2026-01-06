package at.technikum.application.common;

import at.technikum.server.http.Request;
import at.technikum.server.http.Response;
import at.technikum.server.http.Status;

import java.util.HashMap;
import java.util.Map;

public class Router {

    private final Map<String, Controller> routes = new HashMap<>();

    public void addRoute(String pathPattern, Controller controller) {
        routes.put(pathPattern, controller);
    }

    public Response route(Request request) {
        String path = request.getPath();

        //Exakter Match
        if (routes.containsKey(path)) {
            return routes.get(path).handle(request);
        }

        //Match mit {param}
        for (String pattern : routes.keySet()) {
            if (matchAndExtract(pattern, path, request)) {
                return routes.get(pattern).handle(request);
            }
        }

        Response response = new Response();
        response.setStatus(Status.NOT_FOUND);
        response.setBody("Not Found: " + path);
        return response;
    }

    private boolean matchAndExtract(String pattern, String path, Request request) {

        String[] p = pattern.split("/");
        String[] a = path.split("/");

        if (p.length != a.length) {
            return false;
        }

        for (int i = 0; i < p.length; i++) {

            //Platzhalter
            if (p[i].startsWith("{") && p[i].endsWith("}")) {
                String name = p[i].substring(1, p[i].length() - 1);

                //Nur bestimmte Platzhalter müssen Zahlen sein
                if (name.equals("mediaId") || name.equals("userId") || name.equals("ratingId")) {
                    if (!a[i].matches("\\d+")) return false;
                }

                //Token oder andere Strings können beliebig sein
                request.setAttribute(name, a[i]);
            }

            //Fixer Pfadteil
            else if (!p[i].equals(a[i])) {
                return false;
            }
        }
        return true;
    }
}