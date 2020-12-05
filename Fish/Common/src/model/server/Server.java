package model.server;

import java.io.IOException;
import java.util.List;
import model.tree.PlayerInterface;


/**
 * Represents a Game Server that signs up players based on the Fish Remote Interaction Protocol and
 * runs a game using that
 */
public interface Server {

    /**
     * Called at the start of a tournament.  The server awaits TCP connections according to Remote
     * Interactions {https://www.ccs.neu.edu/home/matthias/4500-f20/remote.html}.
     *
     * It waits for some time (here, 30s) for at least a minimum number (here, five)
     * of remote clients to connect and be represented as ClientProxy players. As long as there is
     * not this minimum number of clients connected at the end of a waiting period, the server
     * re-enters the waiting state (here, exactly once).
     *
     * The waiting period ends if the server has accepted a maximal number (here, ten)
     * of client connections; this keeps the overall duration of a tournament to a reasonable
     * amount of time.
     *
     * If there are not a sufficient number of clients signed up at the end of the waiting period,
     * the server shuts down without running a tournament.
     */
    List<PlayerInterface> signUp() throws IOException;

    /**
     * When a sufficient number of clients are connected, the server signs them up with the manager
     * and asks the latter to run a complete tournament;
     *
     * When the manager’s work is done, it outputs the number of winners and cheaters as a Json Array
     * and the server shuts down.
     */
    void sendToTournamentManager() throws IOException;

}
