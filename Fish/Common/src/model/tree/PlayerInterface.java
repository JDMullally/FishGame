package model.tree;

import java.util.concurrent.TimeoutException;
import model.games.IGameResult;
import model.state.IGameState;

/**
 * This interface represents a Player's interface for interacting with the game.
 * The Player interface must spell out how to place their avatars on the initial board,
 * how to take turns, and how/whether to receive information about the end of a game.
 */
public interface PlayerInterface {

    /**
     * A Player takes in an IGameState from a Referee. The player then returns an Action to place a
     * Penguin. This method can only be called during the initial place penguin phase of the game.
     *
     * @param state IGameState
     * @return Action
     */
    Action placePenguin(IGameState state) throws TimeoutException;

    /**
     * A Player takes in an IGameState from a Referee. The player then returns an Action to make a
     * move. This method can only be called after the initial place penguin phase of the game.
     *
     * @param state IGameState
     * @return Action
     */
    Action movePenguin(IGameState state) throws TimeoutException;

    /**
     * Returns an int representing how old the player is in years.
     *
     * @return int
     */
    int getPlayerAge();

    /**
     * Returns a String that represents the players unique identifier in a tournament.
     *
     * @return String
     */
    String getPlayerID();

    /**
     * Return true for confirmation that player has received the message
     *
     * @return boolean for confirmation that player has received the message
     */
    boolean tournamentHasStarted();

    /**
     * Return true for confirmation that player has received the message.
     *
     * @return boolean to acknowledge that this message was received.
     */
    boolean gameHasStarted();

    /**
     * Informs the player that they have been removed from the tournament for cheating.
     */
    void kickedForCheating();

    /**
     * Returns the result of a Game that the player had been playing in. This includes winners,
     * losers and cheaters.
     *
     * @param result IGameResult
     */
    void gameResults(IGameResult result);

    /**
     * Return true for confirmation that player has received the message
     *
     * @param youWon a boolean that is true if the player has won or false if they have lost
     * @return boolean for confirmation that player has received the message
     */
    boolean tournamentResults(boolean youWon);
}
