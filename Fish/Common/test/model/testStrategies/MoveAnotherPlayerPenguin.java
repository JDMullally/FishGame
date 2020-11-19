package model.testStrategies;

import model.state.IGameState;
import model.strategy.IStrategy;
import model.strategy.Strategy;
import model.tree.Action;
import model.tree.PassPenguin;

/**
 * Represents a Strategy that will always try to make an Invalid move.
 *
 * It places a penguin in the next available free spot following a zig zag pattern that starts at
 * the top left corner for the Placement phase of Fish.
 *
 * When asked to make a move, the strategy will create a state where they have already taken their
 * turn using the standard in-house AI method. It will then attempt to pass that player's turn.
 * This AI attempts to abuse the fact that a state has public methods and can be manipulated.
 *
 * This strategy is designed to break rules about player movement and should be removed from the
 * game for cheating.
 *
 * This Strategy is strictly for testing.  An In-House AI will never use this Strategy in a
 * Standard Game of Fish.
 */
public class MoveAnotherPlayerPenguin implements IStrategy {

    private IStrategy strategy;

    /**
     * Constructor for Moving Another Player's Penguin only needs to create a new In-House AI
     * Strategy.
     */
    public MoveAnotherPlayerPenguin() {
        this.strategy = new Strategy();
    }

    @Override
    public Action choosePlacePenguinAction(IGameState state) {
        return this.strategy.choosePlacePenguinAction(state);
    }

    @Override
    public Action chooseMoveAction(IGameState state, int turns) {
        Action action = this.strategy.chooseMoveAction(state, turns);
        state = action.apply(state);
        Action passOtherPlayer = new PassPenguin(state.playerTurn());
        return passOtherPlayer;
    }
}
