package model.server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import model.games.IGameResult;
import model.state.IGameState;
import model.tree.Action;
import model.tree.PlayerInterface;
import constants.Constants;
import util.ColorUtil;
import util.PlayerUtil;

public class ClientProxy implements PlayerInterface {

    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private final DataInputStream dis;
    private final DataOutputStream dos;
    private final int age;
    private String name;
    private Gson gson;
    private final PlayerUtil util = new PlayerUtil();
    private List<Action> observedActions;

    private JsonArray createFunctionObject(String func, JsonArray parameters) {
        JsonArray function = new JsonArray();
        function.add(func);
        function.add(parameters);
        return function;
    }

    /**
     * This function creates the function based on the input string  func and the  JsonArray parameters
     * and sends it to the Client. It then reads back the response, converts it to JsonArray and
     * returns it.
     *
     * @param func String that represents the function  call
     * @param parameters JsonArray that holds all the parameters for the given function call
     * @return JsonArray that represents the response from a client.
     * @throws IOException if the connection is broken while reading or writing
     */
    private JsonArray callAndResponse(String func, JsonArray parameters) throws IOException {
        JsonArray function = createFunctionObject(func, parameters);
        this.dos.writeUTF(this.gson.toJson(function));
        String response = this.dis.readUTF();
        return gson.fromJson(response, JsonArray.class);
    }

    /**
     * Constructor for ClientProxy
     * @param s
     * @param age
     * @throws IOException
     */
    public ClientProxy(Socket s, int age) throws IOException {
        if (s == null || age < 1) {
            throw new IllegalArgumentException("The player must have an age of at least 1 "
                + "and a valid socket");
        }
        this.socket = s;
        this.is = this.socket.getInputStream();
        this.os = this.socket.getOutputStream();
        this.dis = new DataInputStream(this.is);
        this.dos = new DataOutputStream(this.os);
        this.age = age;
        this.gson = new Gson();
        this.observedActions = new ArrayList<>();
    }

    @Override
    public Action placePenguin(IGameState state) throws TimeoutException {
        try {
            JsonArray parameters = new JsonArray();
            JsonObject jsonState = this.util.GameStateToJson(state);
            parameters.add(jsonState);

            JsonArray placement = callAndResponse(Constants.setup, parameters);
            return this.util.toPlacementAction(placement, state);

        } catch (IOException e) {
            return null;
        }

    }


    @Override
    public Action movePenguin(IGameState state) throws TimeoutException {
        try {
            JsonArray parameters = new JsonArray();
            JsonObject jsonState = this.util.GameStateToJson(state);
            parameters.add(jsonState);
            parameters.add(this.util.GameActionsToJson(this.observedActions));

            JsonArray move = callAndResponse(Constants.takeTurn, parameters);
            return this.util.toMoveAction(move, state);

        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public int getPlayerAge() {
        return this.age;
    }

    @Override
    public String getPlayerID() {
        try {
            socket.setSoTimeout(1000);
            this.name = this.dis.readUTF();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return this.name;
    }


    @Override
    public boolean tournamentHasStarted() {
        try {
            JsonArray parameters = new JsonArray();
            parameters.add(true);
            JsonArray function = createFunctionObject(Constants.startTournament, parameters);

            this.dos.writeUTF(this.gson.toJson(function));
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    //Not necessary for remote interactions
    @Override
    public boolean gameHasStarted() {
        return true;
    }

    //Not necessary for remote interactions
    @Override
    public void kickedForCheating() {
    }

    //Not necessary for remote interactions
    @Override
    public void gameResults(IGameResult result) {
    }

    @Override
    public boolean tournamentResults(boolean youWon) {
        try {
            JsonArray parameters = new JsonArray();
            parameters.add(youWon);
            JsonArray function = createFunctionObject(Constants.end, parameters);

            this.dos.writeUTF(this.gson.toJson(function));
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public void playerColor(Color color) {
        try {
            JsonArray parameters = new JsonArray();
            parameters.add(ColorUtil.toColorString(color));
            JsonArray function = createFunctionObject(Constants.playAs, parameters);

            this.dos.writeUTF(this.gson.toJson(function));
        } catch (IOException e) {
        }
    }

    @Override
    public void otherPlayerColors(List<Color> otherPlayers) {
        try {
            JsonArray parameters = new JsonArray();
            for (Color color : otherPlayers) {
                parameters.add(ColorUtil.toColorString(color));
            }
            JsonArray function = createFunctionObject(Constants.playWith, parameters);
            this.dos.writeUTF(this.gson.toJson(function));
        } catch (IOException e) {
        }
    }

    @Override
    public void getOnGoingAction(Action action) {
        this.observedActions.add(action);
    }

    @Override
    public void clearOnGoingAction() {
        this.observedActions = new ArrayList<>();
    }


}
