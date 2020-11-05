package model.strategy;

import java.awt.Point;
import model.state.IGameState;
import model.tree.Action;

/**
 * Represents a Strategy that will always try to make an Invalid move.
 *
 * It places a penguin in the next available free spot following a zig zag pattern that starts at
 * the top left corner for the Placement phase of Fish.
 *
 * When asked to make a move, the strategy will create a state where it is another player's turn.
 * It will then attempt to move another players penguin based on the In-House AI strategy.  This AI
 * attempts to abuse the fact that a state has public methods and can be manipulated.
 *
 * This strategy is designed to break rules about player movement and should be removed from the
 * game for cheating.
 *
 * This Strategy is strictly for testing.  An In-House AI will never use this Strategy in a
 * Standard Game of Fish.
 */
public class MoveAnotherPlayerPenguin implements IStrategy{

    private IStrategy strategy;

    public MoveAnotherPlayerPenguin() {
        this.strategy = new Strategy();
    }

    @Override
    public Action choosePlacePenguinAction(IGameState state) {
        return this.strategy.choosePlacePenguinAction(state);
    }

    @Override
    public Action chooseMoveAction(IGameState state, int turns) {
        state = state.move(
            state.playerTurn(),
            state.playerTurn().getPenguins().get(0),
            new Point(1,1),
            true);

        return this.strategy.chooseMoveAction(state, turns);
    }
}
