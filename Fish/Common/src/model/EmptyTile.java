package model;

import java.awt.Point;
import java.awt.Polygon;

public class EmptyTile implements Tile {

    private Point position; // position of the tile

    /**
     * Constructor that takes in the position of the Tile.
     * @param position
     */
    public EmptyTile(Point position) {
        this.position = new Point(position);
    }

    /**
     * Constructor that takes in the position as an x and y coordinate of the Tile.
     * @param x
     * @param y
     */
    public EmptyTile(int x, int y) {
        this.position = new Point(x, y);
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public int getFish() {
        return 0;
    }

    @Override
    public Point getPosition() {
        return new Point(this.position);
    }

    @Override
    public Polygon getGraphicalTile() {
        return null;
    }

    @Override
    public Tile clone() {
        return new EmptyTile(new Point(this.position));
    }

    @Override
    public String toString() {
        return "(" + this.getPosition() + ", Fish: " + this.getFish() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof EmptyTile) {
            EmptyTile other = (EmptyTile) o;
            return this.position.equals(other.position);
        }
        return false;
    }
}
