package model.strategy;

import java.awt.Point;
import model.state.IGameState;
import model.state.Penguin;
import model.tree.Action;
import model.tree.MovePenguin;
import model.tree.PlacePenguin;

/**
 *
 * Represents a Strategy that will always try to make an Invalid move.
 * 
 * When asked to provide a place to put a penguin, it will attempt to move a dummy penguin from
 * [1,1] to [1,1].
 *
 * When asked to make a move by the Referee, it will attempt to place a penguin on the board at
 * [1,1].
 *
 * This strategy is designed to break rules about player movement and should be removed from the
 * game for cheating.
 *
 * This Strategy is strictly for testing.  An In-House AI will never use this Strategy in a
 * Standard Game of Fish.
 */
public class BadStrategy implements IStrategy {

    @Override
    public Action choosePlacePenguinAction(IGameState state) {
        return new MovePenguin(state.playerTurn(), new Penguin(state.playerTurn().getColor(),
            new Point(1,1)), new Point(1,1));
    }

    @Override
    public Action chooseMoveAction(IGameState state, int turns) {
        return new PlacePenguin(state.playerTurn(), new Point(1,1));
    }
}
