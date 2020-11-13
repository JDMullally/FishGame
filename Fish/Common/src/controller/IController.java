package controller;

import java.awt.event.MouseListener;

import model.state.IGameState;
import view.IView;

/**
 * Represents the model for a controller, which handles input and distributes responsibility to
 * it's model and view to effectively represent a GameBoard.
 */
public interface IController extends MouseListener {

    /**
     * Makes the GameState visible and clickable
     *
     * @param model IGameState
     * @param view IView
     * @throws IllegalArgumentException if the model or view is null
     */
    void control(IGameState model, IView view) throws IllegalArgumentException;
}
