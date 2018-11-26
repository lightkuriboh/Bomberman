package server;

import signal.AssignId;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection implements  Runnable{

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private int id;
    private Server server;
    private boolean idAssigned = false;
    private ServerListener serverListener;

    public Connection(Socket socket, Server server, ServerListener serverListener) {
        this.socket = socket;
        id = -1;
        this.server = server;
        this.serverListener = serverListener;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (socket.isConnected()) {
                System.out.println();
                if (!idAssigned) {
                    AssignId data = new AssignId(id);
                    sendObject(data);
                    idAssigned = true;
                }
                try {
                    Object data = in.readObject();
                    serverListener.handle(id, data);
                    //System.out.println((String)data);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
        close();
    }

    public void close() {
        try {
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
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
