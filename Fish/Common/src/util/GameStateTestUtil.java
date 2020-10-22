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

            IGameState state;
            try {
                state = util.JsonToGameState(board, players);
            } catch (Exception e) {
                System.out.println("False");
                return;
            }

            IPlayer player = state.playerTurn();
            IPenguin penguin = player.getPenguins().get(0);

            List<Tile> tilesToMove = state.getPossibleMoves(player).entrySet().iterator().next().getValue();

            if (tilesToMove.size() == 0) {
                System.out.println("False");
            } else {

                IGameState endState;
                try {
                    endState = state.move(player, penguin, tilesToMove.get(0), false);
                } catch (Exception e) {
                    System.out.println("False");
                    return;
                }

                // shifts player locations
                List<IPlayer> ps = endState.getPlayers();
                IPlayer last = ps.remove(ps.size() - 1);
                ps.add(0, last);

                IGameState printState = new GameState(endState.getRows(), endState.getColumns(), endState.getGameBoard(), ps);
                System.out.println(new GameStateUtil().GameStateToJson(printState).toString());
            }
        }
    }
}
