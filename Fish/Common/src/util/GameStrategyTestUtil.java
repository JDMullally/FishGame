package util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import controller.Controller;
import model.state.GameState;
import model.state.IGameState;
import model.state.ImmutableGameState;
import model.state.ImmutableGameStateModel;
import model.strategy.Strategy;
import model.tree.Action;
import view.IView;
import view.VisualView;

/**
 * Tests the GameStrategy for the Fish Game.
 */
public class GameStrategyTestUtil {

    /**
     * Entry point for the application.
     *
     * @param args program arguments
     */
    public static void main(String[] args) throws IOException {
        new GameStrategyTestUtil().test();
    }

    /**
     * Reads in JSON for STDIN and produces GameStrategy result to STDOUT
     */
    private void test() throws IOException {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        // reads input
        List<?> list = gson.fromJson(bufferedReader, List.class);

        // gets the depth and state
        int depth = parser.parse(list.get(0).toString()).getAsInt();
        JsonObject state = parser.parse(list.get(1).toString()).getAsJsonObject();
        bufferedReader.close();

        GameStateUtil util = new GameStateUtil();

        JsonArray board = state.get("board").getAsJsonArray();
        JsonArray players = state.get("players").getAsJsonArray();
        IGameState gameState;
        try {
            gameState = util.JsonToGameState(board, players);
            if (gameState.isCurrentPlayerStuck() || gameState.isGameOver()) {
                throw new IllegalArgumentException("Player can't move");
            }
        } catch (Exception e) {
            System.out.println("false");
            return;
        }

        // Todo: Remove
        ImmutableGameStateModel immutableModel = new ImmutableGameState((GameState) gameState);

        IView view = new VisualView(immutableModel);
        Controller controller = new Controller();
        controller.control((GameState) gameState, view);

        Strategy strategy = new Strategy();
        try {
            // takes actions and applies it to the game state
            Action bestAction = strategy.chooseMoveAction(gameState, depth);

            // builds json output
            Point from = bestAction.getFromPosition();
            Point to = bestAction.getToPosition();

            JsonArray fromArray = new JsonArray();
            fromArray.add(from.y);
            fromArray.add(from.x);

            JsonArray toArray = new JsonArray();
            toArray.add(to.y);
            toArray.add(to.x);

            JsonArray res = new JsonArray();
            res.add(fromArray);
            res.add(toArray);

            System.out.println(res);
        } catch (Exception e) {
            System.out.println("false");
        }
    }
}
