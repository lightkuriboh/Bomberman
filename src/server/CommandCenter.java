package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommandCenter implements Runnable {

    private Server server;
    private boolean running = false;
    public CommandCenter(Server server) {
        this.server = server;
    }

    public void start() {new Thread(this).start();}

    @Override
    public void run() {
        running = true;
        String start="";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Input \'y\' to start the game!");
        while (!start.equals("y")) {
            try{
                start = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        server.sendDataToAll();
        while (running) {
            try {
                Thread.sleep(1);
                server.sendCmdToAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
