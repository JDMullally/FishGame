package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import model.Tile;

public class VisualPanel extends JPanel {

    private Tile[][] board;

    /**
     * Default constructor takes in an immutable model.
     *
     * @param board the animation model
     * @throws IllegalArgumentException if the model is null
     */
    public VisualPanel(Tile[][] board) throws IllegalArgumentException {
        if (board == null) {
            throw new IllegalArgumentException("Board can't be null.");
        }

        this.board = board;

        this.setBackground(new Color(150,150,150));
        this.setBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.BLACK));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        for (Tile[] row : this.board) {
            for (Tile tile: row) {
                Polygon hexagon = tile.getGraphicalTile();
                hexagon.translate(1, 1);
                if (hexagon != null) {
                    g2d.setColor(new Color(252, 157, 3));
                    g2d.fill(hexagon);
                    g2d.setColor(new Color(255, 255, 255));
                    g2d.draw(hexagon);
                }
            }
        }
    }

    /**
     * Updates the board and repaints it
     * @param board the game board
     */
    void update(Tile[][] board) {
        this.board = board;
        this.repaint();
    }

    /**
     * Repaints the board
     */
    void refresh() {
        this.repaint();
    }

}
