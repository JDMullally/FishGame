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

    private IStrategy strategy;
    IPlayer player;

    PlayerAI(IStrategy strategy, IPlayer player) {
        if (strategy == null || player == null) {
            throw new IllegalArgumentException("Cannot have a null IStrategy or IPlayer");
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
}
