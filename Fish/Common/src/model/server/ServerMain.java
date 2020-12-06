package model.server;

import java.io.IOException;

/**
 * Main class for runnable that follows remote protocol
 */
public class ServerMain {

    public static void main(String[] args) throws IOException {
        startServer(args);
    }

    private static void startServer(String[] args) throws IOException {
        int port = -1;

        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e) {
            System.out.println("usage: ./xserver <port number>");
            System.exit(0);
        }
        Server server = new SignUpServer(port);
        server.signUp();
        server.sendToTournamentManager();
    }

}
