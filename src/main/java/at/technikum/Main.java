package at.technikum;

import at.technikum.application.common.MRPApplication;
import at.technikum.server.Server;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        Server server = new Server(8080, new MRPApplication());
        System.out.println("Server starting on port 8080");
        server.start();
    }
}