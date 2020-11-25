package model.humans;

import model.games.IGameAction;
import model.state.ImmutableGameStateModel;


/**
 * Interface for a GameVisualizer.  A GameVisualizer should be able to render a game and a single
 * scene of that game.
 */
public interface IGameVisualizer {

    /**
     * Sends all Observers the most recent GameAction.
     */
    void updateAllObserversAction(IGameAction action);

    /**
     * Sends all Observers the results of a Game Once it is over.
     * To do this, this method calls
     */
    void updateAllObserverResults();

    /**
     * Sends all Observers the Initial State as an ImmutableGameStateModel.
     */
    void updateAllObserverInitialState();

    /**
     * Returns the next ImmutableGameStateModel of a Game using the given IGameAction and
     * ImmutableGameStateModel.
     *
     * @param action
     * @param currentImmutableGameState
     * @return ImmutableGameStateModel that represents the next scene in a Game.
     */
    ImmutableGameStateModel nextScene(IGameAction action, ImmutableGameStateModel currentImmutableGameState);

    /**
     * Sends a full game to all observers.
     *
     * @throws InterruptedException if sendGame is interrupted before it finishes sending an entire
     * game.
     */
     void sendGame() throws InterruptedException;
}
