package model;


import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.board.GameBoard;
import model.board.IGameBoard;
import model.games.GameAction;
import model.games.IReferee;
import model.games.PlayerAI;
import model.games.Referee;
import model.state.IPlayer;
import model.state.Player;
import model.strategy.Strategy;
import model.tree.PlayerInterface;
import org.junit.Test;

import static org.junit.Assert.*;


public class RefereeTest {

    private IGameBoard newBoard;
    private List<PlayerInterface> players2, players3, players4;

    void init() {
        this.newBoard = new GameBoard(5,5,
            new ArrayList<>());

        PlayerInterface p1 = new PlayerAI(
            new Player(Color.BLACK, 4, new ArrayList<>()),
            new Strategy());

        PlayerInterface p2 = new PlayerAI(
            new Player(Color.RED, 5, new ArrayList<>()),
            new Strategy());

        PlayerInterface p3 = new PlayerAI(
            new Player(Color.WHITE, 5, new ArrayList<>()),
            new Strategy());

        PlayerInterface p4 = new PlayerAI(new Player(
            new Color(210, 105, 30), 6, new ArrayList<>()),
            new Strategy());

        this.players2 = Arrays.asList(p1, p2);
        this.players3 = Arrays.asList(p1, p2, p3);
        this.players4 = Arrays.asList(p1, p2, p3, p4);
    }


    @Test
    public void runGame() {
        this.init();

        IReferee ref = new Referee(players2);

        ref.runGame(this.newBoard.getRows(), this.newBoard.getColumns(),
            this.newBoard.getGameBoard());

        List<GameAction> actions = ref.getOngoingActions();
        for (GameAction action: actions) {
            System.out.println(action);
        }
    }

    @Test
    public void getGameResult() {
    }

    @Test
    public void getGameState() {
    }

    @Test
    public void getOngoingActions() {
    }
}
