package model.tree;

import model.state.IGameState;

/**
 * An action represents something that effects an IGameState.
 */
public interface Action {

    /**
     * Applies the action to the given game state and returns the resulting game state
     *
     * @param state given IGameState
     * @return resulting IGameState
     */
    IGameState apply(IGameState state);
}
