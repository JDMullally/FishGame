package model.tree;

import java.awt.*;

import model.state.IGameState;
import model.state.IPenguin;
import model.state.IPlayer;
import model.board.Tile;

/**
 * A Move represents a specific action that attempts to move a penguin on a game board.
 */
public class MovePenguin implements Action {

    private IPlayer player;
    private IPenguin penguin;
    private Point newPoint;
    private boolean pass;

    /**
     * Constructor takes in required arguments to make a move
     *
     * @param player
     * @param penguin
     * @param newTile
     * @param pass
     */
    public MovePenguin(IPlayer player, IPenguin penguin, Tile newTile, boolean pass) {
        this(player, penguin, new Point(newTile.getPosition().x, newTile.getPosition().y), pass);
    }

    /**
     * Constructor takes in required arguments to make a move
     *
     * @param player
     * @param penguin
     * @param newPoint
     * @param pass
     */
    public MovePenguin(IPlayer player, IPenguin penguin, Point newPoint, boolean pass) {
        this.player = player.clone();
        this.penguin = penguin.clone();
        this.newPoint = new Point(newPoint);
        this.pass = pass;
    }

    @Override
    public IGameState apply(IGameState state) throws IllegalArgumentException {
        return state.clone().move(player, penguin, newPoint, pass);
    }

    @Override
    public String toString() {
        if(pass) {
            return this.player.getColor() + ": passed their turn!";
        } else {
            return this.player.getColor() + ": " + this.penguin.getPosition() + " --> " + this.newPoint;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MovePenguin) {
            MovePenguin other = (MovePenguin) o;
            return this.newPoint.x == other.newPoint.x
                && this.newPoint.y == other.newPoint.y
                && this.player.equals(other.player)
                && this.penguin.equals(other.penguin)
                && this.pass == other.pass;
        }
        return false;
    }
}
