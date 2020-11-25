package model.humans;

import model.games.IGameAction;
import model.games.IGameResult;
import model.state.ImmutableGameStateModel;

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
