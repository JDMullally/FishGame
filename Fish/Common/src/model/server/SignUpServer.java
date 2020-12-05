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

public class SignUpServer implements Server {

    private static final int PORT = 4567;
    private final int MAX_PLAYERS = 10;
    private final int MIN_PLAYERS = 5;
    private final int timeout = 30000;
    private final int infiniteTimout = 0;
    private ServerSocket server;
    private List<PlayerInterface> proxyPlayerList;

    public SignUpServer(int port) throws IOException {
        this.proxyPlayerList = new ArrayList<>();
        this.server = new ServerSocket(port);
    }

    @Override
    public List<PlayerInterface> signUp() throws IOException {
        for (int i = 0; i < 6; i++) {
        Socket s = this.server.accept();
        PlayerInterface clientProxy = new ClientProxy(s, i + 1);
        this.proxyPlayerList.add(clientProxy);
        }
        return new ArrayList<>(this.proxyPlayerList);
    }

    @Override
    public void sendToTournamentManager() throws IOException {
        Gson gson = new Gson();
        ManagerInterface tournament = new TournamentManager(this.proxyPlayerList);
        int winners = tournament.runTournament().size();
        int cheaters = tournament.getTournamentStatistics().get(PlayerStanding.CHEATER).size();
        JsonArray output = new JsonArray();
        output.add(winners);
        output.add(cheaters);
        System.out.println(gson.toJson(output));
        this.server.close();
    }


}
