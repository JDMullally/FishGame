package model;

/**
 * Represents a model for the bounds of the Fish Game.
 */
public interface ICanvas {

    /**
     * Gets the x value (top).
     *
     * @return the x value
     */
    public int getX();

    /**
     * Gets the y value (left).
     *
     * @return the y value
     */
    public int getY();

    /**
     * Gets the width value.
     *
     * @return the width value
     */
    public int getWidth();

    /**
     * Gets the height value.
     *
     * @return the height value
     */
    public int getHeight();
}
