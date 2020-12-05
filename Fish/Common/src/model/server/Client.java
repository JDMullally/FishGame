package model.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import constants.Constants;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import model.state.IPlayer;
import model.tree.Action;
import model.state.IGameState;
import util.PlayerUtil;
import model.tree.PlayerInterface;

public class Client extends Thread implements ClientInterface {

    private InetAddress ip;
    private PlayerInterface player;
    private int port;
    private Socket socket;
    private OutputStream os;
    private InputStream is;
    private DataInputStream dis;
    private DataOutputStream dos;
    private PlayerUtil util;
    private boolean running;
    private Gson gson;

    public Client(int port, InetAddress ip, PlayerInterface player) throws IOException {
        if (player == null) {
            throw new IllegalArgumentException("Can't create a new client with a null name");
        }
        this.ip = ip;
        this.port = port;
        this.player = player;
        this.socket = new Socket(this.ip, this.port);
        System.out.println("connected to " + this.socket.getPort() + " at " + this.socket.getLocalPort());
        this.is = this.socket.getInputStream();
        this.os = this.socket.getOutputStream();
        this.dis = new DataInputStream(this.is);
        this.dos = new DataOutputStream(this.os);
        this.util = new PlayerUtil();
        this.running = true;
        this.gson = new Gson();
        this.start();
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

    @Override
    public void run() {
        while (this.running) {
            try {
                //this.sendName();
                String message = this.dis.readUTF();
                JsonArray function = this.gson.fromJson(message, JsonArray.class);
                this.checkMethod(function);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }

        }
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
    public void checkMethod(JsonArray functionObject) throws IOException {
        String function = functionObject.get(0).getAsString();
        JsonArray parameters = functionObject.get(1).getAsJsonArray();

        switch (function) {
            case Constants.startTournament:
                this.startTournament(parameters);
                break;
            case Constants.playAs:
                this.playingAs(parameters);
                break;
            case Constants.playWith:
                this.playingWith(parameters);
                break;
            case Constants.setup:
                this.setUp(parameters);
                break;
            case Constants.takeTurn:
                this.takeTurn(parameters);
                break;
            case Constants.end:
                this.endTournament(parameters);
                break;
            default:
                throw new IllegalArgumentException("Should have never gotten here");
        }
    }

    @Override
    public void startTournament(JsonArray parameters) {
        boolean bool = parameters.get(0).getAsBoolean();
    }

    @Override
    public void playingAs(JsonArray parameters) {

    }

    @Override
    public void playingWith(JsonArray parameters) { }

    @Override
    public JsonArray setUp(JsonArray parameters) throws IOException {
        JsonObject object = parameters.get(0).getAsJsonObject();
        JsonArray board = object.get("board").getAsJsonArray();
        JsonArray players = object.get("players").getAsJsonArray();
        IGameState state = util.JsonToGameState(board, players);

        System.out.println(this.getName() + " Setup");
        System.out.println("*************************************************");
        System.out.println(state);
        List<IPlayer> gamePlayers = state.getPlayers();
        for (IPlayer player : gamePlayers) {
            System.out.println(player+ " : " + player.getPenguins());
        }
        System.out.println(state.isGameReady());
        System.out.println("*************************************************");
        System.out.println();


        Action move = null;
        try {
            move = this.player.placePenguin(state);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        assert move != null;
        JsonArray placement = this.util.pointToJson(move.getToPosition());
        this.dos.writeUTF(gson.toJson(placement));
        return placement;
    }

    @Override
    public JsonArray takeTurn(JsonArray parameters) throws IOException {
        JsonObject object = parameters.get(0).getAsJsonObject();
        JsonArray players = object.get("players").getAsJsonArray();
        JsonArray board = object.get("board").getAsJsonArray();
        IGameState state = util.JsonToGameState(board, players);

        System.out.println();
        System.out.println(this.getName() + " TakeTurn");
        System.out.println("*************************************************");
        System.out.println(state);
        List<IPlayer> gamePlayers = state.getPlayers();
        for (IPlayer player : gamePlayers) {
            System.out.println(player+ " : " + player.getPenguins());
        }
        System.out.println(state.isGameReady());
        System.out.println("*************************************************");
        System.out.println();

        Action move = null;
        try {
            move = this.player.movePenguin(state);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        JsonArray action = this.util.moveToJson(move);
        this.dos.writeUTF(gson.toJson(action));
        return action;
    }


    @Override
    public void endTournament(JsonArray parameters) throws IOException {
        this.running = false;
        this.socket.close();
        this.interrupt();
    }

    @Override
    public String sendName() throws IOException {
        // send this
        String str = this.player.getPlayerID();
        this.dos.writeUTF(str);
        return str;

    }
}
