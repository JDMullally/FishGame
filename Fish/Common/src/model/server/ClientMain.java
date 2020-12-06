package model.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.games.PlayerAI;
import model.strategy.IStrategy;
import model.strategy.Strategy;
import model.tree.PlayerInterface;

/**
 * Main class for client runnable that follows remote protocol depicted here:
 * {https://www.ccs.neu.edu/home/matthias/4500-f20/remote.html}
 */
public class ClientMain {
    //Main method
    public static void main(String[] args) throws IOException {
        parseArgsAndCreatePlayers(args);
    }

    /**
     * Prints custom error message and closes the application.
     */
    private static void errorOut() {
        System.out.println("usage: ./xclient n [number between 1 and 10]  [port] [ip address] \n"
            + "or ./xclient n [number between 1 and 10] [port] with default ip");
        System.exit(0);
    }

    /**
     * Parses the command line arguments and checks them to see if they are valid. Once the arguments
     * are parsed, createPlayers is immediately called.
     * @param args command line arguments.
     * @throws IOException if connection is lost
     */
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
        if (numberPlayers <= 0 || port < 0) {
            errorOut();
        }
        createPlayers(port, numberPlayers, ip);
    }

    /**
     * Creates players on the client side that respond to messages from the tournament.
     *
     * @param port the port number to connect to.
     * @param numberPlayers the number of players connecting
     * @param ip the ip they are connecting to. If the ip is an empty string, connect to 127.0.0.1
     * @throws IOException if connection is lost
     */
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
