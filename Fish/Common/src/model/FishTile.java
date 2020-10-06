package model;

import java.awt.Point;
import java.awt.Polygon;

import static constants.Constants.HEX_SIZE;

public class FishTile implements Tile {

    private Point position; // position of the tile relative to the board coordinates
    private int fish; // number of fish on the tile

    private Point center; // the center of the tile relative to it's visual representation
    private Polygon hexagon; // a visual representation of the tile

    /**
     * Constructor that takes in the position of the Tile and the number of fish it has.
     * @param position
     * @param fish
     */
    public FishTile(Point position, int fish) {
        this(position.x, position.y, fish);
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
        this.hexagon = this.generateHexagon();
        this.center = this.calculateCenter();
    }

    /**
     * Calculates the center position of the tile relative to it's visual representation.
     * @return Point
     */
    private Point calculateCenter() {
        int xBuffer = this.position.x * HEX_SIZE;
        if (this.position.y % 2 == 1) {
            xBuffer = xBuffer + 2 * HEX_SIZE;
        }

        return new Point((int) ((HEX_SIZE * this.position.x * 3) + xBuffer + HEX_SIZE * 1.5), (HEX_SIZE * this.position.y)+ HEX_SIZE);
    }

    /**
     * Generates a visual representation of the tile as a hexagon.
     * @return Polygon
     */
    private Polygon generateHexagon() {
        Polygon hexagon = new Polygon();

        int xBuffer = this.position.x * HEX_SIZE;
        if (this.position.y % 2 == 1) {
            xBuffer = xBuffer + 2 * HEX_SIZE;
        }

        hexagon.addPoint((HEX_SIZE * this.position.x * 3) + xBuffer, (HEX_SIZE * this.position.y)+ HEX_SIZE);
        hexagon.addPoint((HEX_SIZE * this.position.x * 3) + HEX_SIZE + xBuffer, (HEX_SIZE * this.position.y));
        hexagon.addPoint((HEX_SIZE * this.position.x * 3) + (2 * HEX_SIZE) + xBuffer, (HEX_SIZE * this.position.y));
        hexagon.addPoint((HEX_SIZE * this.position.x * 3) + (3 * HEX_SIZE) + xBuffer, (HEX_SIZE * this.position.y) + HEX_SIZE);
        hexagon.addPoint((HEX_SIZE * this.position.x * 3) + (2 * HEX_SIZE) + xBuffer, (HEX_SIZE * this.position.y) + (2 * HEX_SIZE));
        hexagon.addPoint((HEX_SIZE * this.position.x * 3) + HEX_SIZE + xBuffer, (HEX_SIZE * this.position.y) + (2 * HEX_SIZE));

        return hexagon;
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
    public Point getCenter() {
        return new Point(this.center);
    }

    @Override
    public Polygon getGraphicalTile() {
        return new Polygon(this.hexagon.xpoints, this.hexagon.ypoints, this.hexagon.npoints);
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