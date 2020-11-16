package model.tournament;

import java.util.ArrayList;
import java.util.List;
import model.games.GameAction;
import model.games.IGameResult;
import model.tree.PlayerInterface;

public class TournamentManager implements ManagerInterface {

  private final List<PlayerInterface> tournamentPlayers;

  private List<IGameResult> lastRoundResults;
  private List<PlayerInterface> winners;

  public TournamentManager(List<PlayerInterface> tournamentPlayers) {

    this.tournamentPlayers = tournamentPlayers;
  }

  @Override
  public List<PlayerInterface> runTournament() {
    while (something) {
      List<IGameResult> roundResults = this.runRound();
    }

    // calculate winners

    return winners;
  }

  /**
   * Run a single round in the tournament. Returns a list of IGameResult that represent the results
   * of each game run in this round.
   *
   * @return a list of GameResult representing the outcome of all games run this round.
   */
  public List<IGameResult> runRound() {
    // sort by age

    // assign to games
    return null;
  }

  private List<List<PlayerInterface>> allocatePlayers(List<PlayerInterface> players) {

    int remainder = players.size() % 4;

    switch (remainder) {
      case 0:
        // easy
        break;
      case 1:
        // send 5 players to function
        break;
      case 2:
        // send 6 players to function
        break;
      case 3:
        // send 3 players to function
        break;
    }

    List<List<PlayerInterface>> playerGroups = new ArrayList<>();

    for (PlayerInterface p : players) {
      playerGroups.add()
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
