package model.strategy;

import java.awt.Point;
import model.state.IGameState;
import model.state.IPlayer;
import model.tree.Action;
import model.tree.MovePenguin;

/**
 * Represents a Strategy that will always try to make an Invalid move.
 *
 * It places a penguin in the next available free spot following a zig zag pattern that starts at
 * the top left corner for the Placement phase of Fish.
 *
 * When asked to make a move, the strategy will attempt to move their first penguin outside the board.
 *
 * This strategy is designed to break rules about player movement and should be removed from the
 * game for cheating.
 *
 * This Strategy is strictly for testing.  An In-House AI will never use this Strategy in a
 * Standard Game of Fish.
 */
public class MoveOutsideBoard implements IStrategy {

    private IStrategy strategy;

    public MoveOutsideBoard() {
        this.strategy = new Strategy();
    }

    @Override
    public Action choosePlacePenguinAction(IGameState state) {
        return this.strategy.choosePlacePenguinAction(state);
    }

    @Override
    public Action chooseMoveAction(IGameState state, int turns) {
        Point point = new Point(2 * state.getRows() * state.getColumns(),
            2 * state.getColumns() * state.getRows());
        IPlayer player = state.playerTurn();

        return new MovePenguin(player, player.getPenguins().get(0), point);
    }

}
