package model.strategy.testStrategies;

import java.awt.Point;
import model.state.IGameState;
import model.strategy.IStrategy;
import model.strategy.Strategy;
import model.tree.Action;
import model.tree.PlacePenguin;

/**
 * Represents a Strategy that will always try to make an movePenguin move, but place all penguins
 * correctly.
 *
 * When asked to provide a place to put a penguin, it will follow the same protocol of our In-House
 * AI Strategy.
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
public class PlacesPenguinsMovesWrong implements IStrategy {

    IStrategy strategy;

    /**
     * Constructor initializes this strategy with a copy of the In-House Strategy that would allow
     * it to exactly follow the choosePlacePenguinAction.
     */
    public PlacesPenguinsMovesWrong() {
        strategy = new Strategy();
    }

    @Override
    public Action choosePlacePenguinAction(IGameState state) {
        return this.strategy.choosePlacePenguinAction(state);
    }

    @Override
    public Action chooseMoveAction(IGameState state, int turns) {
        return new PlacePenguin(state.playerTurn(), new Point(1,1));
    }
}
