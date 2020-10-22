package model.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import model.board.Tile;
import model.state.IGameState;
import model.state.IPenguin;
import model.state.IPlayer;

/**
 * A GameTree represents an entire game, starting from some state. For each state it connects to all
 * legal successor states. Each transition corresponds to a legal action of the player whose turn it
 * is in this state.
 */
public class GameTree<X> implements IGameTree<X> {

    // current state of the tree
    private IGameState state;

    // substates of the tree
    private List<IGameTree> substates;

    /**
     * GameTree takes in a starting IGameState
     *
     * @param state IGameState
     */
    public GameTree(IGameState state) {
        this.state = state.clone();
        this.substates = new ArrayList<>();
    }

    /**
     * GameTree that takes in a starting IGameState and a List of IGameState that
     * represents all reachable nodes.
     *
     * @param state IGameState
     * @param substates List of IGameState
     */
    public GameTree(IGameState state, List<IGameTree> substates) {
        if (state == null || substates == null) {
            throw new IllegalArgumentException("Can't have null states or substates");
        }

        this.state = state.clone();
        this.substates = substates;
    }

    /**
     * Creates the substates for the given startState
     *
     * @param startState starting state
     * @return List of IGameTree
     */
    private List<IGameTree> createSubstates(IGameState startState) {
        IPlayer player = startState.playerTurn();

        List<IGameTree> substates = new ArrayList<>();
        for (Map.Entry<IPenguin, List<Tile>> moves : startState.getPossibleMoves(player).entrySet()) {
            IPenguin penguin = moves.getKey().clone();
            List<Tile> tiles = moves.getValue();

            // add all possible moves
            for (Tile tile : tiles) {
                Action action = new Move(player, penguin, tile, false);
                IGameTree subtree = new GameTree(action.apply(startState.clone()));
                substates.add(subtree);
            }
        }
        return substates;
    }

    @Override
    public IGameState getState() {
        return this.state.clone();
    }

    @Override
    public List<IGameTree> getSubstates() {
        List<IGameTree> nodes = new ArrayList<>();
        for (IGameTree node : this.substates) {
            nodes.add(new GameTree(node.getState().clone(), node.getSubstates()));
        }
        return nodes;
    }

    @Override
    public IGameTree createCompleteTree() {
        if (this.substates.size() == 0) {
            this.substates = this.createSubstates(this.state);
            return this;
        }

        for (IGameTree tree : this.substates) {
            tree.createCompleteTree();
        }

        return this;
    }

    @Override
    public IGameTree createTreeToDepth(IGameState state, int depth) {
        if (depth == 0) {
            return this;
        }

        this.substates = this.createSubstates(this.state);
        for (IGameTree substate : this.substates) {
            substate.createTreeToDepth(substate.getState(), depth - 1);
        }

        return this;
    }

    @Override
    public IGameState queryAction(IGameState state, Action action) throws IllegalArgumentException {
        try {
            return action.apply(state.clone());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("queryAction: Action is illegal");
        }
    }

    @Override
    public List<X> applyFunction(IGameState state, Function<IGameState, X> func) {
        List<X> result = new ArrayList<>();
        for (IGameTree substate : this.createSubstates(state)) {
            result.add(func.apply(substate.getState().clone()));
        }

        return result;
    }
}
