package model;

import com.google.gson.JsonObject;
import java.util.List;
import netscape.javascript.JSObject;

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
     * Gets the GameBoard as an array of array of Tiles.
     * @return Tile[][] representing GameBoard
     */
    Tile[][] getGameBoard();

    /**
     * Returns Tile the Player moved their Penguin to if the move was successful.
     * Otherwise it throws an IllegalArgumentException.
     * @param penguin
     * @param player
     * @param newTile
     * @param board
     * @return boolean whether move is valid or not.
     */
    Tile makeMove(Penguin penguin, Player player,
        Tile newTile, IGameBoard board) throws IllegalArgumentException;

    /**
     * Returns true if all players have skipped their turn and there are no more possible moves.
     * @return
     */
    boolean isGameOver();

    /**
     * Allows players to place penguins on the current GameBoard.
     * @param penguin Penguin owned by Player player
     * @param player Player placing the Penguin
     * @param tile Tile the Player is placing Penguin.
     * @return
     */
    Penguin placePenguin(Penguin penguin, Player player, Tile tile);

    /**
     * Formats the current GameState into a JSON Object and prints it out.
     */
    JsonObject GameStateToJson();

    /**
     *
     * @param gameState
     * @return the GameState created with the JsonObject
     */
    IGameState JsonToGameState(JsonObject gameState);
}
