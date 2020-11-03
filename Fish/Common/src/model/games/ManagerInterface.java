package model.games;

import java.util.List;
import model.state.IPlayer;

/**
 * The Interface of a Tournament Manager.
 *
 * The tournament manager signs up players for tournaments, allocates players to games,
 * creates referees to run games, and collects tournament statistics. It also informs a tournament
 * observer of on-going actions.
 *
 * Externally, a tournament manager should allow:
 * - Players to sign up to a tournament
 * - Referees to report the results of a game and the current ongoing actions of a game
 * - Observers to get those current ongoing actions
 * - Players and Observers to get the results of a round, the results of the entire tournament, and
 * the tournament statistics.
 */
public interface ManagerInterface {

    /**
     * Called only by a Player.
     * This method would allow any number of Players to sign up to the tournament in
     * a specified time frame.
     * Returns true if a player was able to sign up to a Tournament and false otherwise.
     *
     * @param player IPlayer entering
     * @return boolean
     */
    boolean signUp(IPlayer player);

    /**
     * Called only by a Referee.
     * Allows the Referee to report game results including placements and cheating players.
     *
     * @param result IGameResult
     */
    void reportGameResult(IGameResult result);

    /**
     * Called only by a Referee.
     * Allows a Referee to report an ongoing EGameAction to the Tournament Manager.
     *
     * @param gameAction IGameAction
     */
    void reportOngoingAction(GameAction gameAction);

    /**
     * Called only by an Observer.
     * Allows an Observer to see the most recent ongoing GameActions in all games.
     *
     * @return List of IGameAction
     */
    List<GameAction> getOngoingActions();

    /**
     * Called only by an Observer or Player
     * Allows an observer to see the placements of all players of a given tournament.
     *
     * @return List of IGameAction
     * @throws IllegalStateException if the tournament isn't over yet
     */
    List<IPlayer> getTournamentResults() throws IllegalStateException;

    /**
     * Called only by an Observer or Player
     * Allows an observer to see the results of a given tournament round.
     *
     * @return List of IGameResult
     * @throws IllegalStateException if the round provided in the tournament isn't over yet
     */
    List<IGameResult> getRoundResults() throws IllegalStateException;

    /**
     * Called only by an Observer or Player
     * Allows an observer to see the current statistics of the tournament.
     *
     * @return String
     */
    String getTournamentStatistics();
}
