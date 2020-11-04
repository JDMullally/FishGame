package model.strategy;

import java.awt.*;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.board.Tile;
import model.state.IGameState;
import model.state.IPlayer;
import model.tree.Action;
import model.tree.GameTree;
import model.tree.IGameTree;
import model.tree.PassPenguin;
import model.tree.PlacePenguin;

/**
 * Represents a Strategy for a Player in terms of placing penguins and making moves.
 *
 * It places a penguin in the next available free spot following a zig zag pattern that starts at
 * the top left corner for the Placement phase of Fish.
 *
 * The move strategy uses MiniMax to determine it's next action.  MiniMax realizes the minimal maximum
 * gain after looking ahead N > 0 turns for this player in the game tree for the current state.
 * The minimal maximum gain after N turns is the highest score a player can make after playing
 * the specified number of turns—assuming that all opponents pick one of the moves that minimizes
 * the player’s gain.
 *
 * In the event of a tie breaker, the strategy moves the penguin that has the lowest row number for
 * the place from which the penguin is moved and, within this row, the lowest column number.
 * In case this still leaves the algorithm with more than one choice, the process is repeated
 * for the target field to which the penguins will move.
 */
public class Strategy implements IStrategy {

    @Override
    public Action choosePlacePenguinAction(IGameState state) {
        if (state == null) {
            throw new IllegalArgumentException("State cannot be null");
        }

        IPlayer player = state.playerTurn();

        List<Tile> availableTiles = state.getPenguinPlacementTiles();
        if (availableTiles.isEmpty()) {
            throw new IllegalArgumentException("No available Tiles to place a penguin on");
        }

        return new PlacePenguin(player, availableTiles.get(0).getPosition());
    }

    /**
     * Inverts a Game Board representation.
     *
     * @param board the game board
     * @return inverted game board
     */
    private Tile[][] invertBoard(Tile[][] board) {
        Tile[][] newBoard = new Tile[board[0].length][board.length];

        for (Tile[] col : board) {
            for (Tile tile: col) {
                newBoard[tile.getPosition().y][tile.getPosition().x] = tile.clone();
            }
        }
        return newBoard;
    }

    @Override
    public Action chooseMoveAction(IGameState state, int turns) {
        if (state == null) {
            throw new IllegalArgumentException("State cannot be null");
        } else if (turns <= 0) {
            throw new IllegalArgumentException("Turns must be greater than zero");
        }

        int depth = state.getPlayers().size() * (turns - 1) + 1;
        IGameTree<?> gameTree = new GameTree(state).createCompleteTree();
        return this.minimax(gameTree, depth);
    }

    /**
     * Performs minimax on the given game tree, returning the best action for the current player
     * to make.
     *
     * @param tree IGameTree<?>
     * @return Action
     */
    private Action minimax(IGameTree<?> tree, int depth) {
        IGameState startState = tree.getState();
        Map<Action, IGameTree> substates = tree.getSubstates();
        IPlayer player = startState.playerTurn();

        // if the player can't move
        if (substates == null || substates.isEmpty()) {
            throw new IllegalStateException("The current player can't move");
        }

        // retrieves a score for all player actions
        Map<Action, Map.Entry<IGameState, Integer>> actions = new LinkedHashMap<>();
        for (Map.Entry<Action, IGameTree> map : substates.entrySet()) {
            Action action = map.getKey();
            IGameTree subtree = map.getValue();
            IGameState substate = subtree.getState();

            // create tree to depth-1 if applicable
            if (depth != 1) {
                subtree = subtree.createTreeToDepth(substate, depth - 1);
            }

            IGameState resultingState = this.minimaxHelper(subtree, player,depth - 1);

            actions.put(action, new AbstractMap.SimpleEntry<>(resultingState, this.evaluationFunction(resultingState, player)));
        }

        Map.Entry<Action, IGameState> bestAction = this.bestAction(actions, true);
        return bestAction.getKey();
    }

    /**
     * Performs minimax on the given game tree, returning the best score for the current player
     * to make, where the best score is a function of the difference between 'player' score and all
     * the other players scores.
     *
     * @param tree IGameTree<?>
     * @param depth int
     * @return int
     */
    private IGameState minimaxHelper(IGameTree<?> tree, IPlayer player, int depth) {
        IGameState state = tree.getState();
        Map<Action, IGameTree> substates = tree.getSubstates();

        // if depth is 0 or the game is over return the minimax score
        if (depth == 0 || state.isGameOver()) {
            return state;
        }

        // if the current player has no more moves, then return the state
        // if another player has no more moves, then pass their turn
        IPlayer curPlayer = state.playerTurn();
        if (substates.isEmpty() && player.equals(curPlayer)) {
            return state;
        } else if (substates.isEmpty()) {
            Action passAction = new PassPenguin(curPlayer);
            IGameState resultingState = passAction.apply(tree.getState());
            IGameTree resultingTree;
            if (depth == 1) {
                resultingTree = new GameTree(resultingState);
            } else {
                resultingTree = new GameTree(resultingState).createTreeToDepth(resultingState, depth - 1);
            }

            return this.minimaxHelper(resultingTree, player, depth - 1);
        }

        Map<Action, Map.Entry<IGameState, Integer>> actions = new LinkedHashMap<>();
        for (Map.Entry<Action, IGameTree> map : substates.entrySet()) {
            Action action = map.getKey();
            IGameTree subtree = map.getValue();

            IGameState resultingState = this.minimaxHelper(subtree, player,depth - 1);
            actions.put(action, new AbstractMap.SimpleEntry(resultingState, this.evaluationFunction(resultingState, player)));
        }

        Map.Entry<Action, IGameState> bestAction = this.bestAction(actions, player.equals(curPlayer));
        return bestAction.getValue();
    }

    /**
     * Returns the evaluation of a GameState for a given IPlayer which should always be the first
     * player to make a move in the tree.
     *
     * @param state IGameState
     * @param player IPlayer
     * @return int score
     */
    private int evaluationFunction(IGameState state, IPlayer player) {
        IPlayer validPlayer = null;
        for (IPlayer playerTemp : state.getPlayers()) {
            if (player.equals(playerTemp)) {
                validPlayer = playerTemp;
                break;
            }
        }

        assert validPlayer != null;
        return validPlayer.getScore();
    }

    /**
     * returns the best actions based on the best 'score' and performs a tiebreak if necessary.
     *
     * @param actions Map of Action to Map.Entry of IGameState to Integer
     * @param maximize boolean
     * @return Map.Entry of Action to IGameState
     */
    private Map.Entry<Action, IGameState> bestAction(Map<Action, Map.Entry<IGameState, Integer>> actions, boolean maximize) {
        int bestScore = maximize ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        Action bestAction = null;
        IGameState bestState = null;
        for (Map.Entry<Action, Map.Entry<IGameState, Integer>> map : actions.entrySet()) {
            Action action = map.getKey();
            IGameState state = map.getValue().getKey();
            Integer score = map.getValue().getValue();

            if (bestAction == null || (maximize ? score > bestScore : score < bestScore)) {
                bestScore = score;
                bestAction = action;
                bestState = state;
            } else if (score == bestScore) {
                Point from1 = action.getFromPosition();
                Point from2 = bestAction.getFromPosition();
                Point to1 = action.getToPosition();
                Point to2 = bestAction.getToPosition();

                // if the scores are the same but one penguins 'from' position is more desirable
                // or if they are the same penguin, choose the more desirable 'to' position
                if (from1.y < from2.y || (from1.y == from2.y && from1.x < from2.x)) {
                    bestAction = action;
                    bestState = state;
                } else if ((from1.y == from2.y && from1.x == from2.x) && (to1.y < to2.y || (to1.y == to2.y && to1.x < to2.x))) {
                    bestAction = action;
                    bestState = state;
                }
            }
        }

        return new AbstractMap.SimpleEntry<>(bestAction, bestState);
    }
}
