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
import java.util.concurrent.TimeoutException;
import model.games.IGameResult;
import model.games.IReferee;
import model.games.Referee;
import model.tree.PlayerInterface;

public class TournamentManager implements ManagerInterface {

  private final static int BOARD_ROWS = 4;
  private final static int BOARD_COLUMNS = 4;
  private final List<PlayerInterface> tournamentPlayers;

  private int round;
  private Map<Integer, List<IGameResult>> roundResultMap;
  private List<IGameResult> roundResults;
  private List<PlayerInterface> previousWinners;
  private List<PlayerInterface> remainingPlayers;
  private List<PlayerInterface> eliminatedPlayers;
  private List<PlayerInterface> cheaters;
  private final int timeout;

  /**
   * Constructor for TournamentManager that initializes organized lists of Players and sorts the
   * input list of tournamentPlayers.
   * @param tournamentPlayers
   */
  public TournamentManager(List<PlayerInterface> tournamentPlayers) {
    this.tournamentPlayers = orderByAge(tournamentPlayers);
    this.round = 0;
    this.roundResultMap = new HashMap<>();
    this.roundResults = new ArrayList<>();
    this.previousWinners = new ArrayList<>();
    this.timeout = 5;
    this.remainingPlayers = this.tournamentPlayers;
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
    this.tournamentHasBegun(this.remainingPlayers);

    while (!isTournamentOver()) {
      round++;
      this.previousWinners = this.remainingPlayers;
      this.roundResults = this.runRound();
      this.roundResultMap.put(this.round, this.roundResults);
    }
    this.tournamentInform(this.remainingPlayers, true);

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
    } else if (this.round > 0 && this.roundResults.size() <= 1) { // Check for single final game
      return true;
    }

    // over if all the previous round's winners are still present
    return this.remainingPlayers.containsAll(this.previousWinners);
  }

  /**
   * Called at the start of a tournament. If those Players do not respond, they are removed from
   * the game and added to the list of cheaters.
   */
  public void tournamentHasBegun(List<PlayerInterface> players) {
    for (PlayerInterface player: players) {

        Callable<Boolean> task = new Callable<Boolean>() {
          public Boolean call() throws TimeoutException {
            return player.tournamentHasStarted();
          }
        };

        this.informPlayer(player, task);
    }
  }

  /**
   * Called at the start of a game. If those Players do not respond, they are removed from
   * the game and added to the list of cheaters
   * @param players
   */
  private void gameHasBegun(List<PlayerInterface> players) {
    for (PlayerInterface player: players) {

      Callable<Boolean> task = new Callable<Boolean>() {
        public Boolean call() throws TimeoutException {
          return player.gameHasStarted();
        }
      };

      this.informPlayer(player, task);
    }
  }

  /**
   * Called when the results for players are ready or the tournament ends. If those Players do not
   * respond, they are removed from the pool of winners and added to the list of cheaters.
   *
   * @param players List of players that need to be informed
   * @param win Boolean that is true or false depending on if the players have won.
   */
  public void tournamentInform(List<PlayerInterface> players, boolean win) {
    for (PlayerInterface player: players) {

      Callable<Boolean> task = new Callable<Boolean>() {
        public Boolean call() throws TimeoutException {
          return player.tournamentResults(win);
        }
      };

      this.informPlayer(player, task);
    }
  }

  //TODO use a lambda function to run all of the callables.
  /**
   * Informs the givens players the results of their most recent game.
   *
   * @param players PlayerInterface
   * @param result IGameResult which contains the winners, losers and cheaters in the Game.
   */
  public void gameInform(List<PlayerInterface> players, IGameResult result) {
    for (PlayerInterface player: players) {

      Callable<Boolean> task = new Callable<Boolean>() {
        public Boolean call() throws TimeoutException {
            player.gameResults(result);
            return true;
        }
      };

      this.informPlayer(player, task);
    }
  }


  /**
   * Removes a Player from the pool of remaining players if they do not respond to a message from
   * the tournament. Those removed players are added to a list of cheaters.
   *
   * @param player PlayerInterface
   * @param task Callable created by the Tournament that returns true if the player receives
   *             the message.
   */
  private void informPlayer(PlayerInterface player, Callable<Boolean> task) {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Future<Boolean> future = executor.submit(task);

    // allow the player to move based on their strategy
    try {
      Boolean response = future.get(this.timeout, TimeUnit.SECONDS);
      if(!response) {
        this.cheaters.add(player);
        player.kickedForCheating();
        this.remainingPlayers.remove(player);
        this.eliminatedPlayers.remove(player);
      }
      executor.shutdownNow();
    } catch (Exception e) {
      executor.shutdownNow();
      this.cheaters.add(player);
      player.kickedForCheating();
      this.remainingPlayers.remove(player);
      this.eliminatedPlayers.remove(player);
    }
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
      IGameResult gameResult = runGame(group);
      this.roundResults.add(gameResult);
    }

    return this.roundResults;
  }

  /**
   * Runs a single game in the tournament. Returns the IGameResult from that game and adds all
   * winners back to remaining players along with keeping track of eliminated and cheating players.
   *
   * @param group List of PlayerInterface
   * @return IGameResult
   */
  public IGameResult runGame(List<PlayerInterface> group) {
    IReferee referee = new Referee(group, BOARD_ROWS, BOARD_COLUMNS);
    this.gameHasBegun(group);
    IGameResult gameResult = referee.runGame();
    List<PlayerInterface> eliminated = gameResult.getEliminated();
    this.remainingPlayers.addAll(gameResult.getWinners());
    this.eliminatedPlayers.addAll(eliminated);
    this.cheaters.addAll(gameResult.getCheaters());
    this.tournamentInform(eliminated, false);
    return gameResult;
  }


  /**
   * The manager starts by assigning them to games with the maximal number of participants permitted
   * in ascending order of age. Once the number of remaining players drops below the maximal number
   * and can’t form a game, the manager backtracks by one game and tries games of size one less than
   * the maximal number and so on until all players are assigned.
   * The pattern for this is demonstrated below.
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


  @Override
  public List<PlayerInterface> getTournamentResults() throws IllegalStateException {
    if(isTournamentOver())  {
      return new ArrayList<>(this.remainingPlayers);
    } else {
      throw new IllegalStateException("Tournament is not over yet!");
    }
  }

  @Override
  public List<IGameResult> getRoundResults(int round) throws IllegalArgumentException {
    if(round < 1 || round > this.round)  {
      throw new  IllegalArgumentException("Cannot ask for results before Game has started or "
          + "results that don't exist");
    }
    return new ArrayList<>(this.roundResultMap.get(round));
  }

  @Override
  public Map<PlayerStanding,List<PlayerInterface>> getTournamentStatistics() {
    Map<PlayerStanding,List<PlayerInterface>> map = new HashMap<>();
    map.put(PlayerStanding.CHEATER, new ArrayList<>(this.cheaters));
    map.put(PlayerStanding.ELIMINATED, new ArrayList<>(this.eliminatedPlayers));
    map.put(PlayerStanding.REMAINING, new ArrayList<>(this.remainingPlayers));
    return map;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Total rounds run: " + this.round  + "\n");
    sb.append("Cheaters: \n");
    for (PlayerInterface cheater: cheaters) {
      sb.append(cheater.getPlayerID() + "\n");
    }
    sb.append("Eliminated: \n");
    for (PlayerInterface eliminated: eliminatedPlayers) {
      sb.append(eliminated.getPlayerID() + "\n");
    }
    if (isTournamentOver()) {
      sb.append("Winners: \n");
    } else {
      sb.append("Remaining: \n");
    }
    for (PlayerInterface remaining: remainingPlayers) {
      sb.append(remaining.getPlayerID() + "\n");
    }
    return sb.toString();
  }
}
