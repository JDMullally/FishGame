package util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import controller.Controller;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import model.games.GameAction;
import model.games.IGameResult;
import model.games.IReferee;
import model.games.PlayerAI;
import model.games.Referee;
import model.state.ImmutableGameState;
import model.state.ImmutableGameStateModel;
import model.strategy.IStrategy;
import model.strategy.Strategy;
import model.tree.PlayerInterface;
import view.IView;
import view.VisualView;

public class RefereeTestUtil {

    public static void main(String[] args) throws IOException {
        test();
    }

    private static void test() throws IOException {
        Gson gson = new Gson();
        InputStreamReader reader = new InputStreamReader(System.in);
        JsonStreamParser parser = new JsonStreamParser(reader);
        JsonObject element;

        if (parser.hasNext()) {
            element = parser.next().getAsJsonObject();
        } else {
            return;
        }

        int row = element.get("row").getAsInt();
        int col = element.get("column").getAsInt();
        int fish = element.get("fish").getAsInt();

        JsonArray players = element.get("players").getAsJsonArray();

        LinkedHashMap<PlayerInterface, String> interfacePlayers
            = jsonPlayersToInterfacePlayers(players);

        List<PlayerInterface> gamePlayers = new ArrayList<>(interfacePlayers.keySet());

        IReferee referee = new Referee(gamePlayers, row, col, fish);

        referee.runGame();

        IGameResult result = referee.getGameResult();

        List<PlayerInterface> playerPlacements = result.getWinners();

        JsonArray winners = new JsonArray();

        for (PlayerInterface p : playerPlacements) {
            winners.add(interfacePlayers.get(p));
        }
        List<GameAction> actions = referee.getOngoingActions();

        actions.forEach(System.out::println);

        System.out.println(winners);
    }

    private static LinkedHashMap<PlayerInterface, String> jsonPlayersToInterfacePlayers(JsonArray players) {
        LinkedHashMap<PlayerInterface, String> map = new LinkedHashMap<>();
        for (JsonElement player: players) {
            String name = player.getAsJsonArray().get(0).getAsString();
            int depth = player.getAsJsonArray().get(1).getAsInt();

            IStrategy strategy = new Strategy();
            PlayerInterface newPlayer = new PlayerAI(strategy, depth);

            map.put(newPlayer, name);
        }
        return map;
    }
}
