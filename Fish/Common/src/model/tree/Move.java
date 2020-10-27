package model.tree;

import java.awt.*;

import model.state.IGameState;
import model.state.IPenguin;
import model.state.IPlayer;
import model.board.Tile;

/**
 * A Move represents a specific action that attempts to move a penguin on a game board.
 */
public class Move implements Action {

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
    public Move(IPlayer player, IPenguin penguin, Tile newTile, boolean pass) {
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
    public Move(IPlayer player, IPenguin penguin, Point newPoint, boolean pass) {
        this.player = player.clone();
        this.penguin = penguin.clone();
        this.newPoint = new Point(newPoint);
        this.pass = pass;
    }

    @Override
    public IGameState apply(IGameState state) throws IllegalArgumentException {
        return state.clone().move(player, penguin, newPoint, pass);
    }
}
