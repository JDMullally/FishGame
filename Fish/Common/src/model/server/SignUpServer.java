package model.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import model.games.IGameResult;
import model.tournament.ManagerInterface;
import model.tournament.PlayerStanding;
import model.tournament.TournamentManager;
import model.tree.PlayerInterface;

public class SignUpServer {

    private static final int PORT = 4567;
    private final int  MAX_PLAYERS = 10;
    private final int MIN_PLAYERS = 5;
    private final int timeout = 30000;
    private final int infiniteTimout = 0;
    private ServerSocket server;
    private List<PlayerInterface> proxyPlayerList;

    private SignUpServer(int port) throws IOException {
        this.proxyPlayerList = new ArrayList<>();
        this.server = new ServerSocket(port);
        this.signUp();
        List<PlayerInterface> result = this.sendToTournamentManager();
    }

    private void signUp() throws IOException {
        int count = 0;
        int repeat = 0;
        while (repeat < 2) {
            this.server.setSoTimeout(this.timeout);
            try {
                if (proxyPlayerList.size() < MAX_PLAYERS) {
                    Socket s = this.server.accept();
                    DataInputStream dis = new DataInputStream(s.getInputStream());
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());
                    PlayerInterface clientProxy = new ClientProxy(s, dis, dos, count + 1);
                    count++;
                    this.proxyPlayerList.add(clientProxy);
                } else {
                    break;
                }
            } catch (SocketTimeoutException e) {
                if (repeat > 0 && proxyPlayerList.size() < MIN_PLAYERS) {
                    System.out.println("Not enough players connected");
                    return;
                }
            }
            repeat++;
        }
        this.server.setSoTimeout(this.infiniteTimout);
    }

    private List<PlayerInterface> sendToTournamentManager() {
        ManagerInterface tournament = new TournamentManager(this.proxyPlayerList);
        List<PlayerInterface> winners = tournament.runTournament();
        tournament.getTournamentStatistics().get(PlayerStanding.CHEATER);
        System.out.println(winners.size());
        return winners;
    }


}
