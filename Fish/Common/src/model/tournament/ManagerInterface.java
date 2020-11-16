package model.tournament;

import java.util.List;
import java.util.Map;
import model.games.GameAction;
import model.games.IGameResult;
import model.state.IPlayer;
import model.tree.PlayerInterface;

/**
 * The Interface of a Tournament Manager.
 * <p>
 * The tournament manager signs up players for tournaments, allocates players to games, creates
 * referees to run games, and collects tournament statistics. It also informs a tournament observer
 * of on-going actions.
 * <p>
 * Externally, a tournament manager should allow: - Players to sign up to a tournament - Referees to
 * report the results of a game and the current ongoing actions of a game - Observers to get those
 * current ongoing actions - Players and Observers to get the results of a round, the results of the
 * entire tournament, and the tournament statistics.
 */
public interface ManagerInterface {

  /**
   * Run an entire tournament with the players that are already signed up. Returns a list of all
   * winners at the end of the tournament. Advances through all rounds automatically.
   *
   * @return a list of PlayerInterface objects, representing winners of the tournament
   */
  List<PlayerInterface> runTournament();

  /**
   * Called only by an Observer or Player Allows an observer to see the placements of all players of
   * a given tournament.
   *
   * @return List of IGameAction
   * @throws IllegalStateException if the tournament isn't over yet
   */
  List<PlayerInterface> getTournamentResults() throws IllegalStateException;

  /**
   * Called only by an Observer or Player Allows an observer to see the results of a given
   * tournament round. All tournaments start at Round 1.
   *
   * @param round integer representing round n.
   * @return List of IGameResult
   * @throws IllegalStateException if the round provided in the tournament isn't over yet
   */
  List<IGameResult> getRoundResults(int round) throws IllegalStateException;

  /**
   * Called only by an Observer or Player Allows an observer to see the current statistics of the
   * tournament.
   *
   * @return Map of PlayerStanding Enumerators and a List of PlayerInterfaces
   */
  Map<PlayerStanding,List<PlayerInterface>> getTournamentStatistics();
}
