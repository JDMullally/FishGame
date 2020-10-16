package model;

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
     * Gets the IPlayer's List of Penguins.
     *
     * @return List of IPenguin
     */
    List<IPenguin> getPenguins();

    /**
     * Adds an Ipenguin to the Player's List of IPenguins.
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
