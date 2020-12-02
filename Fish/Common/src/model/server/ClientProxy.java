package model.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonStreamParser;
import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
import util.GameStateUtil;
import util.PlayerUtil;

public class ClientProxy implements PlayerInterface {

    private Socket socket;
    private final DataInputStream dis;
    private final DataOutputStream dos;
    private JsonStreamParser jsonStreamParser;
    private final int age;
    private String name;
    private final PlayerUtil util = new PlayerUtil();
    private List<Action> observedActions;

    public ClientProxy(Socket s, DataInputStream dis, DataOutputStream dos, int age) {
        if (s == null || dis == null || dos == null || age < 1) {
            throw new IllegalArgumentException("Invalid ClientProxy Player");
        }
        this.socket = s;
        this.dis = dis;
        this.dos = dos;
        this.age = age;
        InputStreamReader reader = new InputStreamReader(dis);
        this.jsonStreamParser = new JsonStreamParser(reader);
        this.name = this.getName();
        this.observedActions = new ArrayList<>();
    }

    public void addObservedAction(Action action) {
        this.observedActions.add(action);
    }

    public void resetActions() {
        this.observedActions = new ArrayList<>();
    }

    private String getName() throws IllegalArgumentException {
        String newName = "";
        try {
            newName = dis.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (name.length() == 0 || name.length() > 12) {
            throw new IllegalArgumentException("This name is not the right length");
        }
        return newName;
    }


    @Override
    public Action placePenguin(IGameState state) throws TimeoutException {
        try {
            JsonArray function = new JsonArray();
            function.add(Constants.takeTurn);
            JsonObject jsonState = this.util.GameStateToJson(state);
            JsonArray parameters = new JsonArray();
            parameters.add(jsonState);
            function.add(parameters);

            this.dos.writeUTF(function.toString());

            if(this.jsonStreamParser.hasNext()) {
                JsonElement element = this.jsonStreamParser.next();
                return util.toPlacementAction(element, state);
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }


    @Override
    public Action movePenguin(IGameState state) throws TimeoutException {
        try {
            JsonArray function = new JsonArray();
            function.add(Constants.takeTurn);
            JsonObject jsonState = this.util.GameStateToJson(state);
            JsonArray parameters = new JsonArray();
            parameters.add(jsonState);
            parameters.add(this.util.GameActionsToJson(this.observedActions));
            function.add(parameters);

            this.dos.writeUTF(function.toString());

            if(this.jsonStreamParser.hasNext()) {
                JsonElement element = this.jsonStreamParser.next();
                return util.toMoveAction(element, state);
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    @Override
    public int getPlayerAge() {
        return this.age;
    }

    @Override
    public String getPlayerID() {
        return this.name;
    }


    @Override
    public boolean tournamentHasStarted() {
        try {
            JsonArray function = new JsonArray();
            function.add(Constants.startTournament);
            JsonArray parameters = new JsonArray();
            parameters.add(true);
            function.add(parameters);
            this.dos.writeUTF(function.toString());
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
    public void kickedForCheating() { }

    //Not necessary for remote interactions
    @Override
    public void gameResults(IGameResult result) { }

    @Override
    public boolean tournamentResults(boolean youWon) {
        try {
            JsonArray function = new JsonArray();
            function.add(Constants.end);
            JsonArray parameters = new JsonArray();
            parameters.add(youWon);
            function.add(parameters);
            this.dos.writeUTF(function.toString());
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public void playerColor(Color color) {
        try {
            JsonArray function = new JsonArray();
            function.add(Constants.playAs);
            JsonArray parameters = new JsonArray();
            parameters.add(ColorUtil.toColorString(color));
            function.add(parameters);
            this.dos.writeUTF(function.toString());
        } catch (IOException e) { }
    }

    @Override
    public void otherPlayerColors(List<Color> otherPlayers) {
        try {
            JsonArray function = new JsonArray();
            function.add(Constants.playAs);
            JsonArray parameters = new JsonArray();
            for (Color color : otherPlayers) {
                parameters.add(ColorUtil.toColorString(color));
            }
            function.add(parameters);
            this.dos.writeUTF(function.toString());
        } catch (IOException e) { }
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
