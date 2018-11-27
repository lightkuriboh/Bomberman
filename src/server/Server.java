package server;

import signal.AssignId;
import signal.FullPlayer;
import signal.GameStart;
import signal.PlayerMove;

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
    private GameStart gameStart;
    private CommandCenter commandCenter;
    public Server(int port, int mp) {
        this.port = port;
        this.Max_players = mp;
        gameStart = new GameStart(mapPath,mp);
        commandCenter = new CommandCenter(this);
        commandCenter.start();
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
        for(int i=0;i<connectionList.size();i++) {
            if (!connectionList.get(i).assigned()) {
                connectionList.set(i,connection);
                new Thread(connection).start();
                connection.sendObject(new AssignId(i));
                return;
            }
        }
        if (connectionList.size() == Max_players) {
            connection.sendObject(new FullPlayer());
        }
        connection.setId(connectionList.size());
        connectionList.add(connection);
        cmdList.add(0);
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

    public void sendDataToAll() {
        for(int i=0;i<connectionList.size();i++) {
            connectionList.get(i).sendObject(gameStart);
            AssignId data = new AssignId(i);
            connectionList.get(i).sendObject(data);
            connectionList.get(i).setIdAssigned(true);
        }
    }

    public void sendCmdToAll() {
        PlayerMove data = new PlayerMove(cmdList);
        System.out.println(data.getDirState().get(0));
        for(int i=0;i<connectionList.size();i++) {
            connectionList.get(i).sendObject(data);
        }
    }

    public void updateMove(int id,int dirState) {
        cmdList.set(id,dirState);
    }
    public List<Integer> getCmdList() {
        return cmdList;
    }
}
