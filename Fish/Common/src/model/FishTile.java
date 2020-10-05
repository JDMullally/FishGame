package model;

import java.awt.Point;
import java.awt.Polygon;

import static constants.Constants.HEX_SIZE;

public class FishTile implements Tile {

    private Point position; // position of the tile
    private int fish; // number of fish on the tile

    /**
     * Constructor that takes in the position of the Tile and the number of fish it has.
     * @param position
     * @param fish
     */
    public FishTile(Point position, int fish) {
        this.position = new Point(position);
        this.fish = fish;
    }

    /**
     * Constructor that takes in the position as an x and y coordinate of the
     * Tile and the number of fish it has.
     * @param x
     * @param y
     * @param fish
     */
    public FishTile(int x, int y, int fish) {
        this.position = new Point(x, y);
        this.fish = fish;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int getFish() {
        return this.fish;
    }

    @Override
    public Point getPosition() {
        return new Point(this.position);
    }

    @Override
    public Polygon getGraphicalTile() {
        Polygon hexagon = new Polygon();
        int yBuffer = HEX_SIZE;
        int xBuffer = HEX_SIZE;
        if (this.position.y % 2 == 0) {
            yBuffer = 0;

        }
        hexagon.addPoint((HEX_SIZE * this.position.x),
            (yBuffer * this.position.y) + HEX_SIZE);
        hexagon.addPoint((HEX_SIZE * this.position.x) + HEX_SIZE,
            (yBuffer * this.position.y));
        hexagon.addPoint((HEX_SIZE * this.position.x) + (2 * HEX_SIZE),
            (yBuffer * this.position.y) + yBuffer);
        hexagon.addPoint((HEX_SIZE * this.position.x)+ (3 * HEX_SIZE),
            (yBuffer * this.position.y) + yBuffer + HEX_SIZE);
        hexagon.addPoint((HEX_SIZE * this.position.x) + (2 * HEX_SIZE),
            (yBuffer * this.position.y) + yBuffer + (2 * HEX_SIZE));
        hexagon.addPoint((HEX_SIZE * this.position.x) + HEX_SIZE,
            (yBuffer * this.position.y) + yBuffer + (2 * HEX_SIZE));

        return hexagon;
    }

    @Override
    public Tile clone() {
        return new FishTile(new Point(this.position), this.fish);
    }

    @Override
    public String toString() {
        return "(" + this.getPosition() + ", Fish: " + this.getFish() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FishTile) {
            FishTile other = (FishTile) o;
            return this.position.equals(other.position) && this.fish == other.fish;
        }
        return false;
    }



}
