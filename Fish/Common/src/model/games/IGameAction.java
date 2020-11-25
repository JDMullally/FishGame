package model.games;

import model.state.ImmutableGameStateModel;

/**
 * Interface for an IGameAction.  A Game action is a visual representation of an Action. It acts as
 * an Immutable action that can only be used on ImmutableGameStateModels to get the next
 * ImmutableGameStateModel.
 */
public interface IGameAction {

    /**
     * Gets the next ImmutableGameStateModel based on the current Action the GameAction holds and the
     * given ImmutableGameStateModel
     * @param model
     * @return
     */
    ImmutableGameStateModel getNextState(ImmutableGameStateModel model);

}
