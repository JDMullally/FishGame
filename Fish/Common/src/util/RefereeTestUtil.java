package util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.games.GameAction;
import model.games.IGameResult;
import model.games.IReferee;
import model.games.PlayerAI;
import model.games.Referee;
import model.strategy.IStrategy;
import model.strategy.Strategy;
import model.tree.PlayerInterface;

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

        List<PlayerInterface> interfacePlayers = jsonPlayersToInterfacePlayers(players);

        List<PlayerInterface> gamePlayers = new ArrayList<>(interfacePlayers);

        IReferee referee = new Referee(gamePlayers, row, col, fish);

        referee.runGame();

        IGameResult result = referee.getGameResult();

        List<PlayerInterface> playerPlacements = result.getWinners();

        List<String> winners = new ArrayList();

        for (PlayerInterface p : playerPlacements) {
            winners.add(p.getPlayerID());
        }
        List<GameAction> actions = referee.getOngoingActions();

        actions.forEach(System.out::println);

        Collections.sort(winners);

        JsonArray winnersJson = new JsonArray();

        for (String winner: winners) {
            winnersJson.add(winner);
        }

        System.out.println(winnersJson);
    }

    private static List<PlayerInterface> jsonPlayersToInterfacePlayers(JsonArray players) {
        List<PlayerInterface> newPlayers = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            String name = players.get(i).getAsJsonArray().get(0).getAsString();
            int depth = players.get(i).getAsJsonArray().get(1).getAsInt();

            IStrategy strategy = new Strategy();
            PlayerInterface newPlayer = new PlayerAI(strategy, depth, i + 1 ,name);

            newPlayers.add(newPlayer);
        }
        return newPlayers;
    }
}
