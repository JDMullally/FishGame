package model.tree;

import java.awt.*;
import java.util.List;

import model.state.IGameState;
import model.state.IPlayer;
import util.ColorUtil;

/**
 * A Player Cheated action represents a specific action that represents a cheating player.
 */
public class GameEnded implements Action {

    private IGameState gameState;

    /**
     * Constructor takes in an IGameState.
     *
     * @param gameState IGameState
     */
    public GameEnded(IGameState gameState) {
        if (gameState == null) {
            throw new IllegalArgumentException("IGameState cannot be null");
        } else if (!gameState.isGameOver()) {
            throw new IllegalArgumentException("IGameState is not over");
        }

        this.gameState = gameState;
    }

    @Override
    public IGameState apply(IGameState state) {
        return state;
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
        StringBuilder sb = new StringBuilder("Game with the following players: ");
        List<IPlayer> players = this.gameState.getPlayers();
        for (int i = 0; i < players.size(); i++) {
            IPlayer player = players.get(i);
            if (i == players.size() - 1) {
                sb.append(ColorUtil.toColorString(player.getColor()));
            } else {
                sb.append(ColorUtil.toColorString(player.getColor())).append(", ");
            }
        }
        sb.append(" Ended.");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GameEnded) {
            GameEnded other = (GameEnded) o;
            return this.gameState.equals(other.gameState);
        }
        return false;
    }
}
