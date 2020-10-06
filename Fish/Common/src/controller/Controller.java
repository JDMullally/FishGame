package controller;

import java.awt.event.MouseEvent;

import javax.swing.*;

import model.IGameBoard;
import view.IView;

/**
 * Represents the controller for the model and view of a Fish Game, distributes
 * responsibility and handles user input.
 */
public class Controller implements IController {

    private IGameBoard model;
    private IView view;

    private Timer timer = new Timer(0, null); // zero delay timer

    @Override
    public void control(IGameBoard model, IView view) throws IllegalArgumentException {
        if (model == null || view == null) {
            throw new IllegalArgumentException("Model and view can't be null.");
        }

        this.model = model;
        this.view = view;
        this.view.makeVisible();
        this.view.start(this.timer);
        this.view.setListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.view.update(this.model.getGameBoard());
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
