package model.games;

import java.util.List;
import model.tree.PlayerInterface;

/**
 * An IGameResult represents the interface for the results of a single Fish game which should
 * include the placements of all players and a list of the players that attempted to cheat
 * in the game.
 */
public interface IGameResult {

    /**
     * Gets the placements of all players as an ordered (descending) list of PlayerInterface where
     * the PlayerInterface at index 0 placed first.
     *
     * @return List of PlayerInterface
     */
    List<PlayerInterface> getPlayerPlacements();

    /**
     * Gets a list of PlayerInterface that cheated during the game.
     *
     * @return List of PlayerInterface
     */
    List<PlayerInterface> getCheaters();

    /**
     * Gets the list of PlayerInterface that had the maximum score at the end of a game of Fish.
     *
     * @return List of PlayerInterface
     */
    List<PlayerInterface> getWinners();

    /**
     * Gets the list of PlayerInterface that had less than the maximum score at the end of a game of Fish.
     *
     * @return List of PlayerInterface
     */
    List<PlayerInterface> getEliminated();

    /**
     * Gets the score of the given PlayerInterface.
     * @param player PlayerInterface
     * @return int
     */
    int getPlayerScore(PlayerInterface player);
}
