package model.testStrategies;

import java.awt.Point;
import model.state.IGameState;
import model.state.IPlayer;
import model.strategy.IStrategy;
import model.strategy.Strategy;
import model.tree.Action;
import model.tree.MovePenguin;
import model.tree.PassPenguin;

/**
 * Represents a Strategy that will always try to make an Invalid move.
 *
 * It places a penguin in the next available free spot following a zig zag pattern that starts at
 * the top left corner for the Placement phase of Fish.
 *
 * When asked to make a move, the strategy will check if there are other players. If there are, it
 * will pass it's own turn using the GameState's public methods and attempt to make another player
 * cheat by having them move to an invalid slot.  Otherwise, they will use the same strategy for
 * choosing a move as our In-House AI.
 *
 * This strategy is designed to break rules about player movement and should be removed from the
 * game for cheating.
 *
 * This Strategy is strictly for testing.  An In-House AI will never use this Strategy in a
 * Standard Game of Fish.
 */
public class PassOwnTurnMakeAnotherPlayerCheat implements IStrategy {

    private IStrategy strategy;

    /**
     * Constructor for Passing their own turn and moving another Player's Penguin to an invalid
     * location only needs to create a new In-House AI Strategy.
     */
    public PassOwnTurnMakeAnotherPlayerCheat() {
        this.strategy = new Strategy();
    }

    @Override
    public Action choosePlacePenguinAction(IGameState state) {
        return this.strategy.choosePlacePenguinAction(state);
    }

    @Override
    public Action chooseMoveAction(IGameState state, int turns) {
        if(state.getPlayers().size() > 1) {
            Action passToOtherPlayer = new PassPenguin(state.playerTurn());
            state = passToOtherPlayer.apply(state);

            Point point = new Point(2 * state.getRows() * state.getColumns(),
                2 * state.getColumns() * state.getRows());
            IPlayer player = state.playerTurn();

            Action invalidMove = new MovePenguin(player, player.getPenguins().get(0), point);
            return invalidMove;
        } else {
            return this.strategy.chooseMoveAction(state, turns);
        }
    }
}
