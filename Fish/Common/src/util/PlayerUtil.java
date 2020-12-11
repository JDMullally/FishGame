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


    /**
     * Takes in a JSON reprentation of a GameState and returns an IGameState.
     *
     * @param board Json representation of a board
     * @param players Json representation of players
     * @return the GameState created with the JsonObject
     */
    public IGameState JsonToGameStateMovement(JsonArray board, JsonArray players) {
        int rows = board.size();

        int columns = 0;

        for (JsonElement row : board) {
            columns = Math.max(columns, row.getAsJsonArray().size());
        }

        return new GameState(rows, columns, board, players, true);
    }

    /**
     * Takes in a JSON reprentation of a GameState and returns an IGameState.
     *
     * @param board Json representation of a board
     * @param players Json representation of players
     * @return the GameState created with the JsonObject
     */
    public IGameState JsonToGameStatePlacement(JsonArray board, JsonArray players) {
        int rows = board.size();

        int columns = 0;

        for (JsonElement row : board) {
            columns = Math.max(columns, row.getAsJsonArray().size());
        }

        return new GameState(rows, columns, board, players, false);
    }

    /**
     * Returns a JsonArray that represents a List of Actions
     *
     * @param actions List of Action that represents all actions made by other players
     * @return JsonArray that represents all actions made by other players
     */
    public JsonArray GameActionsToJson(List<Action> actions) {
        JsonArray jsonActions = new JsonArray();
        for (Action action : actions) {
            jsonActions.add(moveToJson(action));
        }
        return jsonActions;
    }

    /**
     * Returns a JsonArray that represents a single Action in a game
     *
     * @param action Action
     * @return JsonArray that represents a Json Formatted MovementAction
     */
    public JsonArray moveToJson(Action action) {
        JsonArray jsonAction = new JsonArray();
        JsonArray from = pointToJson(action.getFromPosition());
        JsonArray to = pointToJson(action.getToPosition());
        jsonAction.add(from);
        jsonAction.add(to);
        return jsonAction;
    }

    /**
     * Returns an Action that represents a Json Placement made on the given state
     *
     * @param place JsonArray
     * @param state IGameState
     * @return Action
     */
    public Action toPlacementAction(JsonArray place, IGameState state) {
        try {
            IPlayer player = state.playerTurn();
            Point point = new Point(place.get(0).getAsInt(), place.get(1).getAsInt());
            return new PlacePenguin(player, point);
        } catch (Exception e) {
            System.out.println("Issue?");
            return null;
        }
    }

    /**
     * Returns an Action that represents a Json Action that was made on the given state.
     *
     * @param action JsonArray
     * @param state IGameState
     * @return Action
     */
    public Action toMoveAction(JsonArray action, IGameState state) {
        try {
            IPlayer player = state.playerTurn();
            JsonArray fromPosition = action.get(0).getAsJsonArray();
            JsonArray toPosition = action.get(1).getAsJsonArray();
            IPenguin playerPenguin = null;
            Point from = new Point(fromPosition.get(0).getAsInt(), fromPosition.get(1).getAsInt());
            Point to = new Point(toPosition.get(0).getAsInt(), toPosition.get(1).getAsInt());
            for (IPenguin penguin : player.getPenguins()) {
                if (penguin.getPosition().equals(from)) {
                    playerPenguin = penguin;
                }
            }
            assert playerPenguin != null;
            Action move = new MovePenguin(player, playerPenguin, to);
            return move;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns a Json Representation of a Placement from a given Point.
     *
     * @param point Point
     * @return JsonArray
     */
    public JsonArray pointToJson(Point point) {
        JsonArray pos = new JsonArray();
        pos.add(point.x);
        pos.add(point.y);
        return pos;
    }

    /**
     * Returns a JsonArray that represents a specified Json Function that is sent to remote Clients
     * with a given String to represent the function name and a JsonArray that contains the
     * parameters.
     *
     * @param func String
     * @param parameters JsonArray
     * @return JsonArray
     */
    public JsonArray createFunctionObject(String func, JsonArray parameters) {
        JsonArray function = new JsonArray();
        function.add(func);
        function.add(parameters);
        return function;
    }

}
