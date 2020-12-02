package util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.awt.Point;
import java.util.List;
import model.state.GameState;
import model.state.IGameState;
import model.state.IPenguin;
import model.state.IPlayer;
import model.tree.Action;
import model.tree.MovePenguin;
import model.tree.PlacePenguin;

public class PlayerUtil {

    GameStateUtil stateUtil;

    public PlayerUtil() {
        this.stateUtil = new GameStateUtil();
    }

    public JsonObject GameStateToJson(IGameState gameState) {
       return this.stateUtil.GameStateToJson(gameState);
    }

    public IGameState JsonToGameState(JsonArray board, JsonArray players) {
        return this.stateUtil.JsonToGameState(board, players);
    }

    public JsonArray GameActionsToJson(List<Action> actions) {
        JsonArray jsonActions = new JsonArray();
        for (Action action : actions) {
            jsonActions.add(moveToJson(action));
        }
        return jsonActions;
    }

    public JsonArray moveToJson(Action action) {
        JsonArray jsonAction = new JsonArray();
        JsonArray from = pointToJson(action.getFromPosition());
        JsonArray to = pointToJson(action.getToPosition());
        jsonAction.add(from);
        jsonAction.add(to);
        return jsonAction;
    }

    public Action toPlacementAction(JsonElement place, IGameState state) {
        try {
            JsonArray placeMove = place.getAsJsonArray();
            IPlayer player = state.playerTurn();
            Point point = new Point(placeMove.get(0).getAsInt(), placeMove.get(1).getAsInt());

            return new PlacePenguin(player, point);
        } catch (Exception e) {
            return null;
        }
    }

    public Action toMoveAction(JsonElement action, IGameState state) {
        try {
            JsonArray move = action.getAsJsonArray();
            IPlayer player = state.playerTurn();
            JsonArray fromPosition = move.get(0).getAsJsonArray();
            JsonArray toPosition = move.get(1).getAsJsonArray();
            IPenguin playerPenguin = null;
            Point from = new Point(fromPosition.get(1).getAsInt(), fromPosition.get(0).getAsInt());
            Point to = new Point(toPosition.get(1).getAsInt(), toPosition.get(0).getAsInt());
            for (IPenguin penguin : player.getPenguins()) {
                if (penguin.getPosition().equals(from)) {
                    playerPenguin = penguin;
                }
            }
            if (playerPenguin == null) {
                return null;
            }
            return new MovePenguin(player, playerPenguin, to);
        } catch (Exception e) {
            return null;
        }
    }

    public JsonArray pointToJson(Point point) {
        JsonArray pos = new JsonArray();
        pos.add(point.x);
        pos.add(point.y);
        return pos;
    }

}
