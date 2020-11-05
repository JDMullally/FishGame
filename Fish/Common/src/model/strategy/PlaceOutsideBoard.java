package model.strategy;

import java.awt.Point;
import model.state.IGameState;
import model.state.IPlayer;
import model.tree.Action;
import model.tree.PlacePenguin;

/**
 * Represents a Strategy that will attempt to place a penguin outside the board.
 *
 * When asked to provide a place to put a penguin, it will attempt to place a penguin at an x and y
 * coordinate equal to twice the product of the total number of rows and columns.  This point is
 * guaranteed to be outside any board.
 *
 * This strategy should not be able to make a move as it cannot place a valid penguin, so if
 * chooseMoveAction is called it throws an IllegalStateException.
 *
 * This strategy is designed to break rules about player movement and should be removed from the
 * game for cheating.
 *
 * This Strategy is strictly for testing.  An In-House AI will never use this Strategy in a
 * Standard Game of Fish.
 */
public class PlaceOutsideBoard implements IStrategy {

    @Override
    public Action choosePlacePenguinAction(IGameState state) {
        Point point = new Point(2 * state.getRows() * state.getColumns(),
            2 * state.getColumns() * state.getRows());
        IPlayer player = state.playerTurn();

        return new PlacePenguin(player, point);
    }

    @Override
    public Action chooseMoveAction(IGameState state, int turns) {
        throw new IllegalStateException("This Strategy should never be able to Move a Penguin");
    }
}
