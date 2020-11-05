package model.games;

import model.state.IGameState;
import model.strategy.IStrategy;
import model.tree.Action;
import model.tree.PlayerInterface;

/**
 * This Class represents a in-house AI Player's interface for interacting with the game.
 * PlayerAI must be able to place their avatars on the initial board and take turns when requested
 * by a referee.
 */
public class PlayerAI implements PlayerInterface {

    private IStrategy strategy;

    /**
     * Constructor takes in a player's IStrategy
     *
     * @param strategy IStrategy
     */
    public PlayerAI(IStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("IStrategy cannot be null");
        }

        this.strategy = strategy;
    }

    @Override
    public Action placePenguin(IGameState state) {
        return this.strategy.choosePlacePenguinAction(state);
    }

    @Override
    public Action movePenguin(IGameState state) {
        return this.strategy.chooseMoveAction(state, 2);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PlayerAI) {
            PlayerAI other = (PlayerAI) o;
            return this.strategy.equals(other.strategy);
        }
        return false;
    }
}
