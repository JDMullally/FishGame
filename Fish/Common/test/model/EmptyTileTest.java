package model;

import static org.junit.Assert.*;

import java.awt.Point;
import org.junit.Test;

public class EmptyTileTest {

    private EmptyTile etile;
    public void init() {
        this.etile = new EmptyTile(new Point(1,1));
    }

    @Test
    public void isEmpty() {
        this.init();

        assertEquals(true, this.etile.isEmpty());
    }

    @Test
    public void getFish() {
        this.init();

        assertEquals(0, this.etile.getFish());
    }

    @Test
    public void getPosition() {
        this.init();
        assertEquals(new Point(1,1), this.etile.getPosition());
    }

    @Test
    public void testClone() {
        this.init();

        Tile etile2 = this.etile.clone();
        assertEquals(this.etile, etile2);

    }
}
