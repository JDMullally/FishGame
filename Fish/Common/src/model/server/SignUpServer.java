package model.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import model.tournament.ManagerInterface;
import model.tournament.PlayerStanding;
import model.tournament.TournamentManager;
import model.tree.PlayerInterface;

/**
 * Represents a Server that can sign up between 5 and 10 players and send them to a tournament
 * manager to run a Game of Fish.
 */
public class SignUpServer implements Server {

    private final int MAX_PLAYERS = 10;
    private final int MIN_PLAYERS = 5;
    private int TIMEOUT = 30;
    private final int SECOND = 1000;
    private ServerSocket server;
    private List<PlayerInterface> proxyPlayerList;

    /**
     *
     * @param port Port for clients to connect to.
     * @throws IOException if the connection fails
     */
    public SignUpServer(int port) throws IOException {
        this.proxyPlayerList = new ArrayList<>();
        this.server = new ServerSocket(port);
    }

    /**
     * Testing Constructor that takes in a timeout.
     * @param port Port for clients to connect to.
     * @param timeout a custom timeout that makes testing go much faster.
     * @throws IOException if the connection fails
     */
    public SignUpServer(int port, int timeout) throws IOException {
        this.proxyPlayerList = new ArrayList<>();
        this.server = new ServerSocket(port);
        this.TIMEOUT = timeout;
    }

    @Override
    public List<PlayerInterface> signUp() throws IOException {
        for (int i = 0; i < 2; i++) {
            if (this.proxyPlayerList.size() >= this.MIN_PLAYERS) {
                break;
            } else {
                while (this.proxyPlayerList.size() < MAX_PLAYERS) {
                    try {
                        this.server.setSoTimeout(TIMEOUT * SECOND);
                        Socket s = this.server.accept();
                        PlayerInterface clientProxy = new ClientProxy(s, i + 1);
                        this.proxyPlayerList.add(clientProxy);
                    } catch (SocketTimeoutException e) {
                        break;
                    }
                }
            }
        }
        for (PlayerInterface player : this.proxyPlayerList) {
            player.getPlayerID();
        }

        return new ArrayList<>(this.proxyPlayerList);
    }

    @Override
    public JsonArray sendToTournamentManager() throws IOException {
        if (this.proxyPlayerList.size() < 5) {
            System.out.println("Not enough players connected.  Shutting down");
            this.server.close();
            return null;
        }
        ManagerInterface tournament = new TournamentManager(this.proxyPlayerList);
        int winners = tournament.runTournament().size();
        int cheaters = tournament.getTournamentStatistics().get(PlayerStanding.CHEATER).size();
        JsonArray output = new JsonArray();
        output.add(winners);
        output.add(cheaters);
        this.server.close();
        return output;
    }


}
