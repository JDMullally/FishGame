package model;

import java.awt.Point;

public class FishTile implements Tile {

    private Point position;
    private int fish;

    /**
     * Constructor that takes in the position of the Tile and the number of fish it has.
     * @param position
     * @param fish
     */
    public FishTile(Point position, int fish) {
        this.position = new Point(position);
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


}
