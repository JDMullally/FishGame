package model;

import java.awt.Point;
import java.util.Map;

/**
 * This interface represents a Player's interface for interacting with the game.
 * The Player interface must spell out how to place their avatars on the initial board,
 * how to take turns, and how/whether to receive information about the end of a game.
 */
public interface PlayerInterface {

    /**
     * Allows the Player to queue up the placement of their penguin.
     * If they placed the penguin in an invalid spot, throw an Illegal Argument Exception.
     * @param gameState
     * @param player
     * @param penguin
     * @param point
     * @return IPenguin that the Player Placed.
     * @throws IllegalArgumentException
     */
    IPenguin queuePlacePenguin(IGameState gameState, IPlayer player,
        IPenguin penguin, Point point) throws IllegalArgumentException;

    /**
     * Returns 0 if it is the given Player's turn, or an integer between 1 and the number of
     * Players in the current IGame game.  This other number lets the player know their placement
     * in the queue.
     * @param player
     * @param game
     * @return int
     */
    int myTurn(IPlayer player, IGame game);

    /**
     * Returns a Map of Points to GameStates that allows players to see the states the moving to
     * that point with the input Penguin would yield.  This would work as the building blocks for
     * a tree of potential moves for an AI Player.
     * @param game
     * @param player
     * @param penguin
     * @return Map of Point to IGameState
     */
    Map<Point,IGameState> possibleMoves(IGame game, IPlayer player, IPenguin penguin);

    /**
     * Allows a Player to queue a move to be played on their turn.
     * If the move is invalid, throw an error.  This is the primary way a
     * Player will interact with the Game.
     * @param game
     * @param player
     * @param penguin
     * @param newPoint
     * @throws IllegalArgumentException
     */
    void queueMove(IGame game, IPlayer player,
        IPenguin penguin, Point newPoint) throws IllegalArgumentException;

    /**
     * Allows a Player to check whether the game is over.
     * @param game
     * @return
     */
    boolean isGameOver(IGame game);

    /**
     * Returns the points they have gathered across all of their penguins in this game.
     * @param gameState
     * @return int
     */
    int playerPoints(IGameState gameState);

    /**
     * Gets a copy of the current GameState
     * @return IGameState
     */
    IGameState getCurrentGameState();
}
