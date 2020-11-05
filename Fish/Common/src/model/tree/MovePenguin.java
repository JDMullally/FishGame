package model.tree;

import java.awt.*;

import model.state.IGameState;
import model.state.IPenguin;
import model.state.IPlayer;
import model.board.Tile;
import util.ColorUtil;

/**
 * A Move Penguin represents a specific action that attempts to move a penguin on a game board.
 */
public class MovePenguin implements Action {

    private IPlayer player;
    private IPenguin penguin;
    private Point fromPosition;
    private Point toPosition;

    /**
     * Constructor takes in required arguments to make a move
     *
     * @param player
     * @param penguin
     * @param newTile
     */
    public MovePenguin(IPlayer player, IPenguin penguin, Tile newTile) {
        this(player, penguin, new Point(newTile.getPosition().x, newTile.getPosition().y));
    }

    /**
     * Constructor takes in required arguments to make a move
     *
     * @param player
     * @param penguin
     * @param newPoint
     */
    public MovePenguin(IPlayer player, IPenguin penguin, Point newPoint) {
        this.player = player.clone();
        this.penguin = penguin.clone();
        this.fromPosition = new Point(penguin.getPosition());
        this.toPosition = new Point(newPoint);
    }

    @Override
    public IGameState apply(IGameState state) throws IllegalArgumentException {
        return state.clone().move(this.player, this.penguin, this.toPosition, false);
    }

    @Override
    public Point getFromPosition() {
        return this.penguin.getPosition();
    }

    @Override
    public Point getToPosition() {
        return this.toPosition;
    }

    @Override
    public String toString() {
        return ColorUtil.toColorString(this.player.getColor())
            + ": " + this.fromPosition + " --> " + this.toPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MovePenguin) {
            MovePenguin other = (MovePenguin) o;
            return this.toPosition.x == other.toPosition.x
                    && this.toPosition.y == other.toPosition.y
                    && this.fromPosition.x == other.fromPosition.x
                    && this.fromPosition.y == other.fromPosition.y
                    && this.player.equals(other.player)
                    && this.penguin.equals(other.penguin);
        }
        return false;
    }
}
