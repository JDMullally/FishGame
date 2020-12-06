package model;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.awt.Point;
import model.server.ClientInterface;
import model.state.IGameState;
import model.tree.Action;
import model.tree.MovePenguin;
import model.tree.PlacePenguin;
import model.tree.PlayerInterface;
import org.junit.Test;
import util.PlayerUtil;

import static org.junit.Assert.*;

public class PlayerUtilTest {
    private JsonObject state;

    public void init() {
        this.state = new JsonObject();

        JsonArray players = new JsonArray();

        JsonObject player1 = new JsonObject();
        JsonObject player2 = new JsonObject();
        player1.addProperty("color", "red");
        player2.addProperty("color", "white");
        player1.addProperty("score", 2);
        player2.addProperty("score", 2);


        JsonArray places1 = getPlaces1();
        JsonArray places2 = getPlaces2();

        player1.add("places", places1);
        player2.add("places", places2);

        players.add(player1);
        players.add(player2);

        JsonArray board = getBoard(2, 5);

        state.add("board", board);
        state.add("players", players);
    }

    public void init2() {
        this.state = new JsonObject();

        JsonArray players = new JsonArray();

        JsonObject player1 = new JsonObject();
        JsonObject player2 = new JsonObject();
        player1.addProperty("color", "red");
        player2.addProperty("color", "white");
        player1.addProperty("score", 0);
        player2.addProperty("score", 0);


        JsonArray places1 = getTruePlaces1();
        JsonArray places2 = getTruePlaces2();

        player1.add("places", places1);
        player2.add("places", places2);

        players.add(player1);
        players.add(player2);

        JsonArray board = getBoard(2, 5);

        state.add("board", board);
        state.add("players", players);
    }

    /**
     * Creates a size X size board that each have input value fish and returns it as a JsonArray.
     * @param val the value of the fish
     * @param size length and width of the board
     * @return JsonArray
     */
    private JsonArray getBoard(int val, int size) {
        JsonArray board = new JsonArray();
        for (int i = 0; i < size; i++) {
            JsonArray array = new JsonArray();
            for (int j = 0; j < size; j++) {
                array.add(val);

            }
            board.add(array);
        }
        return board;
    }

    private JsonArray getPlaces2() {
        JsonArray places2 = new JsonArray();
        JsonArray pos11 = new JsonArray();
        JsonArray pos12 = new JsonArray();
        JsonArray pos13 = new JsonArray();

        pos11.add(2);
        pos11.add(2);

        pos12.add(3);
        pos12.add(3);

        pos13.add(2);
        pos13.add(1);

        places2.add(pos11);
        places2.add(pos12);
        places2.add(pos13);
        return places2;
    }

    private JsonArray getPlaces1() {
        JsonArray places1 = new JsonArray();
        JsonArray pos01 = new JsonArray();
        JsonArray pos02 = new JsonArray();
        JsonArray pos03 = new JsonArray();

        pos01.add(0);
        pos01.add(0);

        pos02.add(3);
        pos02.add(0);

        pos03.add(3);
        pos03.add(1);

        places1.add(pos01);
        places1.add(pos02);
        places1.add(pos03);

        return places1;
    }

    private JsonArray getTruePlaces1() {
        JsonArray places1 = this.getPlaces1();
        JsonArray pos04 = new JsonArray();

        pos04.add(3);
        pos04.add(2);
        places1.add(pos04);
        return places1;
    }

    private JsonArray getTruePlaces2() {
        JsonArray places2 = this.getPlaces2();
        JsonArray pos14 = new JsonArray();

        pos14.add(2);
        pos14.add(1);
        places2.add(pos14);
        return places2;
    }

    /**
     * When the board is deserialized, it should calculate the number of cheaters if it can.
     * This will allow the game to still run if players are removed and no one has a valid number
     * of penguins.
     */
    @Test
    public void invalidBoardUnlessPlayerCheated() {
        this.init();
        PlayerUtil util = new PlayerUtil();

        IGameState state = util.JsonToGameStateMovement(this.state.get("board").getAsJsonArray(),
            this.state.get("players").getAsJsonArray());

       assertTrue(state.isGameReady());
    }

    /**
     * A valid board should be deserialized very easily.  A valid board is defined by having
     * the correct number of penguins and players.
     */
    @Test
    public void validBoard() {
        this.init2();

        PlayerUtil util = new PlayerUtil();

        IGameState state = util.JsonToGameStateMovement(this.state.get("board").getAsJsonArray(),
            this.state.get("players").getAsJsonArray());

        assertTrue(state.isGameReady());
    }

    /**
     * If multiple players have been removed and it leaves only one player in the game, they
     * should still be able to play until the end to assure a cheating player doesn't win a game.
     */
    @Test
    public void validBoardPlayerRemovedForCheatingOneRemaining() {
        this.init2();

        PlayerUtil util = new PlayerUtil();
        JsonArray players = this.state.get("players").getAsJsonArray();

        //Removes the last player in the list because they cheated last round.s
        players.remove(players.size() - 1);

        IGameState state = util.JsonToGameStateMovement(this.state.get("board").getAsJsonArray(),
            players);

        assertTrue(state.isGameReady());
    }

    /**
     *
     */
    @Test
    public void invalidBoard() {
        this.init2();

        PlayerUtil util = new PlayerUtil();

        JsonArray shittyBoard = this.getBoard(2, 2);
        JsonArray players = this.state.get("players").getAsJsonArray();

        //Removes the last player in the list because they cheated last round.s

        IGameState state = util.JsonToGameStateMovement(shittyBoard,
            players);

        assertFalse(state.isGameReady());
    }

    @Test
    public void validBoardNoPlayers() {
        this.init2();

        PlayerUtil util = new PlayerUtil();

        JsonArray board = this.getBoard(2, 5);
        JsonArray players = new JsonArray();

        IGameState state = util.JsonToGameStateMovement(board,
            players);

        assertTrue(state.isGameOver());
    }

    @Test
    public void convertActionToJson() {
        this.init2();
        PlayerUtil util = new PlayerUtil();

        IGameState state = util.JsonToGameStateMovement(this.state.get("board").getAsJsonArray(),
            this.state.get("players").getAsJsonArray());

        Action action = new MovePenguin(state.playerTurn(), state.playerTurn().getPenguins().get(0), new Point(5,5));
        JsonArray actual = util.moveToJson(action);

        JsonArray expected = new JsonArray();
        JsonArray from = new JsonArray();
        from.add(state.playerTurn().getPenguins().get(0).getPosition().x);
        from.add(state.playerTurn().getPenguins().get(0).getPosition().y);
        JsonArray to = new JsonArray();
        to.add(5);
        to.add(5);
        expected.add(from);
        expected.add(to);

        assertEquals(expected, actual);
    }

    @Test
    public void convertPositionToJson() {
        this.init2();
        PlayerUtil util = new PlayerUtil();

        IGameState state = util.JsonToGameStateMovement(this.state.get("board").getAsJsonArray(),
            this.state.get("players").getAsJsonArray());

        Action action = new PlacePenguin(state.playerTurn(), new Point(5,5));
        JsonArray actual = util.pointToJson(action.getToPosition());

        JsonArray expected = new JsonArray();
        expected.add(5);
        expected.add(5);

        assertEquals(expected, actual);
    }

}
