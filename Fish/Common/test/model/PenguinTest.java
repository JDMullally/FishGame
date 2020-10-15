package model;


import java.awt.Color;
import java.awt.Point;
import org.junit.Test;

import static constants.Constants.HEX_SIZE;
import static org.junit.Assert.*;

/**
 * Tests all public methods for the Penguin class
 */
public class PenguinTest {
    IPenguin testPenguin;
    Point position;

    private void init() {
        this.position = new Point(0,0);
        this.testPenguin = new Penguin(Color.CYAN, this.position);
    }

    /**
     * Tests for getPosition
     */
    @Test
    public void getPosition() {
        init();

        assertEquals(new Point(0,0), this.testPenguin.getPosition());
    }

    /**
     * Tests for getColor
     */
    @Test
    public void getColor() {
        init();

        assertEquals(Color.CYAN, this.testPenguin.getColor());
    }

    /**
     * Tests for getScore
     */
    @Test
    public void getScore() {
        init();

        assertEquals(0, this.testPenguin.getScore());
    }

    /**
     * Tests for addScore
     */
    @Test
    public void addScore() {
        init();
        int fish = 5;

        assertEquals(0, this.testPenguin.getScore());

        this.testPenguin.addScore(fish);

        assertEquals(fish, this.testPenguin.getScore());
    }

    /**
     * Tests for move
     */
    @Test
    public void move() {
        init();

        assertEquals(new Point(0,0), this.testPenguin.getPosition());

        this.testPenguin.move(new Point(1,1));

        assertEquals(new Point(1,1), this.testPenguin.getPosition());
    }

    /**
     * Tests for drawPenguin
     */
    @Test
    public void drawPenguin() {
        init();


        int xBuffer = this.position.x * HEX_SIZE;
        if (this.position.y % 2 == 1) {
            xBuffer = xBuffer + 2 * HEX_SIZE;
        }

        Point center = new Point((int) ((HEX_SIZE * this.position.x * 3)
            + xBuffer + HEX_SIZE * 1.5) + 1, (HEX_SIZE * this.position.y)+ HEX_SIZE);

        assertTrue(this.testPenguin.drawPenguin().contains(center.x, center.y));
    }
}
