package model.strategy;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import model.board.Tile;
import model.state.IGameState;
import model.state.IPenguin;
import model.state.IPlayer;
import model.state.Penguin;
import model.tree.Action;
import model.tree.GameTree;
import model.tree.IGameTree;
import model.tree.Move;
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

    @Override
    public Action chooseMoveAction(IGameState state, int turns) {
        if (state == null) {
            throw new IllegalArgumentException("State cannot be null");
        } else if (turns <= 0) {
            throw new IllegalArgumentException("Turns must be greater than zero");
        }

        IGameTree<?> gameTree = new GameTree(state).createTreeToDepth(state, turns);
        return this.minimax(gameTree, turns);
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
        IPlayer player = startState.playerTurn();

        // retrieves a score for a all actions where 'score' is a function of the difference between
        // 'player' score and all the other players scores.
        Map<IPenguin, Map<Action, Integer>> penguinActions = new LinkedHashMap<>();
        for (Map.Entry<IPenguin, List<Tile>> moves : startState.getPossibleMoves(player).entrySet()) {
            IPenguin penguin = moves.getKey().clone();
            List<Tile> tiles = moves.getValue();

            Map<Action, Integer> actions = new HashMap<>();
            for (Tile tile : tiles) {
                Action action = new Move(player, penguin, tile, false);
                IGameTree subtree = new GameTree(action.apply(startState.clone()));
                actions.put(action, this.minimaxHelper(subtree, player, depth - 1));
            }

            penguinActions.put(penguin, actions);
        }

        // returns the best actions based on the best 'score' and performs a tiebreak if necessary
        int bestScore = Integer.MIN_VALUE;
        Action bestAction = null;
        IPenguin bestPenguin = null;
        for (Map.Entry<IPenguin, Map<Action, Integer>> entry : penguinActions.entrySet()) {
            IPenguin penguin = entry.getKey();

            for (Map.Entry<Action, Integer> entry2 : entry.getValue().entrySet()) {
                Action action = entry2.getKey();
                Integer score = entry2.getValue();

                if (bestAction == null || score > bestScore) {
                    bestScore = score;
                    bestAction = action;
                    bestPenguin = penguin;
                } else if (score == bestScore) {
                    Point point1 = penguin.getPosition();
                    Point point2 = bestPenguin.getPosition();

                    if (point1.y < point2.y || point1.y == point2.y && point1.x < point2.x) {
                        bestAction = action;
                        bestPenguin = penguin;
                    }
                }
            }
        }

        return bestAction;
    }

    /**
     * Performs minimax on the given game tree, returning the best score for the current player
     * to make, where the best score is a function of the difference between 'player' score and all
     * the other players scores.
     *
     * @param tree IGameTree<?>
     * @param player IPlayer
     * @param depth int
     * @return int
     */
    private int minimaxHelper(IGameTree<?> tree, IPlayer player, int depth) {
        IGameState state = tree.getState();

        // if depth is 0 or the game is over, return the minimax score
        if (depth == 0 || state.isGameOver()) {
            int score = 0;
            for (IPlayer playerTemp : state.getPlayers()) {
                if (player.equals(playerTemp)) {
                    score += player.getScore();
                } else {
                    score -= player.getScore();
                }
            }
            return score;
        }

        List<Integer> scores = new ArrayList<>();
        for (IGameTree subtree : tree.getSubstates()) {
            scores.add(this.minimaxHelper(subtree, player, depth - 1));
        }
        return Collections.max(scores);
    }
}
