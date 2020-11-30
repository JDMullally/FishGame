package model.tournament;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import model.games.IGameResult;
import model.games.IReferee;
import model.games.Referee;
import model.tree.PlayerInterface;

public class TournamentManager implements ManagerInterface {

  private final static int BOARD_ROWS = 5;
  private final static int BOARD_COLUMNS = 5;
  private final static int FISH = 2;
  private final Map<Integer, List<IGameResult>> roundResultMap;
  private final List<PlayerInterface> eliminatedPlayers;
  private final List<PlayerInterface> cheaters;
  private final int timeout;
  private int round;
  private List<PlayerInterface> previousWinners;
  private List<PlayerInterface> remainingPlayers;

  /**
   * Constructor for TournamentManager that initializes organized lists of Players and sorts the
   * input list of tournamentPlayers.
   *
   * @param tournamentPlayers
   */
  public TournamentManager(List<PlayerInterface> tournamentPlayers) {
    this.round = 0;
    this.roundResultMap = new HashMap<>();
    this.previousWinners = new ArrayList<>();
    this.timeout = 1;
    this.remainingPlayers = this.orderByAge(new ArrayList<>(tournamentPlayers));
    this.eliminatedPlayers = new ArrayList<>();
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
    this.informPlayers(this.remainingPlayers, PlayerInterface::tournamentHasStarted);

    while (!isTournamentOver()) {
      this.runRound();
    }
    this.informPlayers(this.remainingPlayers, player -> player.tournamentResults(true));

    return this.remainingPlayers;
  }

  /**
   * Returns true if the tournament is over, false otherwise.
   *
   * @return boolean that is true if the tournament is over
   */
  public boolean isTournamentOver() {

    // over if players < 2
    if (this.remainingPlayers.size() < 2) {
      return true;
    } else if (this.round == 0) { // not over if first round and no games yet
      return false;
    } else if (this.round > 0 && this.roundResultMap.get(this.round).size() <= 1) {
      // over if there is was single final game
      return true;
    }

    // over if all the previous round's winners are still present
    return this.remainingPlayers.containsAll(this.previousWinners);
  }

  /**
   * Inform all players of an update to the tournament status. Takes in a list of players to inform
   * and a function that takes a player and returns a boolean representing whether the player
   * received the information.
   *
   * @param players the players to inform
   * @param playerTask the update to give to the players
   */
  private void informPlayers(List<PlayerInterface> players,
      Function<PlayerInterface, Boolean> playerTask) {
    List<PlayerInterface> playersCopy = new ArrayList<>(players);
    for (PlayerInterface player : playersCopy) {
      this.informPlayer(player, () -> playerTask.apply(player));
    }
  }

  /**
   * Removes a Player from the pool of remaining players if they do not respond to a message from
   * the tournament. Those removed players are added to a list of cheaters.
   *
   * @param player PlayerInterface
   * @param task   Callable created by the Tournament that returns true if the player receives the
   *               message.
   */
  private void informPlayer(PlayerInterface player, Callable<Boolean> task) {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Future<Boolean> future = executor.submit(task);

    Boolean response = false;

    try {
      response = future.get(this.timeout, TimeUnit.SECONDS);
      executor.shutdown();
    } catch (Exception e) {
      executor.shutdownNow();
    }

    if (!response) {
      if(!this.cheaters.contains(player)) {
        this.cheaters.add(player);
      }
      this.remainingPlayers.remove(player);
      this.eliminatedPlayers.remove(player);
    }
  }

  /**
   * Run a single round in the tournament. It uses allocatePlayers to create the groups of players
   * to put in each game, and runs each group with runGame. Returns a list of IGameResult that
   * represent the results of each game run in this round. It also updates the round number, the
   * previous round's winners adds the list of game results to the map of all round results.
   *
   * @return a list of GameResult representing the outcome of all games run this round.
   */
  public List<IGameResult> runRound() {
    this.round++;
    this.previousWinners = this.remainingPlayers;
    // assign to games
    List<List<PlayerInterface>> playerGroups = this.allocatePlayers();
    // Run rounds and add to map of all round results
    List<IGameResult> roundResults = new ArrayList<>();
    for (List<PlayerInterface> group : playerGroups) {
      roundResults.add(this.runGame(group));
    }
    this.roundResultMap.put(round, roundResults);
    this.remainingPlayers.sort(Comparator.comparing(PlayerInterface::getPlayerAge));
    this.eliminatedPlayers.sort(Comparator.comparing(PlayerInterface::getPlayerAge));
    this.cheaters.sort(Comparator.comparing(PlayerInterface::getPlayerAge));
    return roundResults;
  }

  /**
   * Runs a single game in the tournament. Adds the IGameResult from that game to the current
   * round's results and adds all winners back to remaining players along with keeping track of
   * eliminated and cheating players.
   *
   * @param group List of PlayerInterface
   * @return IGameResult
   */
  public IGameResult runGame(List<PlayerInterface> group) {

    // Inform players game has begun
    this.informPlayers(group, PlayerInterface::gameHasStarted);

    // Run the game
    IReferee referee = new Referee(group, BOARD_ROWS, BOARD_COLUMNS, FISH);
    IGameResult gameResult = referee.runGame();

    // Inform player game is over
    this.informPlayers(group, player -> {
      player.gameResults(gameResult);
      return true;
    });

    List<PlayerInterface> winners = gameResult.getWinners();
    List<PlayerInterface> eliminated = gameResult.getEliminated();
    List<PlayerInterface> cheaters = gameResult.getCheaters();

    // Update fields
    this.remainingPlayers.addAll(orderByAge(winners));
    this.eliminatedPlayers.addAll(eliminated);
    this.cheaters.addAll(cheaters);

    // Inform eliminated players
    this.informPlayers(eliminated, player ->
        player.tournamentResults(false));

    this.informPlayers(cheaters, player -> {
      player.kickedForCheating();
      return true;
    });

    return gameResult;
  }

  /**
   * This method clears the list of remaining players  and The manager starts by assigning them to
   * games with the maximal number of participants permitted in ascending order of age. Once the
   * number of remaining players drops below the maximal number and can’t form a game, the manager
   * backtracks by one game and tries games of size one less than the maximal number and so on until
   * all players are assigned. The pattern for this is demonstrated below.
   *
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
   * @return List of List of PlayerInterface
   */
  public List<List<PlayerInterface>> allocatePlayers() {
    List<List<PlayerInterface>> groups = allocatePlayersHelper(this.remainingPlayers,
        new ArrayList<>());
    this.remainingPlayers = new ArrayList<>();
    return groups;
  }

  /**
   * Helper function for allocatePlayers
   *
   * @param players List of PlayerInterfaces remaining to allocate
   * @param acc     Accumulator for already allocated players.
   * @return List of List of PlayerInterface
   */
  private List<List<PlayerInterface>> allocatePlayersHelper(List<PlayerInterface> players,
      List<List<PlayerInterface>> acc) {
    int size = players.size();
    if (size < 2) {
      throw new IllegalArgumentException("Cannot run tournament with less than 2 players.");
    }
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

  /**
   * Returns the current round number. Used for testing.
   *
   * O represents that no rounds have been run.
   *
   * @return the round number.
   */
  public int getRound() {
    return this.round;
  }

  @Override
  public List<PlayerInterface> getTournamentResults() throws IllegalStateException {
    if (isTournamentOver()) {
      return new ArrayList<>(this.remainingPlayers);
    } else {
      throw new IllegalStateException("Tournament is not over yet!");
    }
  }

  @Override
  public List<IGameResult> getRoundResults(int round) throws IllegalArgumentException {
    if (round < 1 || round > this.round) {
      throw new IllegalArgumentException("Cannot ask for results before Game has started or "
          + "results that don't exist");
    }
    return new ArrayList<>(this.roundResultMap.get(round));
  }

  @Override
  public Map<PlayerStanding, List<PlayerInterface>> getTournamentStatistics() {
    Map<PlayerStanding, List<PlayerInterface>> map = new HashMap<>();
    map.put(PlayerStanding.CHEATER, new ArrayList<>(this.cheaters));
    map.put(PlayerStanding.ELIMINATED, new ArrayList<>(this.eliminatedPlayers));
    map.put(PlayerStanding.REMAINING, new ArrayList<>(this.remainingPlayers));
    return map;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Total rounds run: ").append(this.round).append("\n");
    sb.append("Cheaters: \n");
    for (PlayerInterface cheater : cheaters) {
      sb.append(cheater.getPlayerID()).append("\n");
    }
    sb.append("Eliminated: \n");
    for (PlayerInterface eliminated : eliminatedPlayers) {
      sb.append(eliminated.getPlayerID()).append("\n");
    }
    if (isTournamentOver()) {
      sb.append("Winners: \n");
    } else {
      sb.append("Remaining: \n");
    }
    for (PlayerInterface remaining : remainingPlayers) {
      sb.append(remaining.getPlayerID()).append("\n");
    }
    return sb.toString();
  }
}
