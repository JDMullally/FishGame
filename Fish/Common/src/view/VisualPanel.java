package view;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import model.state.IPenguin;
import model.state.IPlayer;
import model.state.ImmutableGameStateModel;
import model.state.Penguin;
import model.board.Tile;

public class VisualPanel extends JPanel {

    private ImmutableGameStateModel immutableModel; // Immutable GameState
    private List<Tile> targets; // current viable paths
    private Tile origin;

    /**
     * Default constructor takes in an immutable model.
     *
     * @param immutableModel the immutable model
     * @throws IllegalArgumentException if the model is null
     */
    public VisualPanel(ImmutableGameStateModel immutableModel) throws IllegalArgumentException {
        if (immutableModel == null) {
            throw new IllegalArgumentException("Board can't be null.");
        }

        this.immutableModel = immutableModel;
        this.targets = new ArrayList<>();

        this.setBackground(new Color(150,150,150));
        this.setBorder(BorderFactory.createMatteBorder(1,1,1,1, Color.BLACK));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // draws hexagons and fish
        for (Tile[] row : this.immutableModel.getGameBoard()) {
            for (Tile tile: row) {
                // draws hexagon
                Polygon hexagon = tile.getVisualHexagon();
                if (!tile.isEmpty()) {
                    hexagon.translate(1, 1);
                    g2d.setColor(new Color(252, 157, 3));
                    g2d.fill(hexagon);
                    g2d.setColor(new Color(255, 255, 255));
                    g2d.draw(hexagon);
                }

                g2d.setColor(new Color(0, 28, 150));
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // draws fish
                List<Shape> fishList = tile.getVisualFish();
                for (Shape fish : fishList) {
                    g2d.fill(fish);
                }
            }
        }

        // draws penguins
        for (IPlayer players : this.immutableModel.getPlayers()) {
            for (IPenguin penguin : players.getPenguins()) {
                g2d.setColor(penguin.getColor());
                g2d.fill(penguin.drawPenguin());
            }
        }

        // draws viable paths
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(2));
            for (Tile target: this.targets) {
                Point tileCenter1 = origin.getCenter();
                Point tileCenter2 = target.getCenter();
                g2d.drawLine(tileCenter1.x, tileCenter1.y, tileCenter2.x, tileCenter2.y);
            }
    }

    /**
     * Updates the model and repaints it
     *
     * @param immutableModel ImmutableGameStateModel
     * @param targets potential paths
     * @param origin origin Tile where all lines are drawn
     */
    void update(ImmutableGameStateModel immutableModel, List<Tile> targets, Tile origin){
        this.immutableModel = immutableModel;
        this.targets = targets;
        this.origin = origin;
        this.repaint();
    }

    /**
     * Repaints the board
     */
    void refresh() {
        this.repaint();
    }

}
