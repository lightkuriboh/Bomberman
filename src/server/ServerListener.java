package server;

import signal.SingleMove;

public class ServerListener {
    private Server server;
    public ServerListener(Server server) {
        this.server = server;
    }

    public void handle(int id, Object data) {
        if (data instanceof SingleMove) {
            server.updateMove(id,((SingleMove) data).getDirState());
        }
    }
}
