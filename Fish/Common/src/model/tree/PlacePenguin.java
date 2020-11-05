package model.tree;

import java.awt.Point;
import model.board.Tile;
import model.state.IGameState;
import model.state.IPlayer;
import util.ColorUtil;

/**
 * An Action that places a Penguin into a given toPosition.
 */
public class PlacePenguin implements Action {

    private IPlayer player;
    private Point toPosition;

    /**
     * Constructor that takes in a IPlayer and Tile to place a Penguin.
     * @param player IPlayer
     * @param tile Tile
     */
    public PlacePenguin(IPlayer player, Tile tile) {
        this(player, tile.getPosition());
    }

    /**
     * Constructor that takes in a IPlayer and Point to place a Penguin.
     * @param player IPlayer
     * @param position Point
     */
    public PlacePenguin(IPlayer player, Point position) {
        this.player = player.clone();
        this.toPosition = new Point(position);
    }

    @Override
    public IGameState apply(IGameState state) {
        return state.clone().placePenguin(this.player, this.toPosition);
    }

    @Override
    public Point getFromPosition() {
        throw new UnsupportedOperationException("A PlacePenguin action doesn't have a from position");
    }

    @Override
    public Point getToPosition() {
        return this.toPosition;
    }

    @Override
    public String toString() {
        return ColorUtil.toColorString(this.player.getColor()) + " --> " + this.toPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PlacePenguin) {
            PlacePenguin other = (PlacePenguin) o;
            return this.toPosition.x == other.toPosition.x
                && this.toPosition.y == other.toPosition.y
                && this.player.equals(other.player);
        }
        return false;
    }
}
