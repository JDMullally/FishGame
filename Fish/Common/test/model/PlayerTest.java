package model;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

import model.state.IPenguin;
import model.state.IPlayer;
import model.state.Penguin;
import model.state.Player;

import static org.junit.Assert.*;

/**
 * Tests all public methods for the Player class.
 */
public class PlayerTest {

    private IPlayer player;
    private IPlayer penguinlessPlayer;
    private List<IPenguin> penguins;

    private void init() {
        this.penguins = new ArrayList<>();
        this.penguins.add(new Penguin(Color.cyan, new Point(1,1)));
        this.penguins.add(new Penguin(Color.cyan, new Point(2,2)));
        this.penguins.add(new Penguin(Color.cyan, new Point(3,3)));
        this.penguins.add(new Penguin(Color.cyan, new Point(4,4)));
        this.penguins.add(new Penguin(Color.cyan, new Point(5,5)));

        this.player = new Player(Color.cyan, 5, this.penguins);

        this.penguinlessPlayer = new Player(Color.BLACK, 3, new ArrayList<>());
    }

    /**
     * Tests for getColor
     */
    @Test
    public void getColor() {
        this.init();
        //System.out.println(this.player.getColor().toString());

       assertEquals(Color.CYAN,this.player.getColor());
    }

    /**
     * Tests for getAge
     */
    @Test
    public void getAge() {
        this.init();

        assertEquals(5, this.player.getAge());
    }

    /**
     * Tests for getPenguins. getPenguins should only get a copy of the list.
     */
    @Test
    public void getPenguins() {
        this.init();
        boolean gotPenguins = this.penguins.equals(this.player.getPenguins());
        assertTrue(gotPenguins);
    }

    /**
     * Tests that the list of penguins is not mutated outside of the class.
     */
    @Test
    public void getPenguinsNoMutate() {
        this.init();
        List<IPenguin> penguins = this.player.getPenguins();;

        this.player.addPenguin(new Penguin(Color.BLACK, new Point(1,1)));

        boolean gotPenguins = penguins.equals(this.player.getPenguins());

        assertFalse(gotPenguins);
    }

    /**
     * Tests for addPenguin.  Add penguin should mutate the the penguin list.
     */
    @Test
    public void addPenguin() {
        this.penguinlessPlayer = new Player(Color.BLACK, 3, new ArrayList<>());
        assertEquals(new ArrayList<>(), this.penguinlessPlayer.getPenguins());

        penguinlessPlayer.addPenguin(new Penguin(Color.BLACK, new Point(1,1)));

        assertEquals(new Penguin(Color.BLACK, new Point(1,1)), penguinlessPlayer.getPenguins().get(0));
    }

    /**
     * Tests for getScore.  getScore should only get the score of the penguin.
     */
    @Test
    public void getScore() {
        init();

        assertEquals(0, this.player.getScore());
    }

    /**
     * Tests for addScore.  addScore should mutate the score of the player.
     */
    @Test
    public void addScore() {
        init();

        assertEquals(0, this.player.getScore());

        this.player.addScore(5);

        assertEquals(5, this.player.getScore());
    }
}
