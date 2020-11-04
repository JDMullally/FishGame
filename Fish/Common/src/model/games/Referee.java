package model.games;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import model.state.GameState;
import model.state.IGameState;
import model.state.IPlayer;
import model.state.Player;
import model.tree.Action;
import model.tree.PlayerInterface;

public class Referee implements IReferee{


    private List<PlayerInterface> players;
    private List<PlayerInterface> cheaters;
    private Action mostRecentAction;

    /**
     * Constructor for Referee that takes in a List of Players. The Referee may assume that the
     * sequence is arranged in ascending order of player age.
     * @param players
     */
    public Referee(List<PlayerInterface> players) {
        if(players == null) {
            throw new IllegalArgumentException("A Referee cannot start a Game with null Players");
        }

        this.players = players;
        this.cheaters = new ArrayList<>();
    }

    private IGameState createIntitialGame(List<PlayerInterface> players) {
        return null;
    }

    @Override
    public IGameResult startGame() {
        IGameState start = this.createIntitialGame(this.players);

        return null;
    }

    @Override
    public IGameResult getGameResult() throws IllegalStateException {
        return null;
    }

    @Override
    public GameAction getGameAction() {
        return new GameAction(this.mostRecentAction);
    }
}
