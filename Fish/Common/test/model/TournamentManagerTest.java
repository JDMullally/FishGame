package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.games.IGameResult;
import model.games.PlayerAI;
import model.strategy.Strategy;
import model.strategy.testStrategies.MoveAnotherPlayerPenguin;
import model.strategy.testStrategies.MoveOutsideBoard;
import model.strategy.testStrategies.PlaceOutsideBoard;
import model.strategy.testStrategies.PlacePenguinOnAnotherPlayerPenguin;
import model.strategy.testStrategies.TimeoutStrategy;
import model.tournament.PlayerStanding;
import model.tournament.TournamentManager;
import model.tree.PlayerInterface;
import org.junit.Test;

public class TournamentManagerTest {

  PlayerInterface p1, p2, p3, p4, p5;
  PlayerInterface cheater1, cheater2, cheater3, cheater4;
  PlayerInterface timeout1;

  List<PlayerInterface> players2, players3, players4;
  List<PlayerInterface> players13, players20;
  List<PlayerInterface> players1cheater4normal, players4cheaters, players4cheaters1normal;
  List<PlayerInterface> players1timeoutStrategy4normal;

  TournamentManager tournamentManager;


  void init() {
    this.p1 = new PlayerAI(new Strategy(), 2, 1, "Player 1");
    this.p2 = new PlayerAI(new Strategy(), 2, 2, "Player 2");
    this.p3 = new PlayerAI(new Strategy(), 2, 3, "Player 3");
    this.p4 = new PlayerAI(new Strategy(), 2, 4, "Player 4");
    this.p5 = new PlayerAI(new Strategy(), 2, 5, "Player 5");
    this.players2 = new ArrayList<>(Arrays.asList(p1, p2));
    this.players3 = new ArrayList<>(Arrays.asList(p3, p1, p2));
    this.players4 = new ArrayList<>(Arrays.asList(p2, p3, p1, p4));
    this.players13 = generatePlayers(13);
    this.players20 = generatePlayers(20);

    this.cheater1 = new PlayerAI(new PlaceOutsideBoard(), 2, 1, "Cheater 1");
    this.cheater2 = new PlayerAI(new MoveAnotherPlayerPenguin(), 2, 2, "Cheater 2");
    this.cheater3 = new PlayerAI(new MoveOutsideBoard(), 2, 3, "Cheater 3");
    this.cheater4 = new PlayerAI(new PlacePenguinOnAnotherPlayerPenguin(), 2, 4, "Cheater 4");

    this.players1cheater4normal = new ArrayList<>(Arrays.asList(p2, p3, p1, p5, cheater4));
    this.players4cheaters = new ArrayList<>(Arrays.asList(cheater1, cheater2, cheater3, cheater4));
    this.players4cheaters1normal = new ArrayList<>(Arrays.asList(p5, cheater1, cheater2, cheater3, cheater4));

    this.timeout1 = new PlayerAI(new TimeoutStrategy(), 2, 3, "Timeout 1");

    this.players1timeoutStrategy4normal = new ArrayList<>(Arrays.asList(p2, timeout1, p1, p4, p5));
  }
  
  private static class Pair {
    int first;
    int second;
    
    public Pair(int first, int second) {
      this.first = first;
      this.second = second;
    }
    
  }

  private Map<Integer, List<Pair>> mapGroupToInteger(int max) {
    Map<Integer, List<Pair>> map = new HashMap<>();
    for (int i = 2; i <= max; i++) {
      List<Pair> pairs = new ArrayList<>();
      if (i % 4 == 0) {
        int count = 0;
        for (int j = 0; j < i/4; j++) {
          pairs.add(new Pair(count, count + 4));
          count = count + 4;
        }
      } else if (i == 2 || i == 3) {
        pairs.add(new Pair(0, i));
      } else if (i == 5) {
        pairs.add(new Pair(0, 3));
        pairs.add(new Pair(3, 5));
      } else if (i == 6) {
        pairs.add(new Pair(0, 3));
        pairs.add(new Pair(3, 6));
      } else if (i == 9) {
        pairs.add(new Pair(0, 3));
        pairs.add(new Pair(3, 6));
        pairs.add(new Pair(6, 9));
      } else if ((i - 3) % 4 == 0 && i != 3) {
        int count = 0;
        for (int j = 0; j < (i-3)/4; j++) {
          pairs.add(new Pair(count, count + 4));
          count = count + 4;
        }
        pairs.add(new Pair(count, count + 3));
      } else if ((i - 6) % 4 == 0 && i != 6) {
        int count = 0;
        for (int j = 0; j < (i-6)/4; j++) {
          pairs.add(new Pair(count, count + 4));
          count = count + 4;
        }
        pairs.add(new Pair(count, count + 3));
        pairs.add(new Pair(count + 3, count + 6));
      } else if ((i - 9) % 4 == 0 && i != 9) {
        int count = 0;
        for (int j = 0; j < (i-9)/4; j++) {
          pairs.add(new Pair(count, count + 4));
          count = count + 4;
        }
        pairs.add(new Pair(count, count + 3));
        pairs.add(new Pair(count + 3, count + 6));
        pairs.add(new Pair(count + 6, count + 9));

      } else {
        throw new IllegalArgumentException("The conditional should catch everything");
      }
      map.put(i, pairs);
    }
    return map;
  }


  private List<PlayerInterface> generatePlayers(int numPlayers) {
    List<PlayerInterface> players = new ArrayList<>();

    for (int i = 0; i < numPlayers; i++) {
      players.add(new PlayerAI(new Strategy(), 1, i + 1, "Player " + (i + 1)));
    }

    return players;
  }

  /**
   * ********************************************************************************************
   * Tests for Constructor
   * ********************************************************************************************
   */

  @Test
  public void testConstructor2Players() {
    init();
    this.tournamentManager = new TournamentManager(this.players2);
    assertFalse(this.tournamentManager.isTournamentOver());

    Map<PlayerStanding, List<PlayerInterface>> stats = this.tournamentManager
        .getTournamentStatistics();

    assertEquals(0, stats.get(PlayerStanding.CHEATER).size());
    assertEquals(0, stats.get(PlayerStanding.ELIMINATED).size());
    assertEquals(this.players2, stats.get(PlayerStanding.REMAINING));
  }

  @Test
  public void testConstructor3PlayersOutOfOrder() {
    init();
    this.tournamentManager = new TournamentManager(this.players3);
    assertFalse(this.tournamentManager.isTournamentOver());

    Map<PlayerStanding, List<PlayerInterface>> stats = this.tournamentManager
        .getTournamentStatistics();

    assertEquals(0, stats.get(PlayerStanding.CHEATER).size());
    assertEquals(0, stats.get(PlayerStanding.ELIMINATED).size());
    assertEquals(generatePlayers(3), stats.get(PlayerStanding.REMAINING));
  }

  @Test
  public void testConstructor4PlayersOutOfOrder() {
    init();
    this.tournamentManager = new TournamentManager(this.players4);
    assertFalse(this.tournamentManager.isTournamentOver());

    Map<PlayerStanding, List<PlayerInterface>> stats = this.tournamentManager
        .getTournamentStatistics();

    assertEquals(0, stats.get(PlayerStanding.CHEATER).size());
    assertEquals(0, stats.get(PlayerStanding.ELIMINATED).size());
    assertEquals(generatePlayers(4), stats.get(PlayerStanding.REMAINING));
  }

  @Test
  public void testConstructor20Players() {
    init();
    this.tournamentManager = new TournamentManager(this.players20);
    assertFalse(this.tournamentManager.isTournamentOver());

    Map<PlayerStanding, List<PlayerInterface>> stats = this.tournamentManager
        .getTournamentStatistics();

    assertEquals(0, stats.get(PlayerStanding.CHEATER).size());
    assertEquals(0, stats.get(PlayerStanding.ELIMINATED).size());
    assertEquals(generatePlayers(20), stats.get(PlayerStanding.REMAINING));
  }

  /**
   * ********************************************************************************************
   * Tests for One Game in Tournament
   * ********************************************************************************************
   */
  @Test
  public void testOneGameOfTournament20Players() {
    init();
    this.tournamentManager = new TournamentManager(this.players20);

    List<PlayerInterface> group1 = this.tournamentManager.allocatePlayers().get(0);

    IGameResult result = this.tournamentManager.runGame(group1);

    List<PlayerInterface> expectedCheaters = result.getCheaters();
    List<PlayerInterface> actualCheaters =
        this.tournamentManager.getTournamentStatistics().get(PlayerStanding.CHEATER);

    List<PlayerInterface> expectedEliminated = result.getEliminated();
    List<PlayerInterface> actualEliminated =
        this.tournamentManager.getTournamentStatistics().get(PlayerStanding.ELIMINATED);

    List<PlayerInterface> expectedRemaining = result.getWinners();
    List<PlayerInterface> actualRemaining =
        this.tournamentManager.getTournamentStatistics().get(PlayerStanding.REMAINING);

    boolean someRemaining = this.players20.size() >= actualRemaining.size()
        && actualRemaining.size() != 0;

    assertEquals(expectedCheaters, actualCheaters);
    assertEquals(expectedEliminated, actualEliminated);
    assertEquals(expectedRemaining, actualRemaining);
    assertTrue(someRemaining);
  }

  /**
   * Every subsequent game adds to the statistics.
   */
  @Test
  public void testSecondGameOfTournament20Players() {
    init();
    this.tournamentManager = new TournamentManager(this.players20);
    List<List<PlayerInterface>> groups = this.tournamentManager.allocatePlayers();
    List<PlayerInterface> group1 = groups.get(0);
    List<PlayerInterface> group2 = groups.get(1);

    IGameResult resultGroup1 = this.tournamentManager.runGame(group1);
    List<PlayerInterface> expectedCheaters = resultGroup1.getCheaters();
    List<PlayerInterface> expectedEliminated = resultGroup1.getEliminated();
    List<PlayerInterface> expectedRemaining = resultGroup1.getWinners();

    IGameResult resultGroup2 = this.tournamentManager.runGame(group2);
    expectedCheaters.addAll(resultGroup2.getCheaters());
    expectedEliminated.addAll(resultGroup2.getEliminated());
    expectedRemaining.addAll(resultGroup2.getWinners());

    List<PlayerInterface> actualCheaters =
        this.tournamentManager.getTournamentStatistics().get(PlayerStanding.CHEATER);
    List<PlayerInterface> actualEliminated =
        this.tournamentManager.getTournamentStatistics().get(PlayerStanding.ELIMINATED);
    List<PlayerInterface> actualRemaining =
        this.tournamentManager.getTournamentStatistics().get(PlayerStanding.REMAINING);

    boolean someRemaining = this.players20.size() >= actualRemaining.size()
        && actualRemaining.size() != 0;

    assertEquals(expectedCheaters, actualCheaters);
    assertEquals(expectedEliminated, actualEliminated);
    assertEquals(expectedRemaining, actualRemaining);
    assertTrue(someRemaining);
  }

  @Test
  public void testOneGameCheatingPlayers() {
    init();
    this.tournamentManager = new TournamentManager(this.players4cheaters1normal);
    List<List<PlayerInterface>> groups = this.tournamentManager.allocatePlayers();
    List<PlayerInterface> group1 = groups.get(0);

    IGameResult resultGroup1 = this.tournamentManager.runGame(group1);
    List<PlayerInterface> expectedCheaters = resultGroup1.getCheaters();

    List<PlayerInterface> actualCheaters =
        this.tournamentManager.getTournamentStatistics().get(PlayerStanding.CHEATER);
    List<PlayerInterface> actualEliminated =
        this.tournamentManager.getTournamentStatistics().get(PlayerStanding.ELIMINATED);
    List<PlayerInterface> actualRemaining =
        this.tournamentManager.getTournamentStatistics().get(PlayerStanding.REMAINING);

    assertEquals(expectedCheaters, actualCheaters);
    assertEquals(3, actualCheaters.size());
    assertEquals(new ArrayList<>(Arrays.asList(cheater1, cheater2, cheater3)), actualCheaters);
    assertEquals(new ArrayList<>(), actualEliminated);
    assertEquals(new ArrayList<>(), actualRemaining);
  }

  @Test
  public void testTwoGamesCheatingPlayers() {
    init();
    this.tournamentManager = new TournamentManager(this.players1cheater4normal);
    List<List<PlayerInterface>> groups = this.tournamentManager.allocatePlayers();
    List<PlayerInterface> group1 = groups.get(0);
    List<PlayerInterface> group2 = groups.get(1);

    IGameResult resultGroup1 = this.tournamentManager.runGame(group1);

    List<PlayerInterface> actualCheaters =
        this.tournamentManager.getTournamentStatistics().get(PlayerStanding.CHEATER);
    List<PlayerInterface> actualEliminated =
        this.tournamentManager.getTournamentStatistics().get(PlayerStanding.ELIMINATED);
    List<PlayerInterface> actualRemaining =
        this.tournamentManager.getTournamentStatistics().get(PlayerStanding.REMAINING);

    assertEquals(new ArrayList<>(), actualCheaters);
    assertEquals(3, actualEliminated.size() + actualRemaining.size());

    this.tournamentManager.runGame(group2);

    actualCheaters =
        this.tournamentManager.getTournamentStatistics().get(PlayerStanding.CHEATER);
    actualEliminated =
        this.tournamentManager.getTournamentStatistics().get(PlayerStanding.ELIMINATED);
    actualRemaining =
        this.tournamentManager.getTournamentStatistics().get(PlayerStanding.REMAINING);

    assertEquals(new ArrayList<>(Arrays.asList(cheater4)), actualCheaters);
    assertEquals(4, actualEliminated.size() + actualRemaining.size());
  }


  /**
   * ********************************************************************************************
   * Tests for One Round of Tournament
   * ********************************************************************************************
   */

  @Test
  public void testOneRoundOfTournament20Players() {
    init();
    this.tournamentManager = new TournamentManager(this.players20);
    List<IGameResult> results = this.tournamentManager.runRound();

    List<PlayerInterface> expectedCheaters = new ArrayList<>();
    List<PlayerInterface> expectedEliminated = new ArrayList<>();
    List<PlayerInterface> expectedRemaining = new ArrayList<>();

    for (IGameResult result : results) {
      expectedCheaters.addAll(result.getCheaters());
      expectedEliminated.addAll(result.getEliminated());
      expectedRemaining.addAll(result.getWinners());
    }

    List<PlayerInterface> actualCheaters =
        this.tournamentManager.getTournamentStatistics().get(PlayerStanding.CHEATER);
    List<PlayerInterface> actualEliminated =
        this.tournamentManager.getTournamentStatistics().get(PlayerStanding.ELIMINATED);
    List<PlayerInterface> actualRemaining =
        this.tournamentManager.getTournamentStatistics().get(PlayerStanding.REMAINING);

    boolean someRemaining = this.players20.size() >= actualRemaining.size()
        && actualRemaining.size() != 0;

    assertEquals(expectedCheaters, actualCheaters);
    assertEquals(expectedEliminated, actualEliminated);
    assertEquals(expectedRemaining, actualRemaining);
    assertTrue(someRemaining);
  }

  @Test
  public void testTwoRoundsOfTournament20Players() {
    init();
    this.tournamentManager = new TournamentManager(this.players20);
    List<IGameResult> resultsRound1 = this.tournamentManager.runRound();
    List<IGameResult> resultsRound2 = this.tournamentManager.runRound();

    assertEquals(resultsRound1, this.tournamentManager.getRoundResults(1));
    assertEquals(resultsRound2, this.tournamentManager.getRoundResults(2));
  }

  @Test
  public void testOneRoundOfTournament20PlayersProperAllocation() {
    init();
    this.tournamentManager = new TournamentManager(this.players20);

    List<IGameResult> results = this.tournamentManager.runRound();
    for (int i = 0; i < this.players20.size(); i = i + 4) {
      List<PlayerInterface> players = (this.players20.subList(i, i + 4));
      IGameResult game = results.get(i / 4);

      List<PlayerInterface> gamePlayers = new ArrayList<>();
      gamePlayers.addAll(game.getWinners());
      gamePlayers.addAll(game.getEliminated());
      gamePlayers.addAll(game.getCheaters());

      gamePlayers.sort(Comparator.comparing(PlayerInterface::getPlayerAge));

      assertEquals(players, gamePlayers);
    }
  }

  /**
   * ********************************************************************************************
   * Tests for Allocate Players
   * ********************************************************************************************
   */
  @Test(expected = IllegalArgumentException.class)
  public void testAllocateNoPlayers() {
    this.init();

    TournamentManager t0 = new TournamentManager(new ArrayList<>());

    t0.allocatePlayers();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAllocatedOnePlayer() {
    this.init();

    TournamentManager t1 = new TournamentManager(new ArrayList<>(Arrays.asList(p1)));

    t1.allocatePlayers();
  }


  @Test
  public void testAllocatePlayersBase() {
    this.init();

    TournamentManager t2 = new TournamentManager(this.players2);
    TournamentManager t3 = new TournamentManager(this.players3);
    TournamentManager t4 = new TournamentManager(this.players4);

    List<List<PlayerInterface>> p2 = t2.allocatePlayers();
    List<PlayerInterface> groupP2 = p2.get(0);

    assertEquals(1, p2.size());
    assertEquals(groupP2, this.players2);

    List<List<PlayerInterface>> p3 = t3.allocatePlayers();
    List<PlayerInterface> groupP3 = p3.get(0);
    this.players3.sort(Comparator.comparing(PlayerInterface::getPlayerAge));

    assertEquals(1, p3.size());
    assertEquals(this.players3, groupP3);

    List<List<PlayerInterface>> p4 = t4.allocatePlayers();
    List<PlayerInterface> groupP4 = p4.get(0);
    this.players4.sort(Comparator.comparing(PlayerInterface::getPlayerAge));

    assertEquals(1, p4.size());
    assertEquals(groupP4, this.players4);
  }

  @Test
  public void testAllocatePlayers56And9Cases() {
    this.init();

    List<PlayerInterface> players5 = this.generatePlayers(5);
    List<PlayerInterface> players6 = this.generatePlayers(6);
    List<PlayerInterface> players9 = this.generatePlayers(9);

    TournamentManager t5 = new TournamentManager(players5);
    TournamentManager t6 = new TournamentManager(players6);
    TournamentManager t9 = new TournamentManager(players9);

    List<List<PlayerInterface>> p5 = t5.allocatePlayers();

    assertEquals(2, p5.size());
    assertEquals(players5.subList(0, 3), p5.get(0));
    assertEquals(players5.subList(3, 5), p5.get(1));

    List<List<PlayerInterface>> p6 = t6.allocatePlayers();

    assertEquals(2, p6.size());
    assertEquals(players6.subList(0, 3), p6.get(0));
    assertEquals(players6.subList(3, 6), p6.get(1));

    List<List<PlayerInterface>> p9 = t9.allocatePlayers();

    assertEquals(3, p9.size());
    assertEquals(players9.subList(0, 3), p9.get(0));
    assertEquals(players9.subList(3, 6), p9.get(1));
    assertEquals(players9.subList(6, 9), p9.get(2));
  }

  @Test
  public void testAllocatePlayers13() {
    this.init();

    TournamentManager t13 = new TournamentManager(this.players13);
    List<List<PlayerInterface>> p13 = t13.allocatePlayers();

    assertEquals(4, p13.size());
    assertEquals(this.players13.subList(0, 4), p13.get(0));
    assertEquals(this.players13.subList(4, 7), p13.get(1));
    assertEquals(this.players13.subList(7, 10), p13.get(2));
    assertEquals(this.players13.subList(10, 13), p13.get(3));
  }

  @Test
  public void testAllocatePlayers14() {
    this.init();

    List<PlayerInterface> players14 = this.generatePlayers(14);
    TournamentManager t14 = new TournamentManager(players14);
    List<List<PlayerInterface>> p14 = t14.allocatePlayers();

    assertEquals(4, p14.size());
    assertEquals(players14.subList(0, 4), p14.get(0));
    assertEquals(players14.subList(4, 8), p14.get(1));
    assertEquals(players14.subList(8, 11), p14.get(2));
    assertEquals(players14.subList(11, 14), p14.get(3));

  }

  @Test
  public void testAllocatePlayer2To20() {
    this.init();

    Map<Integer, List<Pair>> map = this.mapGroupToInteger(20);

    for (int i = 2; i <= 20; i++) {
      List<Pair> pairs = map.get(i);

      List<PlayerInterface> playersN = this.generatePlayers(i);
      TournamentManager tN = new TournamentManager(playersN);
      List<List<PlayerInterface>> pN = tN.allocatePlayers();

      for (int p = 0; p < pairs.size(); p++) {
        int from = pairs.get(p).first;
        int to = pairs.get(p).second;
        assertEquals(playersN.subList(from, to), pN.get(p));
      }
    }
  }

  @Test
  public void testAllocatePlayer2To100() {
    this.init();

    Map<Integer, List<Pair>> map = this.mapGroupToInteger(100);

    for (int i = 2; i <= 100; i++) {
      List<Pair> pairs = map.get(i);

      List<PlayerInterface> playersN = this.generatePlayers(i);
      TournamentManager tN = new TournamentManager(playersN);
      List<List<PlayerInterface>> pN = tN.allocatePlayers();

      for (int p = 0; p < pairs.size(); p++) {
        int from = pairs.get(p).first;
        int to = pairs.get(p).second;
        assertEquals(playersN.subList(from, to), pN.get(p));
      }
    }
  }

  /**
   * ********************************************************************************************
   * Tests for GetRoundResults
   * ********************************************************************************************
   */

  @Test(expected = IllegalArgumentException.class)
  public void testGetRoundResults0NoRounds() {
    init();
    this.tournamentManager = new TournamentManager(this.players13);
    this.tournamentManager.getRoundResults(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetRoundResults1NoRounds() {
    init();
    this.tournamentManager = new TournamentManager(this.players13);
    this.tournamentManager.getRoundResults(1);
  }

  @Test
  public void testGetRoundResults1OneRound() {
    init();
    this.tournamentManager = new TournamentManager(this.players13);
    this.tournamentManager.runRound();
    List<IGameResult> round1Results = this.tournamentManager.getRoundResults(1);
    assertEquals(4, round1Results.size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetRoundResults2OneRound() {
    init();
    this.tournamentManager = new TournamentManager(this.players13);
    this.tournamentManager.runRound();
    this.tournamentManager.getRoundResults(2);
  }

  @Test
  public void testGetRoundResults1RunTournament() {
    init();
    this.tournamentManager = new TournamentManager(this.players13);
    this.tournamentManager.runTournament();
    List<IGameResult> round1Results = this.tournamentManager.getRoundResults(1);
    assertEquals(4, round1Results.size());
  }

  @Test
  public void testGetRoundResults1RunTwo() {
    init();
    this.tournamentManager = new TournamentManager(this.players13);
    List<IGameResult> round1Results = this.tournamentManager.runRound();
    this.tournamentManager.runRound();
    assertEquals(4, this.tournamentManager.getRoundResults(1).size());
    assertEquals(round1Results, this.tournamentManager.getRoundResults(1));
  }

  @Test
  public void testGetRoundResults2RunTwo() {
    init();
    this.tournamentManager = new TournamentManager(this.players13);
    this.tournamentManager.runRound();
    List<IGameResult> round2Results = this.tournamentManager.runRound();
    assertTrue(this.tournamentManager.getRoundResults(2).size() < 4);
    assertEquals(round2Results, this.tournamentManager.getRoundResults(2));
  }

  /**
   * ********************************************************************************************
   * Tests for GetTournamentStatistics
   * ********************************************************************************************
   */

  @Test
  public void testGetTournamentStatisticsNotRun() {
    init();
    this.tournamentManager = new TournamentManager(this.players13);

    Map<PlayerStanding, List<PlayerInterface>> stats =
        this.tournamentManager.getTournamentStatistics();

    List<PlayerInterface> statsRemaining = stats.get(PlayerStanding.REMAINING);
    List<PlayerInterface> statsEliminated = stats.get(PlayerStanding.ELIMINATED);
    List<PlayerInterface> statsCheaters = stats.get(PlayerStanding.CHEATER);

    assertEquals(13, statsRemaining.size());
    assertEquals(0, statsEliminated.size());
    assertEquals(0, statsCheaters.size());
  }

  @Test
  public void testGetTournamentStatistics13Players1Round() {
    init();
    this.tournamentManager = new TournamentManager(this.players13);
    this.tournamentManager.runRound();

    Map<PlayerStanding, List<PlayerInterface>> stats =
        this.tournamentManager.getTournamentStatistics();

    List<PlayerInterface> statsRemaining = stats.get(PlayerStanding.REMAINING);
    List<PlayerInterface> statsEliminated = stats.get(PlayerStanding.ELIMINATED);
    List<PlayerInterface> statsCheaters = stats.get(PlayerStanding.CHEATER);

    assertEquals(13, statsRemaining.size() + statsEliminated.size());
    assertEquals(0, statsCheaters.size());
  }

  @Test
  public void testGetTournamentStatistics1Player() {
    init();
    List<PlayerInterface> players = new ArrayList<>(Arrays.asList(this.p1));
    this.tournamentManager = new TournamentManager(players);
    List<PlayerInterface> winners = this.tournamentManager.runTournament();

    assertTrue(this.tournamentManager.isTournamentOver());
    Map<PlayerStanding, List<PlayerInterface>> stats =
        this.tournamentManager.getTournamentStatistics();

    List<PlayerInterface> statsWinners = stats.get(PlayerStanding.REMAINING);
    List<PlayerInterface> statsEliminated = stats.get(PlayerStanding.ELIMINATED);
    List<PlayerInterface> statsCheaters = stats.get(PlayerStanding.CHEATER);

    assertEquals(winners, statsWinners);
    assertEquals(players, statsWinners);
    assertEquals(0, statsEliminated.size());
    assertEquals(0, statsCheaters.size());
  }

  @Test()
  public void testGetTournamentStatistics13Players() {
    init();
    this.tournamentManager = new TournamentManager(this.players13);
    List<PlayerInterface> winners = this.tournamentManager.runTournament();

    assertTrue(this.tournamentManager.isTournamentOver());
    Map<PlayerStanding, List<PlayerInterface>> stats =
        this.tournamentManager.getTournamentStatistics();

    List<PlayerInterface> statsWinners = stats.get(PlayerStanding.REMAINING);
    List<PlayerInterface> statsEliminated = stats.get(PlayerStanding.ELIMINATED);
    List<PlayerInterface> statsCheaters = stats.get(PlayerStanding.CHEATER);

    assertEquals(winners, statsWinners);
    assertEquals(13, statsWinners.size() + statsEliminated.size());
    try {
      assertTrue(statsWinners.size() < 4);
    } catch (AssertionError e) {
      // Catch the case where the results for two rounds happen to be exactly the same
      int roundNum = this.tournamentManager.getRound();
      assertEquals(this.tournamentManager.getRoundResults(roundNum), this.tournamentManager.getRoundResults(roundNum - 1));
    }
    assertEquals(0, statsCheaters.size());
  }

  /**
   *********************************************************************************************
   * Tests for RunTournament
   *********************************************************************************************
   */

  @Test
  public void testRunTournamentTwoPlayers() {
    this.init();
    this.tournamentManager = new TournamentManager(this.players2);
    List<PlayerInterface> winners = this.tournamentManager.runTournament();

    List<IGameResult> round1Results = this.tournamentManager.getRoundResults(1);

    assertEquals(round1Results.get(0).getWinners(), winners);
  }

  @Test
  public void testRunTournament13Players() {
    this.init();
    this.tournamentManager = new TournamentManager(this.players13);
    List<PlayerInterface> winners = this.tournamentManager.runTournament();
    assertTrue(this.tournamentManager.isTournamentOver());
    assertTrue(winners.size() < 4);

    List<IGameResult> round1Results = this.tournamentManager.getRoundResults(1);
    List<IGameResult> round2Results = this.tournamentManager.getRoundResults(2);

    assertEquals(4, round1Results.size());
    assertTrue(round2Results.size() < round1Results.size());
  }

  @Test
  public void testRunTournament20Players() {
    this.init();
    this.tournamentManager = new TournamentManager(this.players20);
    List<PlayerInterface> winners = this.tournamentManager.runTournament();
    assertTrue(this.tournamentManager.isTournamentOver());
    assertTrue(winners.size() < 4);

    List<IGameResult> round1Results = this.tournamentManager.getRoundResults(1);
    List<IGameResult> round2Results = this.tournamentManager.getRoundResults(2);
    List<IGameResult> round3Results = this.tournamentManager.getRoundResults(3);

    assertEquals(5, round1Results.size());
    assertTrue(round2Results.size() < round1Results.size());
    assertTrue(round3Results.size() < round2Results.size());
  }

  /**
   * ********************************************************************************************
   * Tests for isTournamentOver
   * ********************************************************************************************
   */

  @Test
  public void testTournamentOverTooSmall() {
    this.init();

    TournamentManager t0 = new TournamentManager(new ArrayList<>());
    TournamentManager t1 = new TournamentManager(new ArrayList<>(Arrays.asList(p1)));

    assertTrue(t0.isTournamentOver());
    assertTrue(t1.isTournamentOver());
  }

  @Test
  public void testTournamentOver4Players() {
    this.init();

    this.tournamentManager = new TournamentManager(this.players4);
    assertFalse(this.tournamentManager.isTournamentOver());

    this.tournamentManager.runTournament();
    assertTrue(this.tournamentManager.isTournamentOver());
  }

  @Test
  public void testTournamentOver13Players() {
    this.init();

    this.tournamentManager = new TournamentManager(this.players13);
    assertFalse(this.tournamentManager.isTournamentOver());

    this.tournamentManager.runRound();
    assertFalse(this.tournamentManager.isTournamentOver());

    this.tournamentManager.runTournament();
    assertTrue(this.tournamentManager.isTournamentOver());
  }

  @Test
  public void testTournamentOver20Players() {
    this.init();

    this.tournamentManager = new TournamentManager(this.players20);
    assertFalse(this.tournamentManager.isTournamentOver());

    this.tournamentManager.runRound();
    assertFalse(this.tournamentManager.isTournamentOver());

    this.tournamentManager.runRound();
    assertFalse(this.tournamentManager.isTournamentOver());

    this.tournamentManager.runTournament();
    assertTrue(this.tournamentManager.isTournamentOver());
  }
}
