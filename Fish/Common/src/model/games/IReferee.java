package model.games;

import java.util.List;

import model.state.IGameState;

/**
 * Interface for a referee component, which can run a complete Fish game for a sequence of players.
 *
 * A referee can get the current GameState, ongoing GameActions, the outcome of a Game, and of
 * course run the game itself.
 *
 * Abnormal conditions (cheating) that the referee takes care of resulting in the removal of a
 * player include:
 * - Trying to place a penguin somewhere that isn't on the GameBoard
 * - Trying to place a penguin on another player's penguin
 * - Trying to place more penguins than the player is allowed
 * - Trying to move another player's penguin
 * - Trying to move a penguin when it isn't the player's turn
 * - Trying to move a penguin to a location that isn't on the GameBoard
 * - Trying to move a penguin to a location that is empty (a hole)
 * - Trying to move a penguin to a location already occupied by another penguin
 * - Trying to move a penguin to a location which it cannot reach
 *
 * An Abnormal condition (cheating) that the referee must take care of of resulting in the removal
 * of a player specifically for remote communication include:
 * - Taking too long to make a move (timeout)
 * - Malformed Input
 */
public interface IReferee {

    /**
     * Runs a Game for this Referee's players and returns the result of the game as an IGameResult.
     * While the game runs, each action will be recorded in the Referee's List of actions and each
     * IGameState will be recorded in the referee's current GameState.
     *
     * The referee will ask each player to provide a valid action and apply it to the
     * IGameState
     *
     * If a player is caught cheating, they will be added to a list of cheating players.
     * The referee will then remove them from the game and their personal list of players
     * and continue to run the game.
     *
     * @return IGameResult
     */
    IGameResult runGame();

    /**
     * Returns the initial state of the game the referee is supervising.
     *
     * @return IGameState
     * @throws IllegalStateException If the IGameState has not been generated yet.
     */
    IGameState getInitialGameState() throws IllegalStateException;

    /**
     * Returns the current state of the game the referee is supervising.
     *
     * @return IGameState
     * @throws IllegalStateException If the game hasn't started yet.
     */
    IGameState getGameState() throws IllegalStateException;

    /**
     * Returns a list of all recent GameAction that has occurred in a Referee's Game.
     *
     * @return List of GameAction
     */
    List<IGameAction> getOngoingActions();

    /**
     * Returns the Result of a Referee's Game.
     *
     * @return IGameResult
     * @throws IllegalStateException If the game has not yet concluded
     */
    IGameResult getGameResult() throws IllegalStateException;
}
