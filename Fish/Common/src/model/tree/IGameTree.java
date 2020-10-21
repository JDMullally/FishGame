package model.tree;

import java.util.function.Function;

import model.state.IGameState;

/**
 * An IGameTree represents the interface for an entire game, starting from some state. For each
 * state it connects to all legal successor states. Each transition corresponds to a legal action of
 * the player whose turn it is in this state.
 */
public interface IGameTree<X> {

    /**
     * Creates a complete tree for this IGameTree's state to which players will not add any
     * more penguins.
     *
     * @return IGameTree
     */
    public IGameTree createCompleteTree();

    /**
     * For a given state and action either signals that the action is illegal or returns the state
     * that would result from taking the action.
     *
     * @param state IGameState
     * @param action Action
     * @return IGameState
     *
     * @throws IllegalArgumentException if the action is illegal
     */
    public IGameState queryAction(IGameState state, Action action) throws IllegalArgumentException;

    /**
     * Applies a specified function onto all states directly reachable from the given state.
     *
     * @param state IGameState
     * @param func Function<IGameState, X>
     */
    public void applyFunction(IGameState state, Function<IGameState, X> func);
}
