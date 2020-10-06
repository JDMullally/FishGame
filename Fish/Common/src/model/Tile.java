package model;

import java.awt.Point;
import java.awt.Polygon;

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
     * Returns the center position of the Tile relative to it's visual representation.
     * @return Point
     */
    Point getCenter();

    /**
     * Returns a graphical representation of the tile.
     * @return Polygon
     */
    Polygon getGraphicalTile();

    /**
     * Returns a clone of the Tile.
     * @return Tile
     */
    Tile clone();
}
