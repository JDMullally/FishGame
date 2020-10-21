package model.state;

import model.board.IGameBoard;

/**
 * Represents the model for an immutable implementation of a GameState. No new methods are
 * required, as those from GameState are carried over. This interface is required however, as
 * to distinguish the difference between classes that implement a mutable vs immutable model.
 */
public interface ImmutableGameStateModel extends IGameState, IGameBoard {
}
