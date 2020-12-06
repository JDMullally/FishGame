package model.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.games.PlayerAI;
import model.strategy.IStrategy;
import model.strategy.Strategy;
import model.tree.PlayerInterface;

public class ClientMain {
    private int port;
    private int numberPlayers;
    private String ip;

    public static void main(String[] args) throws IOException {
        parseArgsAndCreatePlayers(args);
    }

    private static void errorOut() {
        System.out.println("usage: ./xclient n [number between 1 and 10]  [port] [ip address] \n"
            + "or ./xclient n [number between 1 and 10] [port] with default ip");
        System.exit(0);
    }

    private static void parseArgsAndCreatePlayers(String[] args) throws IOException {
        int port = -1;
        int numberPlayers = 0;
        String ip = "";
        try {
            if (args.length >= 2) {
                numberPlayers = Integer.parseInt(args[0]);
                port = Integer.parseInt(args[1]);
            } else {
                errorOut();
            }
            if (args.length == 3) {
                ip = args[2];
            }
        } catch (Exception e) {
            errorOut();
        }
        createPlayers(port, numberPlayers, ip);
    }

    private static void createPlayers(int port, int numberPlayers, String ip) throws IOException {
        List<Client> clients = new ArrayList<>();
        for (int i = 0; i < numberPlayers; i++) {
            PlayerInterface player = new PlayerAI(new Strategy(), 2);
            Client newClient;
            if (ip.length() == 0) {
                newClient = new Client(port, player);
            } else {
                newClient = new Client(port, ip, player);
            }
            clients.add(newClient);
        }
    }
}
