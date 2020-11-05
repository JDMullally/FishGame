package model;

import com.google.gson.internal.bind.util.ISO8601Utils;
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
import model.strategy.BadStrategy;
import model.strategy.Strategy;
import model.tree.PlayerInterface;
import org.junit.Test;

import static org.junit.Assert.*;

public class RefereeTest {

    private IGameBoard newBoard;
    private List<PlayerInterface> players2, players3, players4, oneCheater;

    void init() {
        this.newBoard = new GameBoard(5,5,
            new ArrayList<>());

        PlayerInterface p1 = new PlayerAI(new Strategy());
        PlayerInterface p2 = new PlayerAI(new Strategy());
        PlayerInterface p3 = new PlayerAI(new Strategy());
        PlayerInterface p4 = new PlayerAI(new Strategy());
        PlayerInterface cheater = new PlayerAI(new BadStrategy());

        this.oneCheater = new ArrayList<>(Arrays.asList(p1, p2, cheater));

        this.players2 = new ArrayList<>(Arrays.asList(p1, p2));
        this.players3 = new ArrayList<>(Arrays.asList(p1, p2, p3));
        this.players4 = new ArrayList<>(Arrays.asList(p1, p2, p3, p4));
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
     * When a player cheats, they will be removed from the game.  We have one player that has chosen
     * a strategy that asserts that they will cheat.  They should be removed from the game as soon
     * as the Referee calls on them.
     */
    @Test
    public void runGameThreePlayersOneCheater() {
        this.init();

        IReferee ref = new Referee(oneCheater);

        IGameResult result = ref.runGame(this.newBoard.getRows(), this.newBoard.getColumns());

        assertEquals(2, result.getPlayerPlacements().size());
        assertEquals(1, result.getCheaters().size());
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
