package model;

import java.awt.Point;

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
    public Tile clone() {
        return new FishTile(new Point(this.position), this.fish);
    }

}
