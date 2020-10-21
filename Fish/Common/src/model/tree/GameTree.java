package model.tree;

import java.awt.*;
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

    private IGameState state;
    private List<IGameTree> substates;

    /**
     * GameTree takes in a starting IGameState
     *
     * @param state IGameState
     */
    public GameTree(IGameState state) {
        this.state = state;
    }

    /**
     * Creates the substates for the given startState
     * @param startState starting state
     * @return List of IGameTree
     */
    private List<IGameTree> createSubstates(IGameState startState) {
        List<IGameTree> substates = new ArrayList<>();
        IPlayer player = startState.playerTurn();

        for (Map.Entry<IPenguin, List<Tile>> moves : startState.getPossibleMoves(player).entrySet()) {
            IPenguin penguin = moves.getKey();
            List<Tile> tiles = moves.getValue();

            IGameState state = startState.clone();

            // add all possible moves
            for (Tile tile : tiles) {
                IGameTree subtree = new GameTree(state.move(player, penguin, tile, false));
                substates.add(subtree);
            }
        }

        return substates;
    }

    @Override
    public IGameState getState() {
        return this.state;
    }

    @Override
    public IGameTree createCompleteTree() {
        this.substates = this.createSubstates(this.state);
        for (IGameTree substate : this.substates) {
            substate.createCompleteTree();
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
