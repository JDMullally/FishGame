package view;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import model.Tile;

public class VisualPanel extends JPanel {

    private Tile[][] board;
    private List<List<Tile>> viablePaths;

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
        this.viablePaths = new ArrayList<>();

        this.setBackground(new Color(150,150,150));
        this.setBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.BLACK));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // draws hexagons
        for (Tile[] row : this.board) {
            for (Tile tile: row) {
                Polygon hexagon = tile.getVisualHexagon();
                hexagon.translate(1, 1);
                if (hexagon != null) {
                    g2d.setColor(new Color(252, 157, 3));
                    g2d.fill(hexagon);
                    g2d.setColor(new Color(255, 255, 255));
                    g2d.draw(hexagon);
                }
            }
        }

        // draws viable paths
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(2));
        for (List<Tile> paths : this.viablePaths) {
            for (int i = 1; i < paths.size(); i++) {
                Point tileCenter1 = paths.get(i - 1).getCenter();
                Point tileCenter2 = paths.get(i).getCenter();
                g2d.drawLine(tileCenter1.x, tileCenter1.y, tileCenter2.x, tileCenter2.y);
            }
        }
    }

    /**
     * Updates the board and repaints it
     * @param board the game board
     * @param viablePaths viable paths to move on the board
     */
    void update(Tile[][] board, List<List<Tile>> viablePaths) {
        this.board = board;
        this.viablePaths = viablePaths;
        this.repaint();
    }

    /**
     * Repaints the board
     */
    void refresh() {
        this.repaint();
    }

}
