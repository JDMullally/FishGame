package model.tree;

import java.awt.Point;
import java.util.Map;

import model.state.IGameState;
import model.state.IPenguin;
import model.state.IPlayer;

/**
 * This interface represents a Player's interface for interacting with the game.
 * The Player interface must spell out how to place their avatars on the initial board,
 * how to take turns, and how/whether to receive information about the end of a game.
 */
public interface PlayerInterface {

    /**
     * Returns a copy of the current GameState.
     *
     * @return IGameState representing the current game state
     */
    IGameState getGameState();

    /**
     * Returns a copy of a complete game tree. Every time the method is called again, a new subtree
     * for the bottom most node(s) in the tree will be created. i.e. the resulting IGameTree that
     * is returned will have 1 more depth to it.
     *
     * @return IGameTree representing a complete game tree
     */
    IGameTree getGameTree();

    /**
     * Returns 0 if it is the given Player's turn, or an integer between 1 and the number of
     * Players in the current game minus one. This other number lets the player know their placement
     * in the queue (how many turns until it is their turn).
     *
     * @param player IPlayer's turn
     * @return int representing the player's placement in the queue of turns
     */
    int getTurn(IPlayer player);

    /**
     * Returns the score a given player has accumulated across all of their penguins in the game.
     *
     * @param player IPlayer who's checking their score
     * @return the player's score as an int
     */
    int getPlayerScore(IPlayer player);

    /**
     * Returns a Map of potential Points to move to mapped to the resulting IGameState of that move.
     *
     * @param game IGameState being moved on
     * @param player IPlayer playing moving
     * @param penguin IPenguin moving
     * @return Map of Point to IGameState
     */
    Map<Point, IGameState> getPossibleMoves(IGameState game, IPlayer player, IPenguin penguin);

    /**
     * Allows the Player to add themself to the game. If the game already has 4 players, an error
     * will be thrown.
     *
     * @param player IPlayer being added to the game
     * @return IPlayer that is added to the game
     * @throws IllegalArgumentException if the player cannot be added to the game (if there is
     * already 4 players in the game)
     */
    IPlayer addPlayer(IPlayer player) throws IllegalArgumentException;

    /**
     * Allows the Player to queue up the placement of their penguin/ If they attempt to place the
     * penguin in an invalid spot, an Illegal Argument Exception will be thrown.
     *
     * A penguin placement is invalid for a given point if:
     * - The point is outside the bounds of the board
     * - The point is a hole on the board
     * - The point is already occupied by another penguin
     * - The player has already placed their maximum number of penguins
     *
     * @param player IPlayer placing penguin
     * @param penguin IPenguin being placed
     * @param point Point to place penguin
     * @return IPenguin that the Player Placed
     * @throws IllegalArgumentException if the penguin cannot be placed
     */
    IPenguin queuePlacePenguin(IPlayer player, IPenguin penguin, Point point) throws IllegalArgumentException;

    /**
     * Allows a Player to queue a move to be played on their turn. If the move is invalid, throw an
     * error. This is the primary way a Player will interact with the Game.
     *
     * A move is invalid for a given point if:
     * - The point is outside the bounds of the board
     * - The point is a hole on the board
     * - The point is already occupied by another penguin
     * - The point is not reachable from the penguin's current position
     *
     * @param player IPlayer playing moves
     * @param penguin IPenguin moving
     * @param newPoint Point to move to
     * @throws IllegalArgumentException If the move is invalid
     */
    void queueMove(IPlayer player, IPenguin penguin, Point newPoint) throws IllegalArgumentException;

    /**
     * Allows a Player to check whether the game is over. A game is over if no player in the game
     * is able to make a move.
     *
     * @return True if the game is over and False otherwise
     */
    boolean isGameOver();
}
