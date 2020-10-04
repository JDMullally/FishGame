package model;

import java.awt.Point;

public class EmptyTile implements Tile {
    private Point position;

    /**
     * Constructor that takes in the position of the Tile.
     * @param position
     */
    public EmptyTile(Point position) {
        this.position = new Point(position);
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
}
