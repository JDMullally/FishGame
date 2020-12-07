package model;

import com.google.gson.JsonArray;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import model.games.PlayerAI;
import model.server.Client;
import model.server.Server;
import model.server.SignUpServer;
import model.state.Player;
import model.strategy.Strategy;
import model.tree.PlayerInterface;
import org.junit.Test;

public class ClientTest {

    private Client client;

    private void initTournament() throws IOException {
        this.client = new Client(new PlayerAI(new Strategy(), 2));
    }


    @Test
    public void runFullTournamentWithCheater() throws IOException {
        initTournament();
        List<Client> clients = new ArrayList<>();
        this.server.signUp();
        for (int i = 0; i < 10; i++) {
            Client client = new Client(new PlayerAI(new Strategy(), 2));
            clients.add(client);
        }
        JsonArray array = this.server.sendToTournamentManager();
        System.out.println(array);
    }

    @Test
    public void runFullTournament() throws IOException {
        //this.initTournament();
    }

    @Test
    public void runFullTournamentAllCheaters() {

    }
}
