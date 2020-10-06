package controller;

import java.awt.event.MouseListener;
import java.awt.event.TextListener;

import javax.swing.*;

import model.IGameBoard;
import view.IView;

/**
 * Represents the model for a controller, which handles input and distributes responsibility to
 * it's model and view to effectively represent a GameBoard.
 */
public interface IController extends MouseListener {

    /**
     * Makes the IGameBoard visible and clickable
     *
     * @param model
     * @param view
     * @throws IllegalArgumentException if the model or view is null
     */
    void control(IGameBoard model, IView view) throws IllegalArgumentException;
}
