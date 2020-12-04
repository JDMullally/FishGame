package model.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import constants.Constants;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import model.tree.Action;
import model.state.IGameState;
import util.PlayerUtil;
import model.tree.PlayerInterface;

public class Client implements ClientInterface {

    private InetAddress ip;
    private PlayerInterface player;
    private int port;
    private Socket socket;
    private OutputStream os;
    private InputStream is;
    private DataInputStream dis;
    private DataOutputStream dos;
    private JsonWriter writer;
    private JsonReader reader;
    private PlayerUtil util;

    public Client(int port, InetAddress ip, PlayerInterface player) throws IOException {
        if (player == null) {
            throw new IllegalArgumentException("Can't create a new client with a null name");
        }
        this.ip = ip;
        this.port = port;
        this.player = player;
        this.socket = new Socket(this.ip, this.port);
        this.is = this.socket.getInputStream();
        this.os = this.socket.getOutputStream();
        this.dis = new DataInputStream(this.is);
        this.dos = new DataOutputStream(this.os);

        InputStreamReader isr = new InputStreamReader(is);
        OutputStreamWriter osw = new OutputStreamWriter(os);

        this.writer = new JsonWriter(osw);
        this.reader = new JsonReader(isr);
        this.util = new PlayerUtil();
    }

    /**
     * Default constructor with localhost ip.
     * @param port int that represents port Client is connecting to ip at.
     * @param player the player interface
     * @throws IOException
     */
    public Client(int port, PlayerInterface player) throws IOException {
        this(port, InetAddress.getByAddress("localhost", new byte[]{127, 0, 0, 1}), player);
    }

    /**
     * Testing constructor
     * @param player
     */
    public Client(PlayerInterface player) {
        this.player = player;
        this.util = new PlayerUtil();
    }

    @Override
    public void checkMethod(JsonArray functionObject) {
        String function = functionObject.get(0).getAsString();
        JsonArray parameters = functionObject.get(1).getAsJsonArray();
        switch (function) {
            case Constants.startTournament:
                this.startTournament(parameters);
            case Constants.playAs:
                this.playingAs(parameters);
            case Constants.playWith:
                this.playingWith(parameters);
            case Constants.setup:
                this.setUp(parameters);
            case Constants.takeTurn:
                this.takeTurn(parameters);
            case Constants.end:
                this.endTournament(parameters);
            default:
                throw new IllegalArgumentException("Should have never gotten here");
        }
    }

    @Override
    public void startTournament(JsonArray parameters) {

    }

    @Override
    public void playingAs(JsonArray parameters) {

    }

    @Override
    public void playingWith(JsonArray parameters) { }

    @Override
    public JsonArray setUp(JsonArray parameters) {
        JsonObject object = parameters.get(0).getAsJsonObject();
        JsonArray board = object.get("board").getAsJsonArray();
        JsonArray players = object.get("players").getAsJsonArray();
        IGameState state = util.JsonToGameState(board, players);

        Action move = null;
        try {
            move = this.player.placePenguin(state);
        } catch (Exception e) { }

        assert move != null;
        JsonArray placement = this.util.pointToJson(move.getToPosition());
        return placement;
    }

    @Override
    public JsonArray takeTurn(JsonArray parameters) {
        JsonObject object = parameters.get(0).getAsJsonObject();
        JsonArray board = object.get("board").getAsJsonArray();
        JsonArray players = object.get("players").getAsJsonArray();
        IGameState state = util.JsonToGameState(board, players);

        Action move = null;
        try {
            move = this.player.movePenguin(state);
        } catch (Exception e) { }

        JsonArray action = this.util.moveToJson(move);
        return action;
    }

    //TODO close thread
    @Override
    public void endTournament(JsonArray parameters) { }

    @Override
    public String sendName() {
        // send this
        String str = this.player.getPlayerID();
        // send this
        return str;

    }
}
