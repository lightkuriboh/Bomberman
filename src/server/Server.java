package server;

import signal.GameStart;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {
    private int port;
    private boolean running = false;
    private ServerSocket serverSocket;
    private String mapPath = "C:\\Users\\MSI\\Documents\\GitHub\\Bomberman\\res\\levels\\level1.txt";
    private List<Connection> connectionList = new ArrayList<>();
    private List<Integer> cmdList = new ArrayList<>();
    private ServerListener serverListener = new ServerListener(this);
    private int Max_players;
    private GameStart gameStart = new GameStart(mapPath);
    public Server(int port, int mp) {
        this.port = port;
        this.Max_players = mp;
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
        Connection connection = new Connection(socket, this, serverListener);
        connection.setId(connectionList.size()-1);
        connectionList.add(connection);
        cmdList.add(0);
        new Thread(connection).start();
        if (connectionList.size() == Max_players) sendDataToAll(gameStart);
        System.out.println(connectionList.size());
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
        for(int i=0;i<connectionList.size();i++) {
            connectionList.get(i).sendObject(packet);
        }
    }

    public void updateMove(int id,int dirState) {
        cmdList.set(id,dirState);
    }

}
