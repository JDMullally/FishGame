package model.testPlayerAI;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.TimeoutException;
import model.games.IGameResult;
import model.state.IGameState;
import model.strategy.IStrategy;
import model.tree.Action;
import model.tree.PlayerInterface;

public class DoesNotWantGameResult implements PlayerInterface {

    private IStrategy strategy;

    public DoesNotWantGameResult(IStrategy strat) {
        this.strategy = strat;
    }

    @Override
    public Action placePenguin(IGameState state) throws TimeoutException {
        return strategy.choosePlacePenguinAction(state);
    }

    @Override
    public Action movePenguin(IGameState state) throws TimeoutException {
        return strategy.chooseMoveAction(state, 2);
    }

    @Override
    public int getPlayerAge() {
        return 20;
    }

    @Override
    public String getPlayerID() {
        return "ull";
    }

    @Override
    public boolean tournamentHasStarted() {
        return true;
    }

    @Override
    public boolean gameHasStarted() {
        return true;
    }

    @Override
    public void kickedForCheating() {

    }

    @Override
    public void gameResults(IGameResult result) {
        while(true) { }
    }

    @Override
    public boolean tournamentResults(boolean youWon) {
        return false;
    }

    @Override
    public void playerColor(Color color) {

    }

    @Override
    public void otherPlayerColors(List<Color> otherPlayers) {

    }

    @Override
    public void getOnGoingAction(Action action) {

    }

    @Override
    public void clearOnGoingAction() {

    }
}
