package model.tournament;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import model.games.GameAction;
import model.games.IGameResult;
import model.games.IReferee;
import model.games.Referee;
import model.tree.PlayerInterface;

public class TournamentManager implements ManagerInterface {

  private final static int BOARD_ROWS = 4;
  private final static int BOARD_COLUMNS = 4;
  private final List<PlayerInterface> tournamentPlayers;

  private List<IGameResult> roundResults;
  private List<PlayerInterface> previousWinners;
  private List<PlayerInterface> remainingPlayers;
  private List<PlayerInterface> losingPlayers;
  private List<PlayerInterface> cheaters;

  /**
   * Constructor for TournamentManager that initializes organized lists of Players and sorts the
   * input list of tournamentPlayers.
   * @param tournamentPlayers
   */
  public TournamentManager(List<PlayerInterface> tournamentPlayers) {
    this.tournamentPlayers = orderByAge(tournamentPlayers);
    this.remainingPlayers = this.tournamentPlayers;
    this.losingPlayers = new ArrayList<>();
    this.cheaters = new ArrayList<>();
  }

  /**
   * Returns an ordered List of PlayerInterfaces ordered by age in ascending order.
   *
   * @param tournamentPlayers
   * @return List of PlayerInterface
   */
  private List<PlayerInterface> orderByAge(List<PlayerInterface> tournamentPlayers) {
    tournamentPlayers.sort(Comparator.comparing(PlayerInterface::getPlayerAge));
    return tournamentPlayers;
  }

  @Override
  public List<PlayerInterface> runTournament() {

    while (!isTournamentOver()) {
      this.roundResults = this.runRound();
      this.previousWinners = this.remainingPlayers;
      this.remainingPlayers = this.getRoundWinners();
    }

    return this.remainingPlayers;
  }

  public boolean isTournamentOver() {

    // over if there was a single final game
    if (this.roundResults.size() <= 1) {
      return true;
    }

    // over if players < 2
    if (this.remainingPlayers.size() < 2) {
      return true;
    }

    // over if all the previous round's winners are still present
    return this.remainingPlayers.containsAll(this.previousWinners);
  }

  private List<PlayerInterface> getRoundWinners() {
    List<PlayerInterface> roundWinners = new ArrayList<>();
    this.roundResults.forEach(gameResult -> roundWinners.addAll(gameResult.getWinners()));
    return roundWinners;
  }

  /**
   * Run a single round in the tournament. Returns a list of IGameResult that represent the results
   * of each game run in this round.
   *
   * @return a list of GameResult representing the outcome of all games run this round.
   */
  public List<IGameResult> runRound() {

    // assign to games
    List<List<PlayerInterface>> playerGroups = allocatePlayers(this.remainingPlayers);

    this.roundResults.clear();
    this.remainingPlayers.clear();

    for (List<PlayerInterface> group : playerGroups) {
      IReferee referee = new Referee(group, BOARD_ROWS, BOARD_COLUMNS);

      IGameResult gameResult = referee.runGame();

      this.remainingPlayers.addAll(gameResult.getWinners());
      this.losingPlayers.addAll(gameResult.getLosers());
      this.cheaters.addAll(gameResult.getCheaters());

      this.roundResults.add(gameResult);
    }

    return this.roundResults;
  }

  /**
   * Creates a list of Player Groups following the pattern below
   * 0 -> Err
   * 1 -> Err
   * 2 -> 2P
   * 3 -> 3P
   * 4 -> 4P
   * 5 -> 3P + 2P
   * 6 -> 3P + 3P
   * 7 -> 4P + 3P
   * 8 -> 4P + 4P
   * 9 -> 3P + 3P + 3P
   * 10 -> 4P + 3P + 3P
   * 11 -> 4P + 4P + 3P
   * 12 -> 4P + 4P + 4P
   * 13 -> 4P + 3P + 3P + 3P
   * 14 -> 4P + 4P + 3P + 3P
   * 15 -> 4P + 4P + 4P + 3P
   * 16 -> 4P + 4P + 4P + 4P
   * 17 -> 4P + 4P + 3P + 3P + 3P
   *
   * @param players
   * @return List of List of PlayerInterface
   */
  public List<List<PlayerInterface>> allocatePlayers(List<PlayerInterface> players) {
    return allocatePlayersHelper(players, new ArrayList<>());
  }

  /**
   * Helper function for allocatePlayers
   * @param players List of PlayerInterfaces remaining to allocate
   * @param acc Accumulator for already allocated players.
   * @return List of List of PlayerInterface
   */
  private List<List<PlayerInterface>> allocatePlayersHelper(List<PlayerInterface> players, List<List<PlayerInterface>> acc) {
    int size = players.size();
    if (size == 2 || size == 3 || size == 4) {
      acc.add(players);
      return acc;
    } else if (size == 5 || size == 6 || size == 9) {
      List<PlayerInterface> firstThree = players.subList(0, 3);
      acc.add(firstThree);
      return allocatePlayersHelper(players.subList(3, size), acc);
    } else {
      List<PlayerInterface> firstFour = players.subList(0, 4);
      acc.add(firstFour);
      return allocatePlayersHelper(players.subList(4, size), acc);
    }
  }

  @Override
  public List<GameAction> getOngoingActions() {
    return null;
  }

  @Override
  public List<PlayerInterface> getTournamentResults() throws IllegalStateException {
    return null;
  }

  @Override
  public List<IGameResult> getRoundResults() throws IllegalStateException {
    return null;
  }

  @Override
  public String getTournamentStatistics() {
    return null;
  }
}
