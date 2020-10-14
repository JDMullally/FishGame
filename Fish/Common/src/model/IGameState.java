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
     * Returns all possible moves (from any penguin) for a given player as a Map of Penguin to List
     * of Tile.
     *
     * @param player the player who want's to see their possible moves
     * @return Map of Penguin to List of Tile
     */
    Map<Penguin, List<Tile>> getPossibleMoves(Player player);

    /**
     * Allows players to place penguins on the current GameBoard.
     *
     * @param penguin Penguin owned by Player player
     * @param player Player placing the Penguin
     * @param tile Tile the Player is placing Penguin.
     * @return
     */
    Penguin placePenguin(Penguin penguin, Player player, Tile tile);

    /**
     * Moves a Player's Penguin from it's currentTile to a newTile, or throws an error if the move
     * isn't valid.
     *
     * @param player
     * @param penguin
     * @param currentTile
     * @param newTile
     * @throws IllegalArgumentException
     */
    boolean move(Player player, Penguin penguin, Tile currentTile, Tile newTile) throws IllegalArgumentException;

    /**
     * Moves a Player's Penguin from it's currentPoint to a newPoint, or throws an error if the move
     * isn't valid.
     *
     * @param player
     * @param penguin
     * @param currentPoint
     * @param newPoint
     * @throws IllegalArgumentException
     */
    boolean move(Player player, Penguin penguin, Point currentPoint, Point newPoint) throws IllegalArgumentException;

    /**
     * Returns true if no players can make a move and false otherwise.
     *
     * @return boolean
     */
    boolean isGameOver();

    /**
     * Formats the current GameState into a JSON Object and prints it out.
     *
     * @return
     */
    JsonObject GameStateToJson();

    /**
     *
     * @param gameState
     * @return the GameState created with the JsonObject
     */
    IGameState JsonToGameState(JsonObject gameState);
}
