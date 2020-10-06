package controller;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.*;

import model.EmptyTile;
import model.IGameBoard;
import model.Tile;
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
        Point point = new Point(e.getX(), e.getY());

        // checks if the point is inside a polygon
        Tile[][] gameBoard = this.model.getGameBoard();
        for (Tile[] tiles : gameBoard) {
            for (Tile tile : tiles) {
                Polygon hexagon = tile.getVisualHexagon();
                if (hexagon != null && hexagon.contains(point)) {

                    // left click shows viable paths
                    if (e.getButton() == 1) {
                        this.view.update(gameBoard, this.model.getViablePaths(tile.getPosition()));
                    }
                    // right click removes the tile
                    if (e.getButton() == 3) {
                        this.model.replaceTile(new EmptyTile(tile.getPosition()));
                        this.view.update(this.model.getGameBoard(), new ArrayList<>());
                    }

                    return;
                }
            }
        }

        this.view.update(gameBoard, new ArrayList<>());
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
