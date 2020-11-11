package model.strategy;

import model.state.IGameState;
import model.tree.Action;

/**
 * The Interface of a Strategy for players. A player's Strategy should have a strategy for placing
 * penguins and choosing a move to make.
 */
public interface IStrategy {

    /**
     * Takes in the current GameState and returns a Placement Action for placing a Penguin in the
     * next available free spot following a zig zag pattern that starts at the top left corner.
     * The search goes from left to right in each row and moves down to the next row when
     * one is filled up.
     *
     * This method returns an Action that contains the new Point where their penguin will be placed.
     * See {@link model.tree.PlacePenguin}
     *
     * @param state IGameState
     * @return Action
     */
    Action choosePlacePenguinAction(IGameState state);

    /**
     * Takes in the current GameState and returns a Move Action for moving a Penguin to the
     * preferred spot. It picks the action that realizes the minimal maximum gain after looking
     * ahead N > 0 turns for this player in the game tree for the current state.
     *
     * This method returns an Action that contains the new Point where their penguin will be placed.
     * See {@link model.tree.MovePenguin}
     *
     * @param state IGameState
     * @param turns int
     * @return Action
     */
    Action chooseMoveAction(IGameState state, int turns);
}
