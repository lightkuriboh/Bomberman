package server;

public class ServerListener {
    private Server server;
    public ServerListener(Server server) {
        this.server = server;
    }

    public void handle(int id, Object data) {
        if (data instanceof Integer) server.updateMove(id,(Integer) data);
    }
}
