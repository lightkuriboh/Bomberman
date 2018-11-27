package server;

import signal.AssignId;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class Connection implements  Runnable{

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private int id;
    private Server server;
    private boolean idAssigned = false;
    private ServerListener serverListener;
    private boolean closed = false;

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
                try {
                    Object data = in.readObject();
                    serverListener.handle(id, data);
                    //System.out.println((String)data);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SocketException e) {
                    close();
                    break;
                }
            }

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (closed) return;
        try {
            in.close();
            out.close();
            closed = true;
            socket.close();
            idAssigned = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendObject(Object packet) {
        if (closed) return;
        try {
            out.writeObject(packet);
            out.flush();
        } catch (IOException e) {
            return;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdAssigned(boolean idAssigned) {
        this.idAssigned = idAssigned;
    }

    public boolean assigned() {return idAssigned;}
}
