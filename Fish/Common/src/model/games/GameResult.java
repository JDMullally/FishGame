package model.games;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import model.tree.PlayerInterface;

/**
 * A GameResult represents the results of a single Fish game which should include the placements of
 * all players and a list of the players that attempted to cheat in the game.
 */
public class GameResult implements IGameResult {

    private List<PlayerInterface> cheaters;
    private Map<PlayerInterface, Integer> finalScores;

    /**
     * Constructor for GameResult.
     *
     * @param finalScores HashMap of PlayerInterface to Score.
     * @param cheaters List of PlayerInterface
     */
    GameResult(Map<PlayerInterface, Integer> finalScores, List<PlayerInterface> cheaters) {
        if (finalScores == null || cheaters == null) {
            throw new IllegalArgumentException("Player Placements and Cheaters cannot be null");
        }

        this.finalScores = finalScores;
        this.cheaters = cheaters;
    }

    @Override
    public List<PlayerInterface> getPlayerPlacements() {
        return new ArrayList<>(this.finalScores.keySet());
    }

    @Override
    public List<PlayerInterface> getWinners() {
        Integer max = Collections.max(this.finalScores.values());
        List<PlayerInterface> winners = new ArrayList<>();

        for (Map.Entry<PlayerInterface, Integer> entry : this.finalScores.entrySet()) {
            if (entry.getValue().equals(max)) {
                winners.add(entry.getKey());
            }
        }

        return winners;
    }

    @Override
    public List<PlayerInterface> getCheaters() {
        return new ArrayList<>(this.cheaters);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GameResult) {
            GameResult other = (GameResult) o;
            return this.finalScores.equals(other.finalScores)
                && this.cheaters.equals(other.cheaters);
        }
        return false;
    }
}
