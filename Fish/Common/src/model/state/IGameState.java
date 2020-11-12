package model.state;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;

import model.board.IGameBoard;
import model.board.Tile;

/**
 * An IGameState is a IGameBoard that has a List of Players playing the game.
 * It needs to manage more information which continuing to utilize IGameBoard methods.
 *
 * Represents the GameState for Fish. A GameState should be able to change the GameBoard,
 * show it's Players, add Players to the game, let Players place Penguins,
 * get the possible moves for a Penguin, and check if the game is over.
 */
public interface IGameState extends IGameBoard {

    /**
     * Gets the list of the current Players
     * @return List of IPlayers
     */
    List<IPlayer> getPlayers();

    /**
     * Returns the IPlayer whose turn it is to move. The IPlayer who's turn it is to move is always
     * the first IPlayer in the List of IPlayer's. This is kept track of by shifting the position
     * of players in the List of Players; Once a player moves, their IPlayer is moved to the back of
     * the List of Players.
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
    LinkedHashMap<IPenguin, List<Tile>> getPossibleMoves(IPlayer player);

    /**
     * Returns a List of Tiles that do not have holes or penguins currently on them.  The List is
     * organized following a zig zag pattern that starts at the top left corner. The list
     * goes from top to down in each column and moves right when a column is filled.
     *
     * @return List of Tile
     */
    List<Tile> getPenguinPlacementTiles();

    /**
     * Allows players to place penguins on the current GameBoard.
     * Effect: Adds the specified penguin to the player.
     *
     * @param player Player placing the Penguin
     * @param tile Tile the Player is placing Penguin
     * @return IGameState
     */
    IGameState placePenguin(IPlayer player, Tile tile) throws IllegalArgumentException;

    /**
     * Allows players to place penguins on the current GameBoard.
     * Effect: Adds the specified penguin to the player.
     *
     * @param player Player placing the Penguin
     * @param point Point the Player is placing Penguin
     * @return IGameState
     */
    IGameState placePenguin(IPlayer player, Point point) throws IllegalArgumentException;

    /**
     * Moves a Player's Penguin from it's currentTile to a newTile, or throws an error if the move
     * isn't valid.
     *
     * @param player the IPlayer moving
     * @param penguin the IPenguin moving
     * @param newTile the Tile to move to
     * @param pass if the IPlayer would like to pass their turn
     * @return the game state if the move is successful
     *
     * @throws IllegalArgumentException if the move is invalid
     */
    IGameState move(IPlayer player, IPenguin penguin, Tile newTile, boolean pass) throws IllegalArgumentException;

    /**
     * Moves a Player's Penguin from it's currentPoint to a newPoint, or throws an error if the move
     * isn't valid.
     *
     * @param player the IPlayer moving
     * @param penguin the IPenguin moving
     * @param newPoint the Point to move to
     * @param pass if the IPlayer would like to pass their turn
     * @return the game state if the move is successful
     *
     * @throws IllegalArgumentException if the move is invalid
     */
    IGameState move(IPlayer player, IPenguin penguin, Point newPoint, boolean pass) throws IllegalArgumentException;

    /**
     * Removes an IPlayer from the game. This is called as a result of an IPlayer cheats or performs
     * some kind of misbehavior.
     *
     * @param player IPLayer being removed
     * @return the IPLayer being removed
     */
    IGameState removePlayer(IPlayer player);

    /**
     * Returns true if the current player cannot make a move with any of their penguins.
     *
     * @return boolean
     */
    boolean isCurrentPlayerStuck();

    /**
     * Returns true if the game is ready to played (penguin placement phase is over).
     *
     * @return boolean
     */
    boolean isGameReady();

    /**
     * Returns true if no players can perform an action (placements or moves) and false otherwise.
     *
     * @return boolean
     */
    boolean isGameOver();

    /**
     * Returns a clone of the current gameState.
     *
     * @return IPlayer
     */
    IGameState clone();
}
