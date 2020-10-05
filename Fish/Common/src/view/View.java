package view;

import javax.swing.*;

import controller.ControllerModel;

public interface View {

    /**
     * Makes the view visible.
     */
    void makeVisible();

    /**
     * Starts a timer for the game
     *
     * @param timer
     */
    void start(Timer timer);

    /**
     * Sets the view's listener to this listener.
     *
     * @param listener the given listener
     */
    void setListener(ControllerModel listener);
}
