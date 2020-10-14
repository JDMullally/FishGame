package model;

import com.google.gson.JsonObject;

import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Represents the GameState for Fish.
 */
public interface IGameState {

    /**
     * Gets the list of the current Players
     * @return List of IPlayers
     */
    List<IPlayer> getPlayers();

    /**
     * Returns the number of turns that have been played in the game
     *
     * @return int
     */
    int getTurn();

    /**
     * Returns the IPlayer whose turn it is to move.
     *
     * @return IPlayer
     */
    IPlayer playerTurn();

    /**
     * Returns all possible moves (from any penguin) for a given player as a Map of Penguin to List
     * of Tile.
     *
     * @param player the player who want's to see their possible moves
     * @return Map of Penguin to List of Tile
     */
    Map<IPenguin, List<Tile>> getPossibleMoves(IPlayer player);

    /**
     * Allows players to place penguins on the current GameBoard.
     *
     * @param penguin Penguin owned by Player player
     * @param player Player placing the Penguin
     * @param tile Tile the Player is placing Penguin.
     * @return IPenguin that was placed
     */
    IPenguin placePenguin(IPenguin penguin, IPlayer player, Tile tile) throws IllegalArgumentException;

    /**
     * Moves a Player's Penguin from it's currentTile to a newTile, or throws an error if the move
     * isn't valid.
     *
     * @param player
     * @param penguin
     * @param newTile
     * @throws IllegalArgumentException
     */
    boolean move(IPlayer player, IPenguin penguin, Tile newTile, boolean pass) throws IllegalArgumentException;

    /**
     * Moves a Player's Penguin from it's currentPoint to a newPoint, or throws an error if the move
     * isn't valid.
     *
     * @param player
     * @param penguin
     * @param newPoint
     * @throws IllegalArgumentException
     */
    boolean move(IPlayer player, IPenguin penguin, Point newPoint, boolean pass) throws IllegalArgumentException;

    /**
     * Returns true if no players can make a move and false otherwise.
     *
     * @return boolean
     */
    boolean isGameOver();
}
