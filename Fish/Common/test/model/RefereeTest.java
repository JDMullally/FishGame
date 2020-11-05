package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.board.GameBoard;
import model.board.IGameBoard;
import model.games.GameAction;
import model.games.IGameResult;
import model.games.IReferee;
import model.games.PlayerAI;
import model.games.Referee;
import model.state.IGameState;
import model.strategy.testStrategies.MoveAnotherPlayerPenguin;
import model.strategy.testStrategies.MoveOutsideBoard;
import model.strategy.testStrategies.PlaceAnotherPlayerPenguin;
import model.strategy.testStrategies.PlaceOutsideBoard;
import model.strategy.testStrategies.PlacePenguinOnAnotherPlayerPenguin;
import model.strategy.testStrategies.PlacesPenguinsMovesWrong;
import model.strategy.Strategy;
import model.tree.PlayerInterface;
import org.junit.Test;

import static org.junit.Assert.*;

public class RefereeTest {

    private IGameBoard newBoard;
    private List<PlayerInterface> players2, players3, players4,
        oneCheater,
        oneBadMove,
        oneCheatsOneBadMove,
        oneMoveOutsideBoard,
        onePlacesPenguinOnAnotherPlayerPenguin,
        oneMoveAnotherPenguin,
        onePlaceOutsideBoard;

    void init() {
        this.newBoard = new GameBoard(5,5,
            new ArrayList<>());

        PlayerInterface p1 = new PlayerAI(new Strategy());
        PlayerInterface p2 = new PlayerAI(new Strategy());
        PlayerInterface p3 = new PlayerAI(new Strategy());
        PlayerInterface p4 = new PlayerAI(new Strategy());
        PlayerInterface placeAnotherPlayerPenguin =
            new PlayerAI(new PlaceAnotherPlayerPenguin());

        PlayerInterface placesPenguinsMovesWrong =
            new PlayerAI(new PlacesPenguinsMovesWrong());

        PlayerInterface moveOutsideBoard =
            new PlayerAI(new MoveOutsideBoard());

        PlayerInterface placesPenguinOneAnotherPlayerPenguin =
            new PlayerAI(new PlacePenguinOnAnotherPlayerPenguin());

        PlayerInterface moveAnotherPenguin =
            new PlayerAI(new MoveAnotherPlayerPenguin());

        PlayerInterface placeOutsideBoard =
            new PlayerAI(new PlaceOutsideBoard());

        this.players2 = new ArrayList<>(Arrays.asList(p1, p2));
        this.players3 = new ArrayList<>(Arrays.asList(p1, p2, p3));
        this.players4 = new ArrayList<>(Arrays.asList(p1, p2, p3, p4));

        this.oneCheater =
            new ArrayList<>(Arrays.asList(p1, p2, placeAnotherPlayerPenguin));

        this.oneBadMove =
            new ArrayList<>(Arrays.asList(p1, p2, p3, placesPenguinsMovesWrong));

        this.oneCheatsOneBadMove =
            new ArrayList<>(Arrays.asList(placesPenguinsMovesWrong, placeAnotherPlayerPenguin));

        this.oneMoveOutsideBoard =
            new ArrayList<>(Arrays.asList(p1, p2, moveOutsideBoard));

        this.onePlacesPenguinOnAnotherPlayerPenguin =
            new ArrayList<>(Arrays.asList(p1, p2, placesPenguinOneAnotherPlayerPenguin));

        this.oneMoveAnotherPenguin = new ArrayList<>(Arrays.asList(p1, p2, moveAnotherPenguin));

        this.onePlaceOutsideBoard = new ArrayList<>(Arrays.asList(p1, p2, placeOutsideBoard));
    }

    /**
     *********************************************************************************************
     * Tests for runGame
     *********************************************************************************************
     */

    /**
     * We should be able to run a standard game for any number of players.
     */
    @Test
    public void runStandardGameTwoPlayers() {
        this.init();

        IReferee ref = new Referee(players2);

        IGameResult result = ref.runGame(this.newBoard.getRows(), this.newBoard.getColumns());

        assertEquals(2, result.getPlayerPlacements().size());
        assertEquals(0, result.getCheaters().size());
    }

    @Test
    public void runStandardGameThreePlayers() {
        this.init();

        IReferee ref = new Referee(players3);

        IGameResult result = ref.runGame(this.newBoard.getRows(), this.newBoard.getColumns());

        assertEquals(3, result.getPlayerPlacements().size());
        assertEquals(0, result.getCheaters().size());
    }

    @Test
    public void runStandardGameFourPlayers() {
        this.init();

        IReferee ref = new Referee(players4);

        IGameResult result = ref.runGame(this.newBoard.getRows(), this.newBoard.getColumns());

        List<GameAction> actions = ref.getOngoingActions();

        actions.forEach(System.out::println);

        assertEquals(4, result.getPlayerPlacements().size());
        assertEquals(0, result.getCheaters().size());
    }

    /**
     * The following tests for runGame have the Player incorrectly place a Penguin.
     *
     * When a player cheats, they will be removed from the game.  We have one player that has chosen
     * a strategy that asserts that they will cheat.  They should be removed from the game as soon
     * as the Referee calls on them.
     */
    @Test
    public void runGameThreePlayersOneCheater() {
        this.init();

        IReferee ref = new Referee(oneCheater);

        IGameResult result = ref.runGame(this.newBoard.getRows(), this.newBoard.getColumns());

        assertTrue(ref.getGameState().isGameOver());
        assertEquals(2, result.getPlayerPlacements().size());
        assertEquals(1, result.getCheaters().size());
    }

    @Test
    public void runGameThreePlayersOnePlacesPenguinOutsideBoard() {
        this.init();

        IReferee ref = new Referee(onePlaceOutsideBoard);

        IGameResult result = ref.runGame(this.newBoard.getRows(), this.newBoard.getColumns());

        assertTrue(ref.getGameState().isGameOver());
        assertEquals(2, result.getPlayerPlacements().size());
        assertEquals(1, result.getCheaters().size());
    }

    @Test
    public void runGameThreePlayersOnePlacesPenguinOnAnotherPlayerPenguin() {
        this.init();

        IReferee ref = new Referee(onePlacesPenguinOnAnotherPlayerPenguin);

        IGameResult result = ref.runGame(this.newBoard.getRows(), this.newBoard.getColumns());

        assertTrue(ref.getGameState().isGameOver());
        assertEquals(2, result.getPlayerPlacements().size());
        assertEquals(1, result.getCheaters().size());
    }



    /**
     * The following tests for runGame have the Player place Penguins correctly, but does not move a penguin
     * in a valid manner.
     *
     * When a player cheats after the placement round, they will have their penguins removed.  This
     * test is designed to check if the game breaks if a player's penguins are removed from the game.
     */
    @Test
    public void runGameFourPlayersOneCheatsAfterPlacementPenguinsMovesWrong() {
        this.init();

        IReferee ref = new Referee(oneBadMove);

        IGameResult result = ref.runGame(this.newBoard.getRows(), this.newBoard.getColumns());

        assertTrue(ref.getGameState().isGameOver());
        assertEquals(3, result.getPlayerPlacements().size());
        assertEquals(1, result.getCheaters().size());
    }

    @Test
    public void runGameThreePlayersMoveOutsideBoard() {
        this.init();

        IReferee ref = new Referee(this.oneMoveOutsideBoard);

        IGameResult result = ref.runGame(this.newBoard.getRows(), this.newBoard.getColumns());

        assertTrue(ref.getGameState().isGameOver());
        assertEquals(2, result.getPlayerPlacements().size());
        assertEquals(1, result.getCheaters().size());
    }

    @Test
    public void runGameThreePlayersMoveAnotherPenguin() {
        this.init();

        IReferee ref = new Referee(this.oneMoveAnotherPenguin);

        IGameResult result = ref.runGame(this.newBoard.getRows(), this.newBoard.getColumns());

        List<GameAction> actions = ref.getOngoingActions();
        actions.forEach(System.out::println);

        assertTrue(ref.getGameState().isGameOver());
        assertEquals(2, result.getPlayerPlacements().size());
        assertEquals(1, result.getCheaters().size());
    }

    /**
     * If both players cheat, the game will be declared over and the two players will be returned as
     * cheaters.
     */

    @Test
    public void allCheatersGameEnds() {
        this.init();

        IReferee ref = new Referee(oneCheatsOneBadMove);

        IGameResult result = ref.runGame(this.newBoard.getRows(), this.newBoard.getColumns());

        assertTrue(ref.getGameState().isGameOver());
        assertEquals(0, result.getPlayerPlacements().size());
        assertEquals(2, result.getCheaters().size());

    }


    /**********************************************************************************************
     * Tests for GetGameResult.
     *********************************************************************************************
     */
    @Test (expected = IllegalStateException.class)
    public void getGameResultBeforeGame() {
        this.init();
        IReferee ref = new Referee(players2);
        ref.getGameResult();
    }

    @Test
    public void getGameResultAfterGame() {
        this.init();
        IReferee ref = new Referee(players2);

        IGameResult result = ref.runGame(this.newBoard.getRows(), this.newBoard.getColumns());

        IGameResult result1 = ref.getGameResult();

        assertEquals(result, result1);
    }

    /**
     *********************************************************************************************
     * Tests for getGameState.
     *********************************************************************************************
     */
    @Test (expected = IllegalStateException.class)
    public void getGameStateBeforeGame() {
        this.init();
        IReferee ref = new Referee(players2);
        ref.getGameState();
    }

    @Test
    public void getGameStateAfterGame() {
        this.init();
        IReferee ref = new Referee(players2);

        IGameResult result = ref.runGame(this.newBoard.getRows(), this.newBoard.getColumns());

        IGameState finalGameState = ref.getGameState();

        assertTrue(finalGameState.isGameOver());
    }

    /**
     *********************************************************************************************
     * Tests for getOngoingActions.
     *********************************************************************************************
     */
    @Test
    public void getOngoingActionsBeforeGame() {
        this.init();
        IReferee ref = new Referee(players2);

        assertTrue(ref.getOngoingActions().isEmpty());
    }

    @Test
    public void getOngoingActionsAfterGame() {
        this.init();
        IReferee ref = new Referee(players2);

        assertTrue(ref.getOngoingActions().isEmpty());
    }
}
