package model.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import model.games.PlayerAI;
import model.strategy.Strategy;
import model.tree.PlayerInterface;

public class ClientMain {

    public static void main(String[] args) throws IOException {
        int port = -1;
        int numberPlayers = 0;

        /*
        try {
            port = Integer.getInteger(args[0]);
            ip  = args[1];
            numberPlayers = Integer.getInteger(args[2]);



        } catch (Exception e) {
            System.out.println("usage: ./xserver <port number>");
            System.exit(0);
        }
        */
        List<Client> clients = new ArrayList<>();
        for (int i = 0; i < 6; i++) {

            InetAddress ip = InetAddress.getByName("localhost");

            PlayerInterface player = new PlayerAI(new Strategy(), 2);

            Client newClient = new Client(5056, player);

            clients.add(newClient);
        }
        System.out.println(clients);


    }
}
