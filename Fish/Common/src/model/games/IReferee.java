package model.games;

/**
 * Interface for a referee component, which can run a complete Fish game for a sequence of players.
 *
 * A referee can get the outcome of a Game and the most Recent Action that has occurred in the Game.
 */
public interface IReferee {

    /**
     * Returns the Result of a Referee's Game. If the game has not yet concluded, the method throws
     * an Illegal State Exception.
     *
     * @return IGameResult
     */
    IGameResult getGameResult() throws IllegalStateException;

    /**
     * Returns the most recent GameAction that has occurred in a Referee's Game.
     *
     * @return GameAction
     */
    GameAction getGameAction();

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
    IGameResult startGame();
}
