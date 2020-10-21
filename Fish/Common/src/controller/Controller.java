package controller;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.*;

import model.board.EmptyTile;
import model.state.GameState;
import model.state.ImmutableGameState;
import model.board.Tile;
import view.IView;

/**
 * Represents the controller for the model and view of a Fish Game, distributes
 * responsibility and handles user input.
 */
public class Controller implements IController {

    private GameState model; // Fish Game model
    private IView view; // Fish Game view

    private Timer timer = new Timer(0, null); // zero delay timer

    @Override
    public void control(GameState model, IView view) throws IllegalArgumentException {
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
        Point point = new Point(e.getX(), e.getY());

        // checks if the point is inside a polygon
        Tile[][] gameBoard = this.model.getGameBoard();
        for (Tile[] tiles : gameBoard) {
            for (Tile tile : tiles) {
                Polygon hexagon = tile.getVisualHexagon();
                if (!tile.isEmpty() && hexagon.contains(point)) {

                    // left click shows viable paths
                    if (e.getButton() == 1) {
                        this.view.update(new ImmutableGameState(this.model), this.model.getViablePaths(tile.getPosition()));
                    }
                    // right click removes the tile
                    if (e.getButton() == 3) {
                        this.model.replaceTile(new EmptyTile(tile.getPosition()));
                        this.view.update(new ImmutableGameState(this.model), new ArrayList<>());
                    }

                    return;
                }
            }
        }

        this.view.update(new ImmutableGameState(this.model), new ArrayList<>());
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
