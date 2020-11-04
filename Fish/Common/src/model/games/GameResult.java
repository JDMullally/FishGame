package model.games;

import java.util.ArrayList;
import java.util.List;
import model.tree.PlayerInterface;

/**
 * A GameResult represents the results of a single Fish game which should include the placements of
 * all players and a list of the players that attempted to cheat in the game.
 */
public class GameResult implements IGameResult {

    private List<PlayerInterface> placements, cheaters;

    /**
     * Constructor for GameResult.
     *
     * @param placements List of IPlayer
     * @param cheaters List of IPlayer
     */
    GameResult(List<PlayerInterface> placements, List<PlayerInterface> cheaters) {
        if (placements == null || cheaters == null) {
            throw new IllegalArgumentException("Player Placements and Cheaters cannot be null");
        }

        this.placements = placements;
        this.cheaters = cheaters;
    }

    @Override
    public List<PlayerInterface> getPlayerPlacements() {
        return new ArrayList<>(this.placements);
    }

    @Override
    public List<PlayerInterface> getCheaters() {
        return new ArrayList<>(this.cheaters);
    }
}
