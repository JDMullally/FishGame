package model;

import static constants.Constants.HEX_SIZE;
import static org.junit.Assert.*;

import java.awt.*;
import java.util.ArrayList;
import org.junit.Test;

public class FishTileTest {

    private int fish;
    private Tile tile;
    private Tile tile2;

    private void init() {
        this.fish = 5;
        this.tile = new FishTile(new Point(1,1), fish);
        this.tile2 = new FishTile(new Point(2,2), fish/2);
    }

    /**
     * Returns true if the points of the two polygons are equal
     * @param p1 Polygon
     * @param p2 Polygon
     * @return boolean
     */
    private boolean checkPoints(Polygon p1, Polygon p2) {
        if (p1.npoints == p2.npoints) {
            for (int i = 0; i < p1.npoints; i++) {
                if (p1.xpoints[i] != p2.xpoints[i] &&
                    p1.ypoints[i] != p2.ypoints[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Test
    public void isEmpty() {
        this.init();

        assertEquals(false, this.tile.isEmpty());
    }

    @Test
    public void getFish() {
        this.init();

        assertEquals(this.fish, this.tile.getFish());
    }

    @Test
    public void getPosition() {
        this.init();

        assertEquals(new Point(1,1), this.tile.getPosition());
    }

    @Test
    public void testGenerateHexagon1() {
        this.init();

        //create buffers for hexagon
        int oddBuffer = (2 * HEX_SIZE);
        int xBuffer = (4 * this.tile.getPosition().x * HEX_SIZE) + oddBuffer;
        int yBuffer = (HEX_SIZE * this.tile.getPosition().y);

        //create a polygon to match the getVisualHexagon method
        Polygon expected = new Polygon();
        expected.addPoint(xBuffer, yBuffer + (HEX_SIZE));
        expected.addPoint(xBuffer + (HEX_SIZE), yBuffer);
        expected.addPoint(xBuffer + (HEX_SIZE * 2), yBuffer);
        expected.addPoint(xBuffer + (HEX_SIZE * 3), yBuffer + (HEX_SIZE));
        expected.addPoint(xBuffer + (HEX_SIZE * 2), yBuffer + (HEX_SIZE * 2));
        expected.addPoint(xBuffer + (HEX_SIZE), yBuffer + (HEX_SIZE * 2));

        Polygon actual = this.tile.getVisualHexagon();

        //compares the points of the hexagons
        boolean samePoints = checkPoints(actual, expected);

        assertTrue(samePoints);
    }

    @Test
    public void testGenerateHexagon2() {
        this.init();

        //create buffers for hexagon
        int xBuffer = (4 * this.tile2.getPosition().x * HEX_SIZE);
        int yBuffer = (HEX_SIZE * this.tile2.getPosition().y);

        //create a polygon to match the getVisualHexagon method
        Polygon expected = new Polygon();
        expected.addPoint(xBuffer, (HEX_SIZE) + yBuffer);
        expected.addPoint(xBuffer + (HEX_SIZE), yBuffer);
        expected.addPoint(xBuffer + (HEX_SIZE * 2), yBuffer);
        expected.addPoint(xBuffer + (HEX_SIZE * 3), yBuffer + (HEX_SIZE));
        expected.addPoint(xBuffer + (HEX_SIZE * 2), yBuffer + (HEX_SIZE * 2));
        expected.addPoint(xBuffer + (HEX_SIZE), yBuffer + (HEX_SIZE * 2));

        Polygon actual = this.tile2.getVisualHexagon();

        //compares the points of the hexagons
        boolean samePoints = checkPoints(actual, expected);

        assertTrue(samePoints);
    }

    @Test
    public void getCenter() {
        init();

        Point actualCenter = this.tile.getCenter();

        Point expected = new Point( (int) ((HEX_SIZE * this.tile.getPosition().x * 3)
            + HEX_SIZE * (this.tile.getPosition().x + 2)
            + HEX_SIZE * 1.5) + 1,
            (HEX_SIZE * this.tile.getPosition().y)+ HEX_SIZE);

        assertEquals(expected, actualCenter);
    }

    @Test
    public void testGetVisualFish1() {
        init();

        int actualLength = this.tile.getVisualFish().size();

        assertEquals(this.fish, actualLength);
    }

    @Test
    public void testGetVisualFish2() {
        init();

        ArrayList<Shape> fish = new ArrayList<>(this.tile.getVisualFish());

        Shape middleFish = fish.get(2);

        assertTrue(middleFish.contains(this.tile.getCenter()));
    }

    @Test
    public void testClone() {
        this.init();

        Tile copy = this.tile.clone();
        assertEquals(copy, this.tile);
    }
}
