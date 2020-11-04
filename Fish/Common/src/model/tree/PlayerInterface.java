package model.tree;

import model.state.IGameState;
import model.state.IPlayer;

/**
 * This interface represents a Player's interface for interacting with the game.
 * The Player interface must spell out how to place their avatars on the initial board,
 * how to take turns, and how/whether to receive information about the end of a game.
 */
public interface PlayerInterface {

    /**
     * A Player takes in an IGameState from a Referee. The player then returns an Action to place a
     * Penguin. This method can only be called during the initial place penguin phase of the game.
     *
     * @param state IGameState
     * @return Action
     */
    Action placePenguin(IGameState state);

    /**
     * A Player takes in an IGameState from a Referee. The player then returns an Action to make a
     * move. This method can only be called after the initial place penguin phase of the game.
     *
     * @param state IGameState
     * @return Action
     */
    Action movePenguin(IGameState state);
}
