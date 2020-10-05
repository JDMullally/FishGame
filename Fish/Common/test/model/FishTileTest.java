package model;

import static org.junit.Assert.*;

import java.awt.Point;
import org.junit.Test;

public class FishTileTest {

    int fish;
    Tile tile;
    private void init() {
        fish = 5;
        this.tile = new FishTile(new Point(1,1), fish);
    }

    @Test
    public void isEmpty() {
        this.init();

        assertEquals(false, this.tile.isEmpty());
    }

    @Test
    public void getFish() {
        this.init();

        assertEquals(fish, this.tile.getFish());
    }

    @Test
    public void getPosition() {
        this.init();

        assertEquals(new Point(1,1), this.tile.getPosition());
    }

    @Test
    public void testClone() {
        this.init();

        Tile copy = this.tile.clone();
        assertEquals(copy, this.tile);
    }
}
