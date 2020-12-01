package model.server;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.TimeoutException;
import model.games.IGameResult;
import model.state.IGameState;
import model.tree.Action;
import model.tree.PlayerInterface;

public class ClientProxy implements PlayerInterface {

    private Client client;

    public ClientProxy(Client client, int age, String id) {
        if (client == null || id == null ||id.length() > 12) {
            throw new IllegalArgumentException("Invalid ClientProxy Player");
        }
        this.client = client;
    }

    @Override
    public Action placePenguin(IGameState state) throws TimeoutException {
        return this.client.setUp(state);
    }

    @Override
    public Action movePenguin(IGameState state) throws TimeoutException {
        return this.client.takeTurn(state);
    }

    @Override
    public int getPlayerAge() {
        return this.client.getAge();
    }

    @Override
    public String getPlayerID() {
        return this.client.getID();
    }

    @Override
    public boolean tournamentHasStarted() {
        this.client.start(true);
        return true;
    }

    @Override
    public boolean gameHasStarted() {
        return true;
    }

    @Override
    public void kickedForCheating() { }

    @Override
    public void gameResults(IGameResult result) { }

    @Override
    public boolean tournamentResults(boolean youWon) {
        this.client.end(youWon);
        return true;
    }

    @Override
    public void playerColor(Color color) {
        this.client.playingAs(color);
    }

    @Override
    public void otherPlayerColors(List<Color> otherPlayers) {
        this.client.playingWith(otherPlayers);
    }
}
