package model.testStrategies;

import java.util.concurrent.TimeUnit;
import model.state.IGameState;
import model.strategy.IStrategy;
import model.strategy.Strategy;
import model.tree.Action;

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
            throw new IllegalStateException();
        }
        return this.strategy.choosePlacePenguinAction(state);
    }

    @Override
    public Action chooseMoveAction(IGameState state, int turns) {
        return this.strategy.choosePlacePenguinAction(state);
    }
}
