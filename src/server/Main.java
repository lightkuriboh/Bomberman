package server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Input port value:");
            int port = Integer.parseInt(br.readLine());
            System.out.println("Input number of players:");
            int mx = Integer.parseInt(br.readLine());

            Server server = new Server(port, mx);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
