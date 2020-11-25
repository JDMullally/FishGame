package model.humans;

import model.games.IGameAction;
import model.games.IGameResult;
import model.state.ImmutableGameStateModel;

/**
 * An IGameObserver will be able to view the game based on the given state. It will get sent initial ImmutableGameStateModel,
 * for a game run by a Referee, update the GameObserver with next Action to be performed on the current state, and the results of a game.
 *
 */
public interface IGameObserver {

    /**
     * sends the initial ImmutableGameStateModel for a game run by a Referee.
     * @param newState
     */
    void sendInitialState(ImmutableGameStateModel newState);

    /**
     * Updates the GameObserver with next Action to be performed on the current state.
     * @param action
     */
    void updateAction(IGameAction action);

    /**
     * Updates the GameObserver with the results of the game.
     * @param result IGameResult
     */
    void updateResults(IGameResult result);
}
