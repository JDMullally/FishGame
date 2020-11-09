package model.tree;

import java.util.Map;
import java.util.function.Function;

import model.state.IGameState;

/**
 * An IGameTree represents the interface for an entire game, starting from some state. For each
 * state it connects to all legal successor states. Each transition corresponds to a legal action of
 * the player whose turn it is in this state.
 */
public interface IGameTree {

    /**
     * Returns the topmost IGameState of the GameTree.
     *
     * @return IGameState
     */
    IGameState getState();

    /**
     * Returns the currently reachable substates of the current GameTree and the Actions taken to
     * get to those substates.
     *
     *  @return Map of Action to IGameTree
     */
    Map<Action, IGameTree> getSubstates();

    /**
     * Creates a complete tree for this IGameTree's state to which players will not add any
     * more penguins. This is done in the following manner:
     *
     * Every time the method is called, a new subtree for the bottom most node(s) in the tree will
     * be created. i.e. the resulting IGameTree that is returned will have 1 more depth to it.
     *
     * As a result, this method doesn't produce an 'infinite' or 'very large' tree, but instead
     * systematically creates the tree when the user wants to see more of it.
     *
     * @return IGameTree
     */
    IGameTree createCompleteTree();

    /**
     * Creates a tree up to a given depth with a given starting state.
     * The starting state will not add any new penguins.
     *
     * @param state IGameState
     * @param depth int
     * @return IGameTree
     */
    IGameTree createTreeToDepth(IGameState state, int depth);

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
    IGameState queryAction(IGameState state, Action action) throws IllegalArgumentException;

    /**
     * Applies a specified function onto all states directly reachable from the given state.
     *
     * @param state IGameState
     * @param func Function<IGameState, X>
     * @return the resulting substates after applying the function to them
     */
    <X> Map<Action, X> applyFunction(IGameState state, Function<IGameState, X> func);
}
