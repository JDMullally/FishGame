package util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import model.IGameState;
import model.Tile;

public class GameStateUtil {
    //TODO write tests to confirm jsonBoard is correct
    @Override
    public JsonObject GameStateToJson() {
        JsonObject jsonGameState = new JsonObject();
        JsonArray jsonBoard = new JsonArray();
        Tile[][] board = this.invert(this.getGameBoard().clone());
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

    private Tile[][] invert(Tile[][] board) {
        Tile[][] newBoard = new Tile[board.length][board.length];

        for (Tile[] col : board) {
            for (Tile tile: col) {
                newBoard[tile.getPosition().y][tile.getPosition().x] = tile.clone();
            }
        }
        return newBoard;
    }

    @Override
    public IGameState JsonToGameState(JsonObject gameState) {
        return null;
    }
}
