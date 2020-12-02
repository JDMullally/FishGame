package model.testPlayerAI;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.TimeoutException;
import model.games.IGameResult;
import model.state.IGameState;
import model.strategy.IStrategy;
import model.tree.Action;
import model.tree.PlayerInterface;
/**
 * This Class represents a in-house AI Player's interface for testing timeout interactions
 * with the game.  The Timeout AI should be removed from the game for maliciously attempting to
 * timeout the game.
 *
 */
public class TimeOutWinner implements PlayerInterface {

    private IStrategy strategy;

    public TimeOutWinner(IStrategy strat) {
        this.strategy = strat;
    }

    @Override
    public Action placePenguin(IGameState state) throws TimeoutException {
        return this.strategy.choosePlacePenguinAction(state);
    }

    @Override
    public Action movePenguin(IGameState state) throws TimeoutException {
       return this.strategy.chooseMoveAction(state, 2);
    }

    @Override
    public int getPlayerAge() {
        return 20;
    }

    @Override
    public String getPlayerID() {
        return "I will never accept winning";
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
        while(true) {

        }
    }

    @Override
    public void gameResults(IGameResult result) {

    }

    @Override
    public boolean tournamentResults(boolean youWon) {
        while(youWon) {}
        return true;
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
