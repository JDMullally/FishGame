package model;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.state.GameState;
import model.state.IGameState;
import model.state.IPlayer;
import model.state.Player;
import model.strategy.IStrategy;
import model.strategy.Strategy;
import model.tree.Action;
import model.tree.PlacePenguin;
import org.junit.Test;

import static org.junit.Assert.*;


public class StrategyTest {

    private IPlayer player1, player2;
    private List<IPlayer> players;
    private IGameState gameState, gameStateMinimax;

    private void init() {
        this.player1 = new Player(Color.BLACK, 15, new ArrayList<>());
        this.player2 = new Player(Color.WHITE, 15, new ArrayList<>());

        this.players = Arrays.asList(player1, player2);

        this.gameState = new GameState(7,7, new ArrayList<>(), 0, 2, this.players);

        this.gameStateMinimax = new GameState(4, 4, this.craftedBoard(), this.players);
    }

    private Tile[][] craftedBoard() {

    }


    private void placeNPenguins(int n) {
        for (int i = 0; i < n; i++) {
            this.gameState = new PlacePenguin(this.gameState.playerTurn(),
                new Point(i, 0)).apply(this.gameState);
        }
    }

    private void placeAllPenguins(IStrategy strategy) {
        Action placeNextPenguin;
        for (int i = 0; i < 2 * (6 - this.players.size()); i++) {
            placeNextPenguin = strategy.choosePlacePenguinAction(this.gameState);
            this.gameState = placeNextPenguin.apply(this.gameState);
        }
    }

    private void placeAllPenguinsMiniMaxTest(IStrategy strategy) {
        Action placeNextPenguin;
        for (int i = 0; i < 2 * (6 - this.players.size()); i++) {
            placeNextPenguin = strategy.choosePlacePenguinAction(this.gameStateMinimax);
            this.gameStateMinimax = placeNextPenguin.apply(this.gameStateMinimax);
        }
    }

    /**
     * Tests for choose PlacePenguinAction that Tests if a Player will place a penguin in the
     * correct place.
     */

    /**
     * Tests that the first player places their penguins in the correct place (top left corner)
     */
    @Test
    public void choosePlacePenguinActionFirstAction() {
        init();

        IStrategy strat = new Strategy();

        Action action = strat.choosePlacePenguinAction(this.gameState);

        assertEquals(new PlacePenguin(this.gameState.playerTurn(), new Point(0,0)), action);
    }

    /**
     * Tests that the player will choose the next possible space in that row.
     */
    @Test
    public void choosePlacePenguinActionThreePlacementIn() {
        init();
        this.placeNPenguins(4);

        IStrategy strat = new Strategy();

        Action action = strat.choosePlacePenguinAction(this.gameState);

        assertEquals(new PlacePenguin(this.gameState.playerTurn(), new Point(4,0)), action);
    }

    /**
     * If the row is filled, the player will move down in a zig zag pattern and start from the
     * left-most part of the row.
     */
    @Test
    public void choosePlacePenguinActionNextLine() {
        init();
        this.placeNPenguins(7);

        IStrategy strat = new Strategy();

        Action action = strat.choosePlacePenguinAction(this.gameState);

        assertEquals(new PlacePenguin(this.gameState.playerTurn(), new Point(0,1)), action);
    }

    /**
     * Tests for chooseMoveAction that tests if a Player uses minimax to move their penguins
     * to the correct place.
     */

    @Test
    public void chooseMoveAction() {
        init();
        IStrategy strat = new Strategy();

        this.placeAllPenguinsMiniMaxTest(strat);

        // System.out.println(this.gameStateMinimax.getPossibleMoves(this.gameStateMinimax.playerTurn()));
        Action action = strat.chooseMoveAction(this.gameStateMinimax, 1);

        // System.out.println(this.gameStateMinimax);

        System.out.println(action);
    }
}
