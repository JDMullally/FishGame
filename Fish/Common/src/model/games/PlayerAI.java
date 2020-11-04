package model.games;

import model.state.IGameState;
import model.state.IPlayer;
import model.strategy.IStrategy;
import model.tree.Action;
import model.tree.PlayerInterface;

/**
 * This Class represents a in-house AI Player's interface for interacting with the game.
 * PlayerAI must be able to place their avatars on the initial board and take turns when requested
 * by a referee.
 */
public class PlayerAI implements PlayerInterface {

    private IPlayer player;
    private IStrategy strategy;

    /**
     * Constructor takes in an IPlayer and their IStrategy
     * @param player IPlayer
     * @param strategy IStrategy
     */
    public PlayerAI(IPlayer player, IStrategy strategy) {
        if (player == null || strategy == null) {
            throw new IllegalArgumentException("IPlayer and IStrategy cannot be null");
        }

        this.player = player;
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
    public IPlayer getPlayer() {
        return this.player;
    }
}
