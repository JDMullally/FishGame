package controller;

import java.awt.Point;
import java.awt.Polygon;
import java.util.List;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import java.util.Map;
import javax.swing.*;

import model.state.GameState;
import model.state.IGameState;
import model.state.IPenguin;
import model.state.ImmutableGameState;
import model.board.Tile;
import view.IView;

/**
 * Represents the controller for the model and view of a Fish Game, distributes
 * responsibility and handles user input.
 */
public class Controller implements IController {

    private IGameState model; // Fish Game model
    private IView view; // Fish Game view

    private Timer timer = new Timer(0, null); // zero delay timer

    @Override
    public void control(IGameState model, IView view) throws IllegalArgumentException {
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
        List<Tile> move = new ArrayList<>();

        // checks if the point is inside a polygon
        Tile[][] gameBoard = this.model.getGameBoard();
        for (Tile[] tiles : gameBoard) {
            for (Tile tile : tiles) {
                Polygon hexagon = tile.getVisualHexagon();
                if (!tile.isEmpty() && hexagon.contains(point)) {
                    boolean tileContainsPenguin = this.model.playerTurn().getPenguins().stream().anyMatch(penguin -> penguin.getPosition().equals(tile.getPosition()));
                    if (tileContainsPenguin) {
                        for (IPenguin p : this.model.playerTurn().getPenguins()) {
                            if (p.getPosition().equals(tile.getPosition())) {
                                Map<IPenguin, List<Tile>> map = this.model.getPossibleMoves(this.model.playerTurn());
                                move = map.get(p);
                            }
                        }
                    }
                    // left click shows viable paths
                    if (e.getButton() == 1) {
                        this.view.update(new ImmutableGameState(this.model), move, tile);
                    }
                    // right click removes the tile
                    if (e.getButton() == 3) {
                        this.model.removeTile(tile.getPosition());
                        this.view.update(new ImmutableGameState(this.model), new ArrayList<>(), null);
                    }

                    return;
                }
            }
        }

        this.view.update(new ImmutableGameState(this.model), new ArrayList<>(), null);
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
