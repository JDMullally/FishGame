package model.server;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import model.games.PlayerAI;
import model.state.GameState;
import model.state.IGameState;
import model.state.IPenguin;
import model.state.IPlayer;
import model.strategy.Strategy;
import model.tree.PlayerInterface;
import org.junit.Test;
import util.PlayerUtil;

import static org.junit.Assert.*;

public class ClientTest {

    private PlayerInterface player;
    private ClientInterface c1, c2, c3;
    private JsonObject state;

    /*
    {
        "players": [
    {
      "color": "red",
      "score": 0,
      "places": [[0, 0], [3, 0], [3, 1], [3, 2]]
    },
    {
      "color": "white",
      "score": 0,
      "places": [[1, 0], [3, 3], [2, 0], [2, 1]]
    }
  ],
  "board":  [
    [1, 0, 3, 1],
    [0, 0, 5, 1],
    [0, 1, 1, 1],
    [1, 1, 1, 1]
  ]
}
     */

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

        JsonArray board = getBoard();

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

        JsonArray board = getBoard();

        state.add("board", board);
        state.add("players", players);
    }

    private JsonArray getBoard() {
        JsonArray board = new JsonArray();
        JsonArray row1 = new JsonArray();
        JsonArray row2 = new JsonArray();
        JsonArray row3 = new JsonArray();
        JsonArray row4 = new JsonArray();

        row1.add(1);
        row1.add(0);
        row1.add(3);
        row1.add(1);

        row2.add(0);
        row2.add(0);
        row2.add(5);
        row2.add(1);

        row3.add(0);
        row3.add(1);
        row3.add(1);
        row3.add(1);

        row4.add(1);
        row4.add(1);
        row4.add(3);
        row4.add(1);

        board.add(row1);
        board.add(row2);
        board.add(row3);
        board.add(row4);

        return board;
    }

    private JsonArray getPlaces2() {
        JsonArray places2 = new JsonArray();
        JsonArray pos11 = new JsonArray();
        JsonArray pos12 = new JsonArray();
        JsonArray pos13 = new JsonArray();


        pos11.add(1);
        pos11.add(0);

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

    @Test
    public void invalidBoardUnlessPlayerCheated() {
        this.init();
        PlayerUtil util = new PlayerUtil();

        IGameState state = util.JsonToGameState(this.state.get("board").getAsJsonArray(),
            this.state.get("players").getAsJsonArray());

        System.out.println("Over?: " + state.isGameOver());
        System.out.println("Ready?: " + state.isGameReady());

        System.out.println(state.getPlayers());

        System.out.println(state);

    }

    @Test
    public void validBoard() {
        this.init2();

        PlayerUtil util = new PlayerUtil();

        IGameState state = util.JsonToGameState(this.state.get("board").getAsJsonArray(),
            this.state.get("players").getAsJsonArray());

        System.out.println(state);

        IPlayer player = state.playerTurn();
    }
}
