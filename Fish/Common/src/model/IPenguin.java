package model;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;

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
     * Returns a score representing how many fish the penguin has eaten.
     *
     * @return int that represents the number of fish that penguin has eaten.
     */
    int getScore();

    /**
     * Adds points to the Penguins score.
     *
     * @param points number of points to add
     */
    void addScore(int points);

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
