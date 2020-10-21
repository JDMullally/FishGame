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

import controller.Controller;
import model.board.Tile;
import model.state.GameState;
import model.state.IGameState;
import model.state.IPenguin;
import model.state.IPlayer;
import model.state.ImmutableGameState;
import model.state.ImmutableGameStateModel;
import view.IView;
import view.VisualView;

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
        JsonArray players = null;
        JsonArray board = null;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();

            if (key.equals("players")) {
                players = parser.parse(value).getAsJsonArray();
            } else if (key.equals("board")) {
                board = parser.parse(value).getAsJsonArray();
            }
        }
        bufferedReader.close();


        // creates GameState
        if (board != null && players != null) {
            GameStateUtil util = new GameStateUtil();
            IGameState state = util.JsonToGameState(board, players);

            // sets game board, view, model and runs them
            ImmutableGameStateModel immutableModel = new ImmutableGameState((GameState) state);

            IView view = new VisualView(immutableModel);
            Controller controller = new Controller();
            controller.control((GameState) state, view);

            IPlayer player = state.playerTurn();
            IPenguin penguin = player.getPenguins().get(0);

            List<Tile> tilesToMove = state.getPossibleMoves(player).entrySet().iterator().next().getValue();

            IGameState endState = state.move(player, penguin, tilesToMove.get(0), false);

            if (endState == null) {
                System.out.println("False");
            } else {
                System.out.println(new GameStateUtil().GameStateToJson(endState).toString());
            }
        }
    }
}
