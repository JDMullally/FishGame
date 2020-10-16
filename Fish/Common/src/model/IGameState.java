package model;

import com.google.gson.JsonObject;

import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Represents the GameState for Fish. A GameState should be able to change the GameBoard,
 * show it's Players, add Players to the game, let Players place Penguins,
 * get the possible moves for a Penguin, and check if the game is over.
 */
public interface IGameState extends IGameBoard{

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
     * Adds a player to the GameState.
     *
     * @param player the player being added
     * @throws IllegalArgumentException if their are already 4 players in the game
     */
    void addPlayer(Player player) throws IllegalArgumentException;

    /**
     * Allows players to place penguins on the current GameBoard.
     * Effect: Adds the specified penguin to the player.
     *
     * @param penguin Penguin to be owned by Player player
     * @param player Player placing the Penguin
     * @param tile Tile the Player is placing Penguin.
     */
    void placePenguin(IPenguin penguin, IPlayer player, Tile tile) throws IllegalArgumentException;

    /**
     * Moves a Player's Penguin from it's currentTile to a newTile, or throws an error if the move
     * isn't valid.
     *
     * @param player the IPlayer moving
     * @param penguin the IPenguin moving
     * @param newTile the Tile to move to
     * @param pass if the IPlayer would like to pass their turn
     * @return true if the move is successful
     *
     * @throws IllegalArgumentException if the move is invalid
     */
    boolean move(IPlayer player, IPenguin penguin, Tile newTile, boolean pass) throws IllegalArgumentException;

    /**
     * Moves a Player's Penguin from it's currentPoint to a newPoint, or throws an error if the move
     * isn't valid.
     *
     * @param player the IPlayer moving
     * @param penguin the IPenguin moving
     * @param newPoint the Point to move to
     * @param pass if the IPlayer would like to pass their turn
     * @return true if the move is successful
     *
     * @throws IllegalArgumentException if the move is invalid
     */
    boolean move(IPlayer player, IPenguin penguin, Point newPoint, boolean pass) throws IllegalArgumentException;

    /**
     * Returns true if no players can make a move and false otherwise.
     *
     * @return boolean
     */
    boolean isGameOver();
}
