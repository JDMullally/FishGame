package util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.board.Direction;
import model.board.Tile;
import model.state.IGameState;
import model.state.IPenguin;
import model.state.IPlayer;

/**
 * Tests the GameTree for the Fish Game.
 */
public class GameTreeTestUtil {

    /**
     * Entry point for the application.
     *
     * @param args program arguments
     */
    public static void main(String[] args) throws IOException {
        new GameTreeTestUtil().test();
    }

    /**
     * Reads in JSON for STDIN and produces GameTree result to STDOUT
     */
    private void test() throws IOException {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        // reads input
        Map<?, ?> map = gson.fromJson(bufferedReader, Map.class);

        // gets the position and board
        JsonObject state = null;
        JsonArray fromPosition = null;
        JsonArray toPosition = null;
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();

            switch (key) {
                case "state":
                    state = parser.parse(value).getAsJsonObject();
                    break;
                case "from":
                    fromPosition = parser.parse(value).getAsJsonArray();
                    break;
                case "to":
                    toPosition = parser.parse(value).getAsJsonArray();
                    break;
                default:
                    break;
            }
        }
        bufferedReader.close();

        // creates GameState and GameTree results
        if (state != null && fromPosition != null && toPosition != null) {
            GameStateUtil util = new GameStateUtil();

            JsonArray board = state.get("board").getAsJsonArray();
            JsonArray players = state.get("players").getAsJsonArray();
            IGameState gameState;
            try {
                gameState = util.JsonToGameState(board, players);
            } catch (Exception e) {
                System.out.println("false");
                return;
            }
            //System.out.println(gameState);

            Point from = new Point(fromPosition.get(1).getAsInt(), fromPosition.get(0).getAsInt());
            Point to = new Point(toPosition.get(1).getAsInt(), toPosition.get(0).getAsInt());

            IPlayer player = gameState.playerTurn();
            List<IPenguin> penguins = player.getPenguins();

            // checks if the from position matches any of the penguins current locations
            IPenguin penguinToMove = null;
            for (IPenguin penguin : penguins) {
                Point penguinPoint = penguin.getPosition();
                if (penguinPoint.x == from.x && penguinPoint.y == from.y) {
                    penguinToMove = penguin;
                    break;
                }
            }

            // if no penguin has position fromPosition
            if (penguinToMove == null) {
                System.out.println("false");
                return;
            }

            // if no penguin can move
            try {
                gameState = gameState.move(player, penguinToMove, to, false);
            } catch (Exception e) {
                System.out.println("false");
                return;
            }

            // updates player to next player to move
            player = gameState.playerTurn();

            LinkedHashMap<IPenguin, List<Tile>> tilesToMove = gameState.getPossibleMoves(player);

            // checks if the player can move a penguin next to 'to' variable.
            if (tilesToMove.size() == 0) {
                System.out.println("false");
            } else {
                Map<Direction, Map<Point, Point>> actions = new HashMap<>();
                for (Map.Entry<IPenguin, List<Tile>> entrySet : tilesToMove.entrySet()) {
                    IPenguin penguin = entrySet.getKey();
                    List<Tile> tiles = entrySet.getValue();

                    for (Tile tile : tiles) {
                        Point point = tile.getPosition();
                        Direction direction = this.neighborDirection(point, to);
                        if (direction != null) {
                            if (actions.containsKey(direction)) {
                                Map<Point, Point> action = actions.get(direction);
                                action.put(penguin.getPosition(), point);
                                actions.put(direction, action);
                            } else {
                                Map<Point, Point> action = new HashMap<>();
                                action.put(penguin.getPosition(), point);
                                actions.put(direction, action);
                            }
                        }
                    }
                }

                this.tieBreak(actions);
            }
        }
    }

    /**
     * Returns the direction point1 is relative to point2 if the 2 given points are neighbors. Assumes that the two points are valid
     * points on the GameBoard.
     *
     * @param point1 first point
     * @param point2 second point
     * @return Direction or null if they points aren't neighbors
     */
    private Direction neighborDirection(Point point1, Point point2) {
        for (Direction dir: Direction.values()) {
            if (dir.apply(point2).equals(point1)) {
                return dir;
            }
        }
        return null;
    }

    /**
     * Returns the tie break for potential actions to make.
     *
     * if more than one position satisfies the "closeness" condition, a tie breaker algorithm picks
     * by the top-most row of the "from" position, the left-most column of the "from" position, the
     * top-most row of the "to" position, and the left-most column of the "to" position---in exactly
     * this order.
     *
     * @param actions potential actions to make
     */
    private void tieBreak(Map<Direction, Map<Point, Point>> actions) {
        if (actions.size() == 0) {
            System.out.println("false");
            return;
        }

        Map<Point, Point> directionActions;
        if (actions.containsKey(Direction.UP)) {
            directionActions = actions.get(Direction.UP);
        } else if (actions.containsKey(Direction.DIAGONAL_UP_RIGHT)) {
            directionActions = actions.get(Direction.DIAGONAL_UP_RIGHT);
        } else if (actions.containsKey(Direction.DIAGONAL_DOWN_RIGHT)) {
            directionActions = actions.get(Direction.DIAGONAL_DOWN_RIGHT);
        } else if (actions.containsKey(Direction.DOWN)) {
            directionActions = actions.get(Direction.DOWN);
        } else if (actions.containsKey(Direction.DIAGONAL_DOWN_LEFT)) {
            directionActions = actions.get(Direction.DIAGONAL_DOWN_LEFT);
        } else {
            directionActions = actions.get(Direction.DIAGONAL_UP_LEFT);
        }
        // does tie break
        Point from = null;
        Point to = null;
        for (Map.Entry<Point, Point> entry : directionActions.entrySet()) {
            Point fromTemp = entry.getKey();
            Point toTemp = entry.getValue();

            // assigns from and to
            if (from == null || to == null) {
                from = fromTemp;
                to = toTemp;
            } else {
                if (fromTemp.y < from.y) {
                    from = fromTemp;
                    to = toTemp;
                } else if (fromTemp.y > from.y) {
                    continue;
                }

                if (fromTemp.x < from.x) {
                    from = fromTemp;
                    to = toTemp;
                } else if (fromTemp.x > from.x) {
                    continue;
                }

                if (toTemp.y < to.y) {
                    from = fromTemp;
                    to = toTemp;
                } else if (toTemp.y > to.y) {
                    continue;
                }

                if (toTemp.x < to.x) {
                    from = fromTemp;
                    to = toTemp;
                }
            }
        }

        // print action
        if (from != null && to != null) {
            JsonArray fromArray = new JsonArray();
            fromArray.add(from.y);
            fromArray.add(from.x);

            JsonArray toArray = new JsonArray();
            toArray.add(to.y);
            toArray.add(to.x);

            JsonArray res = new JsonArray();
            res.add(fromArray);
            res.add(toArray);

            System.out.println(res.toString());
        }
    }
}
