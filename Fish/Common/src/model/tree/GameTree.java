package model.tree;

import java.util.List;
import java.util.function.Function;

import model.state.IGameState;

/**
 * A GameTree represents an entire game, starting from some state. For each state it connects to all
 * legal successor states. Each transition corresponds to a legal action of the player whose turn it
 * is in this state.
 */
public class GameTree implements IGameTree {

    private IGameState state;

    private List<IGameTree> substates;

    @Override
    public IGameTree createCompleteTree() {
        return null; // todo
    }

    @Override
    public IGameState queryAction(IGameState state, Action action) throws IllegalArgumentException {
        return null; // todo
    }

    @Override
    public void applyFunction(IGameState state, Function func) {
        // todo
    }
}
