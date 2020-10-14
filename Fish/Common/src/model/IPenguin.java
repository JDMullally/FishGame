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
     * Moves Penguin to given Tile, eats the fish at that Tile
     * and returns the Penguin's new Position.
     * @param tile where Penguin is moving to.
     * @return Point where the Penguin now resides.
     */
    Point movePenguin(Tile tile);

    /**
     * Returns the Penguin's team as a Color.
     * @return Color that represents the Penguin's team.
     */
    Color getTeam();

    /**
     * Returns a score representing how many fish the penguin has eaten.
     * @return int that represents the number of fish that penguin has eaten.
     */
    int getScore();

    /**
     * Returns true if the input team color is the same as this Penguin's team color
     * @param team Color of a team.
     * @return boolean if given color is the same as the team color.
     */
    boolean checkTeam(Color team);

    /**
     * Returns a Shape that looks like a penguin.
     * @return Shape that represents a Penguin.
     */
    Shape drawPenguin();
}
