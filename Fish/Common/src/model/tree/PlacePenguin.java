package model.tree;

import java.awt.Point;
import model.board.Tile;
import model.state.IGameState;
import model.state.IPlayer;

/**
 * An Action that places a Penguin into a given position.
 */
public class PlacePenguin implements Action {

    private IPlayer player;
    private Point position;

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
        this.position = new Point(position);
    }

    @Override
    public IGameState apply(IGameState state) {
        return state.clone().placePenguin(this.player, this.position);
    }

    @Override
    public String toString() {
        return this.player.getColor() + " --> " + this.position;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PlacePenguin) {
            PlacePenguin other = (PlacePenguin) o;
            return this.position.x == other.position.x
                && this.position.y == other.position.y
                && this.player.equals(other.player);
        }
        return false;
    }
}
