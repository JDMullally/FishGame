package model;

import java.io.IOException;
import model.server.Server;
import model.server.SignUpServer;
import org.junit.Test;

public class ClientServerTest {

    private Server server;
    private int port;

    private void initTournament() throws IOException {
        this.port = 444444;
        this.server = new SignUpServer(this.port);
    }


    @Test
    public void runFullTournamentWithCheater() {

    }

    @Test
    public void runFullTournament() throws IOException {
        this.initTournament();
    }

    @Test void runFullTournamentAllCheaters() {

    }
}
