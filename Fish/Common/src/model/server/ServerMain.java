package model.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import java.io.IOException;

/**
 * Main class for server runnable that follows remote protocol depicted here:
 * {https://www.ccs.neu.edu/home/matthias/4500-f20/remote.html}
 */
public class ServerMain {
    // Main class
    public static void main(String[] args) throws IOException {
        startServer(args);
    }

    /**
     * Reads the arguments, opens the server, connects all players with the signUp method and runs
     * the tournament with the sendToTournamentManager method. At the end, the result from send to
     * Tournament Manager is printed using the Gson Library.
     *
     * @param args command line arguments
     * @throws IOException
     */
    private static void startServer(String[] args) throws IOException {
        int port = -1;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e) {
            errorOut();
        }
        if (port <= 0) {
            errorOut();
        }
        Server server = new SignUpServer(port);
        server.signUp();
        Gson gson = new Gson();
        JsonArray output = server.sendToTournamentManager();
        System.out.println(gson.toJson(output));

    }

    /**
     * Prints custom error message and closes the application.
     */
    private static void errorOut() {
        System.out.println("usage: ./xserver <port number>");
        System.exit(0);
    }

}
