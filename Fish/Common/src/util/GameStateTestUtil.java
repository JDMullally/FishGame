package util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import model.GameState;
import model.Tile;

/**
 * Tests the GameState for the Fish Game.
 */
public class GameStateTestUtil {

    /**
     * Entry point for the application.
     *
     * @param args program arguments
     */
    public static void main(String[] args) throws IOException {
        new GameStateTestUtil().test();
    }

    /**
     * Reads in JSON for STDIN and produces GameState result to STDOUT
     */
    private void test() throws IOException {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        // reads input
        Map<?, ?> map = gson.fromJson(bufferedReader, Map.class);

        // gets the position and board
        JsonArray position = null;
        JsonArray board = null;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();

            if (key.equals("position")) {
                position = parser.parse(value).getAsJsonArray();
            } else if (key.equals("board")) {
                board = parser.parse(value).getAsJsonArray();
            }
        }
        bufferedReader.close();

        // creates GameState
        if (board != null && position != null) {
            int rows = board.size();
            int columns = board.get(0).getAsJsonArray().size();

            GameState gameState = new GameState(rows, columns, board);

            Point point = new Point(position.get(1).getAsInt(), position.get(0).getAsInt());
            List<Tile> viableTiles = gameState.getViableTiles(point);

            System.out.println(viableTiles.size());
        }
    }
}
