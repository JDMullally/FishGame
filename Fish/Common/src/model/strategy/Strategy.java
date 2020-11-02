package model.strategy;

import java.awt.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.board.Tile;
import model.state.IGameState;
import model.state.IPenguin;
import model.state.IPlayer;
import model.tree.Action;
import model.tree.GameTree;
import model.tree.IGameTree;
import model.tree.MovePenguin;
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

            // if the player has no moves to make
            if (tiles.isEmpty()) {
                continue;
            }

            Map<Action, Integer> actions = new HashMap<>();
            for (Tile tile : tiles) {
                Action action = new MovePenguin(player, penguin, tile, false);
                IGameTree subtree = new GameTree(action.apply(startState.clone()));
                IGameState resultingState = this.minimaxHelper(subtree, depth - 1);
                actions.put(action, this.evaluationFunction(resultingState, player));
            }
            penguinActions.put(penguin, actions);
        }

        // if the player cannot move
        if (penguinActions.isEmpty()) {
            throw new IllegalStateException("The current player can't move");
        }

        // returns the best actions based on the best 'score' and performs a tiebreak if necessary
        int bestScore = Integer.MIN_VALUE;
        Action bestAction = null;
        for (Map.Entry<IPenguin, Map<Action, Integer>> entry : penguinActions.entrySet()) {
            for (Map.Entry<Action, Integer> entry2 : entry.getValue().entrySet()) {
                Action action = entry2.getKey();
                Integer score = entry2.getValue();

                if (bestAction == null || score > bestScore) {
                    bestScore = score;
                    bestAction = action;
                } else if (score == bestScore) {
                    Point from1 = action.getFromPosition();
                    Point from2 = bestAction.getFromPosition();
                    Point to1 = action.getToPosition();
                    Point to2 = bestAction.getToPosition();

                    // if the scores are the same but one penguins 'from' position is more desirable
                    // or if they are the same penguin, choose the more desirable 'to' position
                    if (from1.y < from2.y || (from1.y == from2.y && from1.x < from2.x)) {
                        bestAction = action;
                    } else if ((from1.y == from2.y && from1.x == from2.x) && (to1.y < to2.y || (to1.y == to2.y && to1.x < to2.x))) {
                        bestAction = action;
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
     * @param depth int
     * @return int
     */
    private IGameState minimaxHelper(IGameTree<?> tree, int depth) {
        IGameState state = tree.getState();
        List<IGameTree> substates = tree.getSubstates();

        // if depth is 0, the game is over, or there are no moves then return the minimax score
        if (depth == 0 || state.isGameOver() || substates.isEmpty()) {
            return state;
        }

        IPlayer player = state.playerTurn();

        LinkedHashMap<IGameState, Integer> scores = new LinkedHashMap<>();
        for (IGameTree subtree : substates) {
            IGameState resultingState = this.minimaxHelper(subtree, depth - 1);
            scores.put(resultingState, this.evaluationFunction(resultingState, player));
        }

        // returns the IGameState with the maximum score
        return Collections.max(scores.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
    }

    /**
     * Returns the evaluation of a GameState for a given Iplayer based on the difference in player
     * scores compared to this player's score.
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
            }
        }

        if (validPlayer == null) {
            throw new IllegalArgumentException("Invalid player specified");
        }

        int score = 0;
        for (IPlayer playerTemp : state.getPlayers()) {
            if (!player.equals(playerTemp)) {
                score += validPlayer.getScore() - player.getScore();
            }
        }
        return score;
    }
}