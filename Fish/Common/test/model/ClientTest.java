package model;

import static org.junit.Assert.assertEquals;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.awt.Point;
import java.io.IOException;
import model.games.PlayerAI;
import model.server.Client;
import model.state.IGameState;
import model.strategy.Strategy;
import model.testUtil.ClientTestUtil;
import model.tree.Action;
import model.tree.PlacePenguin;
import org.junit.Test;
import util.PlayerUtil;


public class ClientTest {

    private Client client;
    private PlayerUtil util;

    private void init() {
        this.client = new Client(new PlayerAI(new Strategy(), 2));
        this.util = new PlayerUtil();
    }

    private IGameState createTestGameState4PlayersInvalid() {
        return ClientTestUtil.createTestGameState4PlayersInvalid();
    }

    /**
     * Creates a GameState where all the Penguins are placed and returns it. Great for testing!
     * @return IGameState is the new GameState.
     */
    private IGameState createTestGameState2Players() {
      return ClientTestUtil.createTestGameState2Players();
    }

    /**
     * Creates a GameState where all the Penguins are placed and returns it. Great for testing!
     * @return IGameState is the new GameState.
     */
    private IGameState createTestGameState4Players() {
       return ClientTestUtil.createTestGameState4Players();
    }


    /**
     * Creates a GameState where all the Penguins are placed and returns it. Great for testing!
     * @return IGameState is the new GameState.
     */
    private IGameState createTestGameState3Players() {
        return ClientTestUtil.createTestGameState3Players();
    }


    private IGameState createTestGameStateNoPenguins() {
        return ClientTestUtil.createTestGameStateNoPenguins();
    }

    private IGameState createTestGameState3PlayersInvalid() {
        return ClientTestUtil.createTestGameState3PlayersInvalid();
    }

    private IGameState createTestGameState2PlayersInvalid() {
        return ClientTestUtil.createTestGameState2PlayersInvalid();
    }

    @Test (expected = RuntimeException.class)
    public void testTakeTurn2PlayersNullBoard() throws IOException {
        init();

        JsonObject state = null;
        JsonArray params = new JsonArray();
        params.add(state);
        params.add(new JsonArray());

        JsonArray actual = this.client.takeTurn(params);
        JsonArray expected = new JsonArray();
        JsonArray from = new JsonArray();
        from.add(1);
        from.add(0);
        JsonArray to = new JsonArray();
        to.add(0);
        to.add(1);
        expected.add(from);
        expected.add(to);

        assertEquals(expected, actual);
    }


    @Test (expected = RuntimeException.class)
    public void testTakeTurn2PlayersInvalidBoard() throws IOException {
        init();

        JsonObject state = util.GameStateToJson(this.createTestGameState2PlayersInvalid());
        JsonArray params = new JsonArray();
        params.add(state);
        params.add(new JsonArray());

        JsonArray actual = this.client.takeTurn(params);
        JsonArray expected = new JsonArray();
        JsonArray from = new JsonArray();
        from.add(1);
        from.add(0);
        JsonArray to = new JsonArray();
        to.add(0);
        to.add(1);
        expected.add(from);
        expected.add(to);

        assertEquals(expected, actual);
    }


    @Test
    public void testTakeTurn2PlayersFirstTurn() throws IOException {
        init();

        JsonObject state = util.GameStateToJson(this.createTestGameState2Players());
        JsonArray params = new JsonArray();
        params.add(state);
        params.add(new JsonArray());

        JsonArray actual = this.client.takeTurn(params);
        JsonArray expected = new JsonArray();
        JsonArray from = new JsonArray();
        from.add(1);
        from.add(0);
        JsonArray to = new JsonArray();
        to.add(0);
        to.add(1);
        expected.add(from);
        expected.add(to);

        assertEquals(expected, actual);
    }

    @Test
    public void testTakeTurn2PlayersSecondTurn() throws IOException {
        init();

        IGameState gameState = this.createTestGameState2Players();
        JsonObject state = util.GameStateToJson(gameState);
        JsonArray params = new JsonArray();
        params.add(state);
        params.add(new JsonArray());

        JsonArray jsonAction = this.client.takeTurn(params);

        Action action = this.util.toMoveAction(jsonAction, gameState);

        gameState =  action.apply(gameState);

        state = util.GameStateToJson(gameState);
        params = new JsonArray();
        params.add(state);
        params.add(new JsonArray());

        JsonArray actual = this.client.takeTurn(params);

        JsonArray expected = new JsonArray();
        JsonArray from = new JsonArray();
        from.add(1);
        from.add(1);
        JsonArray to = new JsonArray();
        to.add(1);
        to.add(2);
        expected.add(from);
        expected.add(to);

        assertEquals(expected, actual);
    }


    @Test
    public void testTakeTurnLastPlayerCheated2Players() throws IOException {
        init();

        IGameState gameState = this.createTestGameState2Players();

        //remove last player because they cheated
        gameState = gameState.removePlayer(gameState.playerTurn());

        JsonObject state = util.GameStateToJson(gameState);
        JsonArray params = new JsonArray();
        params.add(state);
        params.add(new JsonArray());

        JsonArray actual = this.client.takeTurn(params);

        JsonArray expected = new JsonArray();
        JsonArray from = new JsonArray();
        from.add(1);
        from.add(1);
        JsonArray to = new JsonArray();
        to.add(1);
        to.add(0);
        expected.add(from);
        expected.add(to);

        assertEquals(expected, actual);
    }

    @Test
    public void testTakeTurn3PlayersFirstTurn() throws IOException {
        init();

        JsonObject state = util.GameStateToJson(this.createTestGameState3Players());
        JsonArray params = new JsonArray();
        params.add(state);
        params.add(new JsonArray());

        JsonArray actual = this.client.takeTurn(params);
        JsonArray expected = new JsonArray();
        JsonArray from = new JsonArray();
        from.add(0);
        from.add(0);
        JsonArray to = new JsonArray();
        to.add(0);
        to.add(2);
        expected.add(from);
        expected.add(to);

        assertEquals(expected, actual);
    }

    @Test (expected = RuntimeException.class)
    public void testTakeTurn3PlayersInvalidBoard() throws IOException {
        init();

        JsonObject state = util.GameStateToJson(this.createTestGameState3PlayersInvalid());
        JsonArray params = new JsonArray();
        params.add(state);
        params.add(new JsonArray());

        JsonArray actual = this.client.takeTurn(params);
        JsonArray expected = new JsonArray();
        JsonArray from = new JsonArray();
        from.add(0);
        from.add(0);
        JsonArray to = new JsonArray();
        to.add(0);
        to.add(2);
        expected.add(from);
        expected.add(to);

        assertEquals(expected, actual);
    }

    @Test
    public void testTakeTurn3PlayersSecondTurn() throws IOException {
        init();

        IGameState gameState = this.createTestGameState3Players();
        JsonObject state = util.GameStateToJson(gameState);
        JsonArray params = new JsonArray();
        params.add(state);
        params.add(new JsonArray());

        JsonArray jsonAction = this.client.takeTurn(params);

        Action action = this.util.toMoveAction(jsonAction, gameState);

        gameState =  action.apply(gameState);

        state = util.GameStateToJson(gameState);
        params = new JsonArray();
        params.add(state);
        params.add(new JsonArray());

        JsonArray actual = this.client.takeTurn(params);

        JsonArray expected = new JsonArray();
        JsonArray from = new JsonArray();
        from.add(1);
        from.add(0);
        JsonArray to = new JsonArray();
        to.add(1);
        to.add(2);
        expected.add(from);
        expected.add(to);

        assertEquals(expected, actual);
    }

    @Test
    public void testTakeTurnLastPlayerCheated3Players() throws IOException {
        init();

        IGameState gameState = this.createTestGameState3Players();

        //remove last player because they cheated
        gameState = gameState.removePlayer(gameState.playerTurn());

        JsonObject state = util.GameStateToJson(gameState);
        JsonArray params = new JsonArray();
        params.add(state);
        params.add(new JsonArray());

        JsonArray actual = this.client.takeTurn(params);

        JsonArray expected = new JsonArray();
        JsonArray from = new JsonArray();
        from.add(1);
        from.add(0);
        JsonArray to = new JsonArray();
        to.add(1);
        to.add(1);
        expected.add(from);
        expected.add(to);

        assertEquals(expected, actual);
    }

    @Test
    public void testTakeTurn4PlayersFirstTurn() throws IOException {
        init();

        JsonObject state = util.GameStateToJson(this.createTestGameState4Players());
        JsonArray params = new JsonArray();
        params.add(state);
        params.add(new JsonArray());

        JsonArray actual = this.client.takeTurn(params);
        JsonArray expected = new JsonArray();
        JsonArray from = new JsonArray();
        from.add(0);
        from.add(0);
        JsonArray to = new JsonArray();
        to.add(0);
        to.add(2);
        expected.add(from);
        expected.add(to);

        assertEquals(expected, actual);
    }

    @Test
    public void testTakeTurn4PlayersSecondTurn() throws IOException {
        init();

        IGameState gameState = this.createTestGameState4Players();
        JsonObject state = util.GameStateToJson(gameState);
        JsonArray params = new JsonArray();
        params.add(state);
        params.add(new JsonArray());

        JsonArray jsonAction = this.client.takeTurn(params);

        Action action = this.util.toMoveAction(jsonAction, gameState);

        gameState =  action.apply(gameState);

        state = util.GameStateToJson(gameState);
        params = new JsonArray();
        params.add(state);
        params.add(new JsonArray());

        JsonArray actual = this.client.takeTurn(params);

        JsonArray expected = new JsonArray();
        JsonArray from = new JsonArray();
        from.add(1);
        from.add(0);
        JsonArray to = new JsonArray();
        to.add(1);
        to.add(2);
        expected.add(from);
        expected.add(to);

        assertEquals(expected, actual);
    }

    @Test
    public void testTakeTurnLastPlayerCheated4Players() throws IOException {
        init();

        IGameState gameState = this.createTestGameState4Players();

        //remove last player because they cheated
        gameState = gameState.removePlayer(gameState.playerTurn());

        JsonObject state = util.GameStateToJson(gameState);
        JsonArray params = new JsonArray();
        params.add(state);
        params.add(new JsonArray());

        JsonArray actual = this.client.takeTurn(params);

        JsonArray expected = new JsonArray();
        JsonArray from = new JsonArray();
        from.add(1);
        from.add(0);
        JsonArray to = new JsonArray();
        to.add(1);
        to.add(2);
        expected.add(from);
        expected.add(to);

        assertEquals(expected, actual);
    }

    @Test (expected = RuntimeException.class)
    public void testTakeTurn4PlayersInvalidBoard() throws IOException {
        init();

        IGameState gameState = this.createTestGameState4PlayersInvalid();

        //remove last player because they cheated
        gameState = gameState.removePlayer(gameState.playerTurn());

        JsonObject state = util.GameStateToJson(gameState);
        JsonArray params = new JsonArray();
        params.add(state);
        params.add(new JsonArray());

        JsonArray actual = this.client.takeTurn(params);

        JsonArray expected = new JsonArray();
        JsonArray from = new JsonArray();
        from.add(1);
        from.add(0);
        JsonArray to = new JsonArray();
        to.add(1);
        to.add(2);
        expected.add(from);
        expected.add(to);

        assertEquals(expected, actual);
    }

    @Test
    public void testSetup() throws IOException {
        init();

        IGameState gameState = this.createTestGameStateNoPenguins();
        JsonObject state = util.GameStateToJson(gameState);
        JsonArray params = new JsonArray();
        params.add(state);

        JsonArray actual = this.client.setUp(params);

        JsonArray expected = new JsonArray();

        expected.add(0);
        expected.add(0);

        assertEquals(expected, actual);
    }

    @Test
    public void testSetup2() throws IOException {
        init();

        IGameState gameState = this.createTestGameStateNoPenguins();
        JsonObject state = util.GameStateToJson(gameState);
        JsonArray params = new JsonArray();
        params.add(state);

        JsonArray jsonPlacement = this.client.setUp(params);

        Point point = new Point(jsonPlacement.get(0).getAsInt(), jsonPlacement.get(1).getAsInt());

        Action placement = new PlacePenguin(gameState.playerTurn(), point);

        gameState = placement.apply(gameState);

        state = util.GameStateToJson(gameState);
        params = new JsonArray();
        params.add(state);

        JsonArray actual = this.client.setUp(params);

        JsonArray expected = new JsonArray();
        expected.add(1);
        expected.add(0);

        assertEquals(expected, actual);
    }

    @Test
    public void testSetupPreviousPlayerCheated() throws IOException {
        init();

        IGameState gameState = this.createTestGameStateNoPenguins();
        gameState = gameState.removePlayer(gameState.playerTurn());
        JsonObject state = util.GameStateToJson(gameState);
        JsonArray params = new JsonArray();
        params.add(state);

        JsonArray actual = this.client.setUp(params);

        JsonArray expected = new JsonArray();

        expected.add(0);
        expected.add(0);

        assertEquals(expected, actual);
    }

    @Test (expected = RuntimeException.class)
    public void placePenguinInvalidState() throws IOException {
        init();

        IGameState gameState = this.createTestGameStateNoPenguins();
        gameState = gameState.removePlayer(gameState.playerTurn());
        gameState = gameState.removePlayer(gameState.playerTurn());
        JsonObject state = util.GameStateToJson(gameState);
        JsonArray params = new JsonArray();
        params.add(state);

        JsonArray actual = this.client.setUp(params);

        JsonArray expected = new JsonArray();

        expected.add(0);
        expected.add(0);

        assertEquals(expected, actual);
    }

}
