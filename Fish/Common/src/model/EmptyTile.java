package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * An Empty Tile is a Tile with no fish on it.
 * It doesn't draw anything, because an EmptyTile is a hole on the board.
 */
public class EmptyTile implements Tile {

    private Point position; // position of the tile relative to the board coordinates

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
    public Point getCenter() {
        return null;
    }

    @Override
    public Polygon getVisualHexagon() {
        return null;
    }

    @Override
    public List<Shape> getVisualFish() {
        return new ArrayList<>();
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
