package util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.List;

import model.state.GameState;
import model.state.IGameState;
import model.state.IPenguin;
import model.state.IPlayer;
import model.board.Tile;

/**
 * Utility class for GameState deals with converting to and from JSON.
 */
public class GameStateUtil {

    /**
     * Formats the current GameState into a JSON Object and prints it out. If the IPlayer first is
     * not null, then iterate through the list setting the order such that the given player is first
     *
     * @param gameState the IGameState to convert to JSON
     * @param first the First player in the list of players or optionally null
     * @return JsonObject
     */
    public JsonObject GameStateToJson(IGameState gameState, IPlayer first) {
        JsonObject jsonGameState = new JsonObject();

        // shifts order of players in list
        List<IPlayer> players = new ArrayList<>();
        if (first != null) {
            boolean foundPlayer = false;
            int counter = 1;
            for (IPlayer player : gameState.getPlayers()) {
                if (player.equals(first)) {
                    players.add(0, player);
                    foundPlayer = true;
                } else if (foundPlayer) {
                    players.add(counter, player);
                    counter++;
                } else {
                    players.add(player);
                }
            }

            if (!foundPlayer) {
                throw new IllegalArgumentException("Couldn't find first player");
            }
        }

        // creates player object
        JsonArray jsonPlayers = new JsonArray();
        for (IPlayer player : players) {
            JsonObject o = new JsonObject();

            List<IPenguin> penguins = player.getPenguins();
            JsonArray penguinArray = new JsonArray();
            for (IPenguin p : penguins) {
                JsonArray point = new JsonArray();
                point.add(p.getPosition().y);
                point.add(p.getPosition().x);
                penguinArray.add(point);
            }

            o.add("color", new JsonPrimitive(ColorUtil.toColorString(player.getColor())));
            o.add("score", new JsonPrimitive(player.getScore()));
            o.add("places", penguinArray);
            jsonPlayers.add(o);
        }

        jsonGameState.add("players", jsonPlayers);

        // creates gameboard object
        JsonArray jsonBoard = new JsonArray();
        Tile[][] board = gameState.getGameBoard().clone();
        for (Tile[] row : board) {
            JsonArray jsonRow = new JsonArray();
            for (Tile tile: row) {
                jsonRow.add(tile.getFish());
            }
            jsonBoard.add(jsonRow.deepCopy());
        }
        jsonGameState.add("board", jsonBoard);

        return jsonGameState;
    }

    /**
     * Takes in a JSON reprentation of a GameState and returns an IGameState.
     *
     * @param board Json representation of a board
     * @param players Json representation of players
     * @return the GameState created with the JsonObject
     */
    public IGameState JsonToGameState(JsonArray board, JsonArray players) {
        int rows = board.size();
        // TODO find the max column size
        int columns = board.get(0).getAsJsonArray().size();

        return new GameState(rows, columns, board, players);
    }

    /**
     * Inverts a Game Board representation.
     *
     * @param board the game board
     * @return inverted game board
     */
    private Tile[][] invert(Tile[][] board) {
        Tile[][] newBoard = new Tile[board[0].length][board.length];

        for (Tile[] col : board) {
            for (Tile tile: col) {
                newBoard[tile.getPosition().y][tile.getPosition().x] = tile.clone();
            }
        }
        return newBoard;
    }
}
