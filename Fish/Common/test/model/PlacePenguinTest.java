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
import model.tree.Action;
import model.tree.PlacePenguin;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlacePenguinTest {

    private IPlayer player1, player2;
    private List<IPlayer> players;
    private IGameState gameState;
    private Action place11;

    private void init() {
        this.player1 = new Player(Color.BLACK, 15, new ArrayList<>());
        this.player2 = new Player(Color.WHITE, 15, new ArrayList<>());

        this.players = Arrays.asList(player1, player2);

        this.gameState = new GameState(8,8, new ArrayList<>(), 0, 2, this.players);
    }

    private void placeNPenguins(int n) {
        for (int i = 0; i < n; i++) {
            this.gameState = new PlacePenguin(this.gameState.playerTurn(),
                new Point(i, 0)).apply(this.gameState);
        }
    }

    /**
     * PlacePenguin has one method: apply.  This method should maintain the rules of the game.
     */

    /**
     * This test assures that two players can each place a penguin
     */
    @Test
    public void PlaceTwoPenguins() {
        this.init();
        assertEquals(0, this.gameState.playerTurn().getPenguins().size());

        this.place11 = new PlacePenguin(this.gameState.playerTurn(), new Point(1,1));
        this.gameState = this.place11.apply(this.gameState);

        assertEquals(0, this.gameState.playerTurn().getPenguins().size());

        Action place01 = new PlacePenguin(this.gameState.playerTurn(), new Point(0,1));
        this.gameState = place01.apply(this.gameState);

        assertEquals(1, this.gameState.playerTurn().getPenguins().size());
    }

    /**
     * This test assures that a given player cannot place two penguins in a row if it isn't
     * their turn.
     */
    @Test (expected = IllegalArgumentException.class)
    public void PlacePenguinTwoInARow() {
        this.init();
        IPlayer player1 = this.gameState.playerTurn();
        this.place11 = new PlacePenguin(player1, new Point(1,1));
        this.gameState = this.place11.apply(this.gameState);
        Action place01 = new PlacePenguin(player1, new Point(0,1));
        this.gameState = place01.apply(this.gameState);
    }

    /**
     * This test assures that a player cannot place a penguin on another penguin.
     */
    @Test (expected = IllegalArgumentException.class)
    public void PlacePenguinOnAnotherPenguin() {
        this.init();
        this.place11 = new PlacePenguin(this.gameState.playerTurn(), new Point(1,1));
        this.gameState = this.place11.apply(this.gameState);
        this.place11 = new PlacePenguin(this.gameState.playerTurn(), new Point(1,1));
        this.gameState = this.place11.apply(this.gameState);
    }

    /**
     * This test assures that a player cannot place a penguin once all penguins have been placed.
     */
    @Test (expected = IllegalArgumentException.class)
    public void PlacePenguinPlacementRoundOver() {
        this.init();
        this.placeNPenguins(8);
        Action placeNinth = new PlacePenguin(this.gameState.playerTurn(), new Point(1,1));
        placeNinth.apply(this.gameState);
    }


}
