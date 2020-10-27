package model.strategy;

import java.awt.Point;
import java.util.List;
import model.board.Tile;
import model.state.IGameState;
import model.state.IPenguin;
import model.state.IPlayer;
import model.state.Penguin;
import model.tree.Action;
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
    public Action placePenguins(IGameState state) {
        IPlayer player = state.playerTurn();

        List<Tile> availableTiles = state.getPenguinPlacementTiles();
        if (availableTiles.isEmpty()) {
            throw new IllegalArgumentException("No available Tiles to place a penguin on");
        }

        return new PlacePenguin(player, availableTiles.get(0).getPosition());
    }

    @Override
    public Action makeMove(IGameState state, int turns) {
        return null;
    }
}
