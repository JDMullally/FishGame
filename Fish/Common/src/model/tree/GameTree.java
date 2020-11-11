package model.tree;

import java.util.LinkedHashMap;
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
public class GameTree implements IGameTree {

    // current state of the tree
    private final IGameState state;

    // substates of the tree
    private Map<Action, IGameTree> substates;

    /**
     * GameTree takes in a starting IGameState
     *
     * @param state IGameState
     */
    public GameTree(IGameState state) {
        this.state = state.clone();
        this.substates = new LinkedHashMap<>();
    }

    /**
     * GameTree that takes in a starting IGameState and a List of IGameState that
     * represents all reachable nodes.
     *
     * @param state IGameState
     * @param substates List of IGameState
     */
    public GameTree(IGameState state, Map<Action, IGameTree> substates) {
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
    private Map<Action, IGameTree> createSubstates(IGameState startState) {
        IPlayer player = startState.playerTurn();

        Map<Action, IGameTree> substates = new LinkedHashMap<>();

        // Deal with stuck players
        if (startState.isCurrentPlayerStuck()) {
            Action pass = new PassPenguin(player);
            IGameTree subtree = new GameTree(pass.apply(startState.clone()));
            substates.put(pass, subtree);
            return substates;
        }

        for (Map.Entry<IPenguin, List<Tile>> moves : startState.getPossibleMoves(player).entrySet()) {
            IPenguin penguin = moves.getKey().clone();
            List<Tile> tiles = moves.getValue();

            // add all possible moves
            for (Tile tile : tiles) {
                Action action = new MovePenguin(player, penguin, tile);
                IGameTree subtree = new GameTree(action.apply(startState.clone()));
                substates.put(action, subtree);
            }
        }
        return substates;
    }

    @Override
    public IGameState getState() {
        return this.state.clone();
    }

    @Override
    public Map<Action, IGameTree> getSubstates() {
        Map<Action, IGameTree> nodes = new LinkedHashMap<>();
        for (Map.Entry<Action, IGameTree> map : this.substates.entrySet()) {
            Action key = map.getKey();
            IGameTree value = map.getValue();

            nodes.put(key, new GameTree(value.getState().clone(), value.getSubstates()));
        }
        return nodes;
    }

    @Override
    public IGameTree createCompleteTree() {
        if (this.substates.size() == 0) {
            this.substates = this.createSubstates(this.state);
            return this;
        }

        for (Map.Entry<Action, IGameTree> map : this.substates.entrySet()) {
            map.getValue().createCompleteTree();
        }

        return this;
    }

    @Override
    public IGameTree createTreeToDepth(IGameState state, int depth) {
        if (depth == 0) {
            return this;
        }

        this.substates = this.createSubstates(this.state);
        for (Map.Entry<Action, IGameTree> map : this.substates.entrySet()) {
            IGameTree substate = map.getValue();
            substate.createTreeToDepth(substate.getState(), depth - 1);
        }

        return this;
    }

    @Override
    public IGameState queryAction(IGameState state, Action action) throws IllegalArgumentException {
        try {
            return action.apply(state.clone());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public <X> Map<Action, X> applyFunction(IGameState state, Function<IGameState, X> func) {
        Map<Action, X> result = new LinkedHashMap<>();
        for (Map.Entry<Action, IGameTree> entry : this.createSubstates(state).entrySet()) {
            Action action = entry.getKey();
            IGameTree substate = entry.getValue();
            result.put(action, func.apply(substate.getState().clone()));
        }

        return result;
    }
}
