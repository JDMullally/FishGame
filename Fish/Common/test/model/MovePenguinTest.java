package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.state.GameState;
import model.state.IGameState;
import model.state.IPenguin;
import model.state.IPlayer;
import model.state.Penguin;
import model.state.Player;
import model.tree.Action;
import model.tree.MovePenguin;
import model.tree.PlacePenguin;
import org.junit.Test;


public class MovePenguinTest {

    private List<IPenguin> penguins1, penguins2;
    private IPlayer player1, player2;
    private List<IPlayer> players;
    private IGameState gameState;
    private Action pass, moveDown;

    private void init() {

        this.player1 = new Player(Color.BLACK, 14, new ArrayList<>());
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
     * MovePenguin has one method: apply.  This method should maintain the rules of the game.
     */

    /**
     * Move Penguin should be able to have a Penguin move to a valid position and pass if they
     * so desire.
     */
    @Test
    public void MovePenguinValidMove() {
        this.init();
        this.placeNPenguins(8);

        IPenguin penguin = this.gameState.playerTurn().getPenguins().get(0);

        this.pass = new MovePenguin(this.gameState.playerTurn(),
            this.gameState.playerTurn().getPenguins().get(0), new Point(100,100), true);

        assertEquals(new Point(0,0), this.gameState.playerTurn().getPenguins().get(0).getPosition());

        this.gameState = this.pass.apply(this.gameState);

        this.moveDown = new MovePenguin(this.gameState.playerTurn(), this.gameState.playerTurn().getPenguins().get(1), new Point(3, 2), false);

        this.gameState = this.moveDown.apply(this.gameState);

        IPlayer wentLast = this.gameState.getPlayers().get(1);
        IPenguin lastMoved = wentLast.getPenguins().get(1);

        assertEquals(new Point(3,2), lastMoved.getPosition());
    }

    /**
     * A player should not be able to move a penguin onto a space occupied by another penguin.
     */
    @Test (expected = IllegalArgumentException.class)
    public void MovePenguinOnAnotherPenguin() {
        this.init();
        this.placeNPenguins(7);
        new PlacePenguin(this.gameState.playerTurn(), new Point(2,2)).apply(this.gameState);
        this.moveDown = new MovePenguin(this.gameState.playerTurn(),
            this.gameState.playerTurn().getPenguins().get(1), new Point(2, 2), false);
        this.gameState = this.moveDown.apply(this.gameState);
    }

    /**
     * A Player cannot move a Penguin when it is not their turn.
     */
    @Test (expected = IllegalArgumentException.class)
    public void MovePenguinNotOnTheirTurn() {
        this.init();
        this.placeNPenguins(8);
        IPlayer player = this.gameState.getPlayers().get(1);

        this.moveDown = new MovePenguin(player, player.getPenguins().get(0),
            new Point(0,2), false);

        this.gameState = this.moveDown.apply(this.gameState);
    }

    /**
     * A Player cannot move another Player's Penguin.
     */
    @Test (expected = IllegalArgumentException.class)
    public void MovePenguinOwnedByOtherPlayer() {
        this.init();
        this.placeNPenguins(8);

        IPenguin otherPenguin = this.gameState.getPlayers().get(1).getPenguins().get(0);

        this.moveDown = new MovePenguin(this.gameState.playerTurn(), otherPenguin,
            new Point(1,2), false);

        this.gameState = this.moveDown.apply(this.gameState);
    }

    /**
     * A Player should not be able to move their Penguin
     */
    @Test (expected = IllegalArgumentException.class)
    public void MoveButCannotMoveToEmptyTile() {
        this.init();
        this.placeNPenguins(8);

        this.moveDown = new MovePenguin(this.gameState.playerTurn(),
            this.gameState.playerTurn().getPenguins().get(0),
            new Point(0,2), false);

        this.gameState = this.moveDown.apply(this.gameState);

        IPenguin penguin = this.gameState.getPlayers().get(1).getPenguins().get(0);

        assertEquals(new Point(0,2), penguin.getPosition());

        this.pass = new MovePenguin(this.gameState.playerTurn(),
            this.gameState.playerTurn().getPenguins().get(0), new Point(100,100), true);

        this.gameState = this.pass.apply(this.gameState);

        Action moveUp = new MovePenguin(this.gameState.playerTurn(),
            this.gameState.playerTurn().getPenguins().get(1),
            new Point(0,0), false);

        moveUp.apply(this.gameState);
    }
}
