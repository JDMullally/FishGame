package model.state;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;

import model.board.Tile;

/**
 * Interface for the Penguin class.  A Penguin should be able to move, eat fish,
 * know how many fish it has eaten, know it's location as a Point and know what team it is on.
 */
public interface IPenguin {

    /**
     * Returns the position of the Penguin on the GameBoard.
     *
     * @return Point
     */
    Point getPosition();

    /**
     * Returns the Penguin's team as a Color.
     *
     * @return Color that represents the Penguin's team.
     */
    Color getColor();

    /**
     * Moves the penguin's position to position of the given Tile.
     *
     * @param tile tile
     */
    void move(Tile tile);

    /**
     * Moves the penguin's position to the new point.
     *
     * @param point Point
     */
    void move(Point point);

    /**
     * Returns a Shape that looks like a penguin.
     *
     * @return Shape that represents a Penguin.
     */
    Shape drawPenguin();

    /**
     * Returns a clone of the IPenguin.
     *
     * @return IPenguin
     */
    IPenguin clone();
}
