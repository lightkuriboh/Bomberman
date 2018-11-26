package server;

public class Main {
    public static void main(String[] args) {
        int port = 1000;
        Server server = new Server(port);
        server.start();
    }
}
