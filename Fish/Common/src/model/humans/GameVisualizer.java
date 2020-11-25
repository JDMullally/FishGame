package model.humans;

import java.util.List;
import java.util.concurrent.TimeUnit;
import model.games.GameAction;
import model.games.IGameAction;
import model.games.IGameResult;
import model.state.ImmutableGameState;
import model.state.ImmutableGameStateModel;


public class GameVisualizer implements IGameVisualizer {

    private ImmutableGameStateModel initialState;
    private List<IGameObserver> observers;
    private ImmutableGameStateModel currentState;
    private List<IGameAction> actions;
    private IGameResult result;

    public GameVisualizer(ImmutableGameStateModel initialState,
        List<IGameAction> actions, IGameResult result, List<IGameObserver> observers) {
        if (initialState == null || actions == null || result == null  || observers == null) {
            throw new IllegalArgumentException("Cannot have a null initial state, list of actions,"
                + "results or observers");
        }
        this.initialState = initialState;
        this.observers = observers;
        this.actions = actions;
        this.result = result;
    }

    @Override
    public void sendGame() throws InterruptedException {
        this.currentState = new ImmutableGameState(this.initialState.clone());
        this.updateAllObserverInitialState();
        TimeUnit.SECONDS.sleep(1);

        for (IGameAction action: actions) {
            this.currentState = nextScene(action, this.currentState);
            this.updateAllObserversAction(action);
            TimeUnit.SECONDS.sleep(1);
        }

        this.updateAllObserverResults();
    }

    @Override
    public ImmutableGameStateModel nextScene(IGameAction action,
        ImmutableGameStateModel currentImmutableGameState) {

        currentImmutableGameState = currentImmutableGameState.getNextGameState(action);
        return currentImmutableGameState;
    }

    @Override
    public void updateAllObserversAction(IGameAction action) {
        for (IGameObserver observer: observers) {
            observer.updateAction(action);
        }
    }

    @Override
    public void updateAllObserverResults() {
        for (IGameObserver observer: observers) {
            observer.updateResults(this.result);
        }
    }

    @Override
    public void updateAllObserverInitialState() {
        for (IGameObserver observer: observers) {
            observer.sendInitialState(this.initialState);
        }
    }
}
