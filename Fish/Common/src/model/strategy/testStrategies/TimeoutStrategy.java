package model.strategy.testStrategies;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import model.state.IGameState;
import model.strategy.IStrategy;
import model.strategy.Strategy;
import model.tree.Action;
import model.tree.PlayerInterface;

public class TimeoutStrategy implements IStrategy {

    private IStrategy strategy;

    public TimeoutStrategy() {
        this.strategy = new Strategy();
    }

    @Override
    public Action choosePlacePenguinAction(IGameState state) {
        try {
            TimeUnit.MINUTES.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this.strategy.choosePlacePenguinAction(state);
    }

    @Override
    public Action chooseMoveAction(IGameState state, int turns) {
        return this.strategy.choosePlacePenguinAction(state);
    }
}
