package model;

import static org.junit.Assert.*;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class EmptyTileTest {

    private EmptyTile etile;

    private void init() {
        this.etile = new EmptyTile(new Point(1, 1));
    }

    @Test
    public void isEmpty() {
        this.init();

        assertTrue(this.etile.isEmpty());
    }

    @Test
    public void getFish() {
        this.init();

        assertEquals(0, this.etile.getFish());
    }

    @Test
    public void getPosition() {
        this.init();
        assertEquals(new Point(1, 1), this.etile.getPosition());
    }

    @Test
    public void testGetVisualHexagon() {
        this.init();

        Polygon actual = this.etile.getVisualHexagon();

        assertNull(actual);
    }

    @Test
    public void testGetCenter() {
        this.init();

        Point actual = this.etile.getCenter();

        assertNull(actual);
    }

    @Test
    public void getVisualFish() {
        this.init();

        List<Shape> actual = this.etile.getVisualFish();
        List<Shape> expected = new ArrayList<>();

        assertEquals(expected, actual);
    }

    @Test
    public void testClone() {
        this.init();

        Tile etile2 = this.etile.clone();
        assertEquals(this.etile, etile2);

    }
}
