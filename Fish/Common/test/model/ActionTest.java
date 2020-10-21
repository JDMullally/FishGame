package model;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.state.GameState;
import model.state.IGameState;
import model.state.IPenguin;
import model.state.IPlayer;
import model.state.Penguin;
import model.state.Player;
import model.tree.Action;
import model.tree.Move;
import org.junit.Test;
import static org.junit.Assert.*;


public class ActionTest {

    private List<IPenguin> penguins1, penguins2;
    private IPlayer player1, player2;
    private List<IPlayer> players;
    private IGameState gameState;
    private Action pass, moveUp;

    public void init() {
        IPenguin p1= new Penguin(Color.BLACK, new Point(1,1));
        IPenguin p2= new Penguin(Color.BLACK, new Point(0,1));
        IPenguin p3= new Penguin(Color.BLACK, new Point(1,0));
        IPenguin p4= new Penguin(Color.BLACK, new Point(0,0));

        IPenguin p5= new Penguin(Color.WHITE, new Point(2,3));
        IPenguin p6= new Penguin(Color.WHITE, new Point(2,2));
        IPenguin p7= new Penguin(Color.WHITE, new Point(2,6));
        IPenguin p8= new Penguin(Color.WHITE, new Point(2,0));


        this.penguins1 = Arrays.asList(p1, p2, p3, p4);
        this.penguins2 = Arrays.asList(p5,p6,p7,p8);

        this.player1 = new Player(Color.BLACK, 15, this.penguins1);
        this.player2 = new Player(Color.WHITE, 15, this.penguins2);

        this.players = Arrays.asList(player1, player2);

        this.gameState = new GameState(8,8, new ArrayList<>(), 0, 2, this.players);

        this.pass = new Move(this.player1, this.player1.getPenguins().get(0),new Point(0,0), true);

        this.moveUp = new Move(this.player2, this.player2.getPenguins().get(2), new Point(2, 4), false);
    }

    //TODO write some more tests for this that show Actions enforce rules;
    @Test
    public void apply() {
        init();

        this.pass.apply(this.gameState);

        this.moveUp.apply(this.gameState);
    }
}
