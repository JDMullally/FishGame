package model;

import java.awt.Point;

/**
 * Represents a tile on the GameBoard.
 */
public interface Tile {

    /**
     * Returns true or false whether a tile is empty or has fish.
     * @return boolean
     */
    boolean isEmpty();

    /**
     * Returns how many fish are on a given tile. An EmptyTile will have 0 fish.
     * @return int
     */
    int getFish();

    /**
     * Returns the position of the tile on the GameBoard.
     * @return Point
     */
    Point getPosition();

    /**
     * Returns a clone of the Tile.
     * @return Tile
     */
    Tile clone();
}
