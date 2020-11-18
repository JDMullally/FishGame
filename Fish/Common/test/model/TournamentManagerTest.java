package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import model.games.PlayerAI;
import model.strategy.Strategy;
import model.tournament.PlayerStanding;
import model.tournament.TournamentManager;
import model.tree.PlayerInterface;
import org.junit.Test;

public class TournamentManagerTest {


  PlayerInterface p1, p2, p3, p4;

  List<PlayerInterface> players2, players3, players4;

  List<PlayerInterface> players13, players20;

  TournamentManager tournamentManager;


  void init() {
    this.p1 = new PlayerAI(new Strategy(), 2, 1, "Player 1");
    this.p2 = new PlayerAI(new Strategy(), 2, 2, "Player 2");
    this.p3 = new PlayerAI(new Strategy(), 2, 3, "Player 3");
    this.p4 = new PlayerAI(new Strategy(),2, 4, "Player 4");
    this.players2 = new ArrayList<>(Arrays.asList(p1, p2));
    this.players3 = new ArrayList<>(Arrays.asList(p3, p1, p2));
    this.players4 = new ArrayList<>(Arrays.asList(p2, p3, p1, p4));
    this.players13 = generatePlayers(13);
    this.players20 = generatePlayers(20);
  }

  private List<PlayerInterface> generatePlayers(int numPlayers) {
    List<PlayerInterface> players = new ArrayList<>();

    for (int i = 0; i < numPlayers; i++) {
      players.add(new PlayerAI(new Strategy(), 1, i + 1, "Player " + (i+1)));
    }

    return players;
  }

  /**
   *********************************************************************************************
   * Tests for Constructor
   *********************************************************************************************
   */

  @Test
  public void testConstructor2Players() {
    init();
    tournamentManager = new TournamentManager(players2);
    assertFalse(tournamentManager.isTournamentOver());

    Map<PlayerStanding, List<PlayerInterface>> stats = tournamentManager.getTournamentStatistics();

    assertEquals(0, stats.get(PlayerStanding.CHEATER).size());
    assertEquals(0, stats.get(PlayerStanding.ELIMINATED).size());
    assertEquals(this.players2, stats.get(PlayerStanding.REMAINING));
  }

  @Test
  public void testConstructor3PlayersOutOfOrder() {
    init();
    tournamentManager = new TournamentManager(players3);
    assertFalse(tournamentManager.isTournamentOver());

    Map<PlayerStanding, List<PlayerInterface>> stats = tournamentManager.getTournamentStatistics();

    assertEquals(0, stats.get(PlayerStanding.CHEATER).size());
    assertEquals(0, stats.get(PlayerStanding.ELIMINATED).size());
    assertEquals(generatePlayers(3), stats.get(PlayerStanding.REMAINING));
  }

  @Test
  public void testConstructor4PlayersOutOfOrder() {
    init();
    tournamentManager = new TournamentManager(players4);
    assertFalse(tournamentManager.isTournamentOver());

    Map<PlayerStanding, List<PlayerInterface>> stats = tournamentManager.getTournamentStatistics();

    assertEquals(0, stats.get(PlayerStanding.CHEATER).size());
    assertEquals(0, stats.get(PlayerStanding.ELIMINATED).size());
    assertEquals(generatePlayers(4), stats.get(PlayerStanding.REMAINING));
  }

}
