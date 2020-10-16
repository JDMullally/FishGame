package model;

import java.awt.*;
import java.util.List;

/**
 * Represents a Tile on the GameBoard.
 * A Tile should know if it is empty, get it's fish, position, graphical center,
 * graphical shape and clone itself.
 */
public interface Tile {

    /**
     * Returns true or false whether a tile is empty or has fish.
     *
     * @return boolean
     */
    boolean isEmpty();

    /**
     * Returns how many fish are on a given tile. An EmptyTile will have 0 fish.
     *
     * @return int
     */
    int getFish();

    /**
     * Returns the position of the tile on the GameBoard.
     *
     * @return Point
     */
    Point getPosition();

    /**
     * Returns the center position of the Tile relative to it's visual representation.
     *
     * @return Point
     */
    Point getCenter();

    /**
     * Returns a graphical representation of the tile as a hexagon.
     *
     * @return Polygon
     */
    Polygon getVisualHexagon();

    /**
     * Returns a graphical representation of the fish on the tile.
     *
     * @return List<Shape>
     */
    List<Shape> getVisualFish();

    /**
     * Returns a clone of the Tile.
     *
     * @return Tile
     */
    Tile clone();
}
