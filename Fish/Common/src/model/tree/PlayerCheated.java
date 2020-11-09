package model.tree;

import java.awt.*;

import model.state.IGameState;
import model.state.IPlayer;
import util.ColorUtil;

/**
 * A Player Cheated action represents a specific action that represents a cheating player.
 */
public class PlayerCheated implements Action {

    private IPlayer player;

    /**
     * Constructor takes in an IPlayer.
     *
     * @param player IPlayer
     */
    public PlayerCheated(IPlayer player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }

        this.player = player;
    }

    @Override
    public IGameState apply(IGameState state) {
        return state.removePlayer(this.player);
    }

    @Override
    public Point getFromPosition() {
        throw new UnsupportedOperationException("A PlayerCheated action doesn't have a from position");
    }

    @Override
    public Point getToPosition() {
        throw new UnsupportedOperationException("A PlayerCheated action doesn't have a from position");
    }

    @Override
    public String toString() {
        return ColorUtil.toColorString(this.player.getColor()) + ": cheated!";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PlayerCheated) {
            PlayerCheated other = (PlayerCheated) o;
            return this.player.equals(other.player);
        }
        return false;
    }
}
