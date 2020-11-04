package model.games;

import java.util.List;

import model.board.Tile;
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
 */
public interface IReferee {

    /**
     * Returns the current GameState of the game the referee is supervising.
     *
     * @return IGameState
     * @throws IllegalStateException If the game has not yet started
     */
    IGameState getGameState() throws IllegalStateException;

    /**
     * Returns the most recent GameAction that has occurred in a Referee's Game.
     *
     * @return GameAction
     */
    List<GameAction> getOngoingActions();

    /**
     * Returns the Result of a Referee's Game.
     *
     * @return IGameResult
     * @throws IllegalStateException If the game has not yet concluded
     */
    IGameResult getGameResult() throws IllegalStateException;

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
     * @param rows The number of rows on the GameBoard
     * @param columns the number of columns on the GameBoard
     * @return IGameResult
     */
    IGameResult runGame(int rows, int columns);
}
