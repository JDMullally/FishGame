package util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import model.IGameState;
import model.ImmutableGameState;
import model.Tile;

/**
 * Utility class for GameState deals with converting to and from JSON.
 */
public class GameStateUtil {

    /**
     * Formats the current GameState into a JSON Object and prints it out.
     *
     * @return JsonObject
     */
    public JsonObject GameStateToJson(ImmutableGameState gameState) {
        JsonObject jsonGameState = new JsonObject();
        JsonArray jsonBoard = new JsonArray();
        Tile[][] board = this.invert(gameState.getGameBoard().clone());
        for (Tile[] row : board) {
            JsonArray jsonRow = new JsonArray();
            for (Tile tile: row) {
                jsonRow.add(tile.getFish());
            }
            jsonBoard.add(jsonRow.deepCopy());
        }
        jsonGameState.add("board", jsonBoard);

        JsonArray jsonPosition = new JsonArray();
        jsonPosition.add(0);
        jsonPosition.add(0);

        jsonGameState.add("position", jsonPosition);

        return jsonGameState;
    }

    /**
     * Takes in a JSON reprentation of a GameState and returns an IGameState.
     *
     * @param gameState Json representation of a gamestate
     * @return the GameState created with the JsonObject
     */
    public IGameState JsonToGameState(JsonObject gameState) {
        return null; // TODO
    }

    /**
     * Inverts a Game Board representation.
     *
     * @param board the game board
     * @return inverted game board
     */
    private Tile[][] invert(Tile[][] board) {
        Tile[][] newBoard = new Tile[board.length][board.length];

        for (Tile[] col : board) {
            for (Tile tile: col) {
                newBoard[tile.getPosition().y][tile.getPosition().x] = tile.clone();
            }
        }
        return newBoard;
    }
}
