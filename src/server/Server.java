package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {
    private int port;
    private boolean running = false;
    private ServerSocket serverSocket;
    List<Connection> connectionList = new ArrayList<>();
    public Server(int port) {
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                initSocket(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initSocket(Socket socket) {
        Connection connection = new Connection(socket, this);
        connectionList.add(connection);
        new Thread(connection).start();
    }

    public void shutdown() {
        running = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDataToAll(Object packet) {
        //System.out.println("mmm");
        System.out.println((String)packet);
        for(int i=0;i<connectionList.size();i++) {
            connectionList.get(i).sendObject(packet);
        }
    }

}
