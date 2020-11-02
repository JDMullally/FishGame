package model.tree;


import java.awt.*;
import java.util.List;

import model.state.IGameState;
import model.state.IPenguin;
import model.state.IPlayer;

/**
 * A Pass Penguin represents a specific action that passes a player's turn.
 */
public class PassPenguin implements Action {

    private IPlayer player;
    private IPenguin penguin;
    private Point position;

    /**
     * Constructor takes in required arguments to make a pass move
     *
     * @param player
     */
    public PassPenguin(IPlayer player) {
        try {
            IPenguin penguin = player.getPenguins().get(0);

            this.player = player.clone();
            this.penguin = penguin.clone();
            this.position = new Point(penguin.getPosition());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid player pass");
        }
    }

    @Override
    public IGameState apply(IGameState state) {
        return state.clone().move(this.player, this.penguin, this.position, true);
    }

    @Override
    public Point getFromPosition() {
        throw new UnsupportedOperationException("A PassPenguin action doesn't have a from position");
    }

    @Override
    public Point getToPosition() {
        throw new UnsupportedOperationException("A PassPenguin action doesn't have a from position");
    }

    @Override
    public String toString() {
        return this.player.getColor() + ": passed their turn!";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PassPenguin) {
            PassPenguin other = (PassPenguin) o;
            return this.position.x == other.position.x
                    && this.position.y == other.position.y
                    && this.player.equals(other.player)
                    && this.penguin.equals(other.penguin);
        }
        return false;
    }
}
