package model.strategy.testStrategies;

import java.awt.Point;
import model.state.IGameState;
import model.state.Penguin;
import model.strategy.IStrategy;
import model.tree.Action;
import model.tree.MovePenguin;
import model.tree.PassPenguin;
import model.tree.PlacePenguin;

/**
 * Represents a Strategy that will always try to make an Invalid move.
 *
 * When asked to provide a place to put a penguin, it will attempt to place a Penguin for
 * Another Player.  If there is no other player, it will attempt to Pass which will guarantee that
 * this strategy cheats.
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
public class PlaceAnotherPlayerPenguin implements IStrategy {

    @Override
    public Action choosePlacePenguinAction(IGameState state) {
        if(state.getPlayers().size() > 1) {
            return new PlacePenguin(state.getPlayers().get(1), new Point(1,1));
        } else {
            return new PassPenguin(state.playerTurn());
        }
    }

    @Override
    public Action chooseMoveAction(IGameState state, int turns) {
        throw new IllegalStateException("This Strategy should not be able to make a move");
    }
}
