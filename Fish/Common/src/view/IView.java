package view;

import javax.swing.*;

import controller.IController;
import model.Tile;

public interface IView {

    /**
     * Makes the view visible.
     */
    void makeVisible();

    /**
     * Starts a timer for the game
     *
     * @param timer the given timer
     */
    void start(Timer timer);

    /**
     * Sets the view's listener to this listener.
     *
     * @param listener the given listener
     */
    void setListener(IController listener);

    /**
     * Updates the view with the new game board and redraws the board
     * @param board the game board
     */
    void update(Tile[][] board);
}
