package model.testPlayerAI;

import java.util.concurrent.TimeoutException;
import model.games.IGameResult;
import model.state.IGameState;
import model.strategy.IStrategy;
import model.tree.Action;
import model.tree.PlayerInterface;

public class TournamentStaller implements PlayerInterface {

    private IStrategy strategy;

    public TournamentStaller(IStrategy strat) {
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
        return "I stall";
    }

    @Override
    public boolean tournamentHasStarted() {
        while (true) { }
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

    }

    @Override
    public boolean tournamentResults(boolean youWon) {
        return true;
    }
}
