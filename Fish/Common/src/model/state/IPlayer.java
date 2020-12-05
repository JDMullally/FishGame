package model.state;

import java.awt.Color;
import java.util.List;

/**
 * Interface for Player class that allows them to interact with the GameState.
 * An IPlayer should be able to display it's age, color and Penguins.
 * It should also be able to add Penguins to it's current list of Penguins.
 */
public interface IPlayer {

    /**
     * Gets the IPlayer's color
     *
     * @return Color
     */
    Color getColor();

    /**
     * Gets the IPlayer's age
     *
     * @return int
     */
    int getAge();

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
     * Gets the IPlayer's List of Penguins.
     *
     * @return List of IPenguin
     */
    List<IPenguin> getPenguins();

    /**
     * Adds an IPenguin to the Player's List of IPenguins.
     *
     * @param penguin IPenguin
     */
    void addPenguin(IPenguin penguin);

    /**
     * Returns a clone of the Iplayer.
     *
     * @return IPlayer
     */
    IPlayer clone();
}
