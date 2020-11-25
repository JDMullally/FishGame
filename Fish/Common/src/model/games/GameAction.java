package model.games;

import model.state.IGameState;
import model.state.ImmutableGameState;
import model.state.ImmutableGameStateModel;
import model.tree.Action;

/**
 * A GameAction represents ongoing actions in a Fish game that are deemed important. This can
 * include A Player placing a penguin, a player moving a penguin, a player being forced to pass
 * their turn, a player cheating, and a game ending. A GameAction also includes a toString
 * representation of a GameAction.
 */
public class GameAction implements IGameAction{

    private Action action;

    /**
     * Constructor takes in an Action.
     *
     * @param action Action
     */
    public GameAction(Action action) {
        if (action == null) {
            throw new IllegalArgumentException("Action cannot be null");
        }

        this.action = action;
    }

    @Override
    public String toString() {
        return action.toString();
    }

    @Override
    public ImmutableGameStateModel getNextState(ImmutableGameStateModel model) {
        IGameState state = model.clone();

        IGameState nextState = action.apply(state);

        return new ImmutableGameState(nextState);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GameAction) {
            GameAction other = (GameAction) o;
            return this.action.equals(other.action);
        }
        return false;
    }
}
