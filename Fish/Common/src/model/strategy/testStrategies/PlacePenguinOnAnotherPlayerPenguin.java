package model.strategy.testStrategies;

import java.awt.Point;
import model.state.IGameState;
import model.state.IPlayer;
import model.strategy.IStrategy;
import model.strategy.Strategy;
import model.tree.Action;
import model.tree.PlacePenguin;

/**
 * Represents a Strategy that will attempt to place a penguin outside the board.
 *
 * When asked to provide a place to put a penguin, it will look for an existing penguin that another
 * player has placed.  If it doesn't exist, it uses our in-house AI's strategy of placing penguins
 * in a zig zag pattern.  If it does exist, it tries to place a penguin on an existing player's penguin.
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
public class PlacePenguinOnAnotherPlayerPenguin implements IStrategy {

    private IStrategy strategy;
    /**
     * Basic constructor creates a Strategy for this IStrategy to use so it can eventually attempt
     * to place a penguin on another player's penguin.
     */
    public PlacePenguinOnAnotherPlayerPenguin() {
        this.strategy = new Strategy();
    }

    @Override
    public Action choosePlacePenguinAction(IGameState state) {
        IPlayer current = state.playerTurn();
        for (IPlayer player: state.getPlayers()) {
            if (!player.equals(current) && !player.getPenguins().isEmpty()) {
                Point usedPoint = player.getPenguins().get(0).getPosition();
                return new PlacePenguin(current, usedPoint);
            }
        }
        return strategy.choosePlacePenguinAction(state);
    }

    @Override
    public Action chooseMoveAction(IGameState state, int turns) {
        throw new IllegalStateException("This Strategy should never be able to move a Penguin");
    }
}
