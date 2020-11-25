package model.humans;

import model.games.IGameAction;
import model.games.IGameResult;
import model.state.ImmutableGameStateModel;
import view.IView;
import view.VisualView;

/**
 * A GameObserver is implementation of IGameObserver that creates an Observer for the game.
 */
public class GameObserver implements IGameObserver {

    private ImmutableGameStateModel currentState;
    private IView view;

    @Override
    public void sendInitialState(ImmutableGameStateModel newState) {
        this.currentState = newState;
        this.view = new VisualView(this.currentState);
        this.view.makeVisible();
    }

    @Override
    public void updateAction(IGameAction action) {
        this.currentState = this.currentState.getNextGameState(action);
        this.view.update(this.currentState);
    }

    @Override
    public void updateResults(IGameResult result) {
        view.update(this.currentState, result);
    }

}
