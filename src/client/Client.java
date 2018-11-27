package client;
import signal.GameStart;
import signal.PlayerMove;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.input.Keyboard;
import uet.oop.bomberman.Game;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Client implements Runnable {

    private String host;
    private int port;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean running = false;
    private EventListener listener;
    public String name;
    private int id;
    public static boolean started = false;
    protected Keyboard input;
    protected GameStart gameStart;

    private List<Integer> moveCmd = new ArrayList<>();

    public Client(String host, int port, String name, Keyboard input) {
        this.host = host;
        this.port = port;
        this.name = name;
        this.input = input;
    }

    public String getName() {
        return name;
    }

    public void connect() {
        try {
            socket = new Socket(host,port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            listener = new EventListener(this);
            System.out.println("connect successfully!");
            new Thread(this).start();
        } catch (ConnectException e) {
            System.out.println("Unable to connect to the server!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            running = false;
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendObject(Object packet) {
        try {
            out.writeObject(packet);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
            running = true;
            while (running) {
                try {
                    Object data = in.readObject();
                    //if (data instanceof PlayerMove) System.out.println(((PlayerMove)data).getDirState().get(0));
                    listener.received(data);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }

    public void updateCmd(PlayerMove data) {
        List<Integer> tmp = data.getDirState();
        for(int i=0;i< Game.get_players();i++) {
            moveCmd.set(i,tmp.get(i));
        }

    }

    public List<Integer> getMoveCmd() {
        return moveCmd;
    }

    public void startGame(GameStart gameStart) {
        this.gameStart = gameStart;
        synchronized (Game.LOCK) {
            started = true;
            Game.LOCK.notifyAll();
        }
        for(int i=0;i< gameStart.getMx_players();i++) moveCmd.add(0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GameStart getGameStart() {
        return gameStart;
    }

    public boolean isStopped() {return !running;}
}

