package view;

import java.awt.*;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.Map.Entry;
import javax.swing.*;

import model.games.IGameResult;
import model.state.IPenguin;
import model.state.IPlayer;
import model.state.ImmutableGameStateModel;
import model.state.Penguin;
import model.board.Tile;
import model.state.Player;
import model.tree.PlayerInterface;

public class VisualPanel extends JPanel {

    public static final int FONTSIZE = 20;
    private ImmutableGameStateModel immutableModel; // Immutable GameState
    private IGameResult gameResults;

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
        //this.targets = new ArrayList<>();

        this.setBackground(new Color(150, 150, 150));
        this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // draws hexagons, fish and penguins
        if (this.gameResults == null) {
            renderBoard(g2d);
            renderPenguins(g2d);
        } else {
            renderGameResults(g2d);
        }
    }

    /**
     * Rends the Penguins for a game of fish
     *
     * @param g2d Graphics2D
     */
    private void renderPenguins(Graphics2D g2d) {
        for (IPlayer players : this.immutableModel.getPlayers()) {
            for (IPenguin penguin : players.getPenguins()) {
                g2d.setColor(penguin.getColor());
                g2d.fill(penguin.drawPenguin());
            }
        }
    }

    /**
     * Renders the GameBoard for a game of Fish
     *
     * @param g2d Graphics2D
     */
    private void renderBoard(Graphics2D g2d) {
        for (Tile[] row : this.immutableModel.getGameBoard()) {
            for (Tile tile : row) {
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
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

                // draws fish
                List<Shape> fishList = tile.getVisualFish();
                for (Shape fish : fishList) {
                    g2d.fill(fish);
                }
            }
        }
    }

    /**
     * Renders the results of a Game of Fish
     *
     * @param g2d Graphics2D
     */
    private void renderGameResults(Graphics2D g2d) {
        List<PlayerInterface> winners = gameResults.getWinners();
        Map<PlayerInterface, Integer> map = new HashMap<>();
        for (PlayerInterface winner : winners) {
            map.put(winner, this.gameResults.getPlayerScore(winner));
        }
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.black);
        g2d.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, FONTSIZE));

        renderWinners(g2d, winners, map);
    }

    /**
     * Rends the winners in a Game of Fish
     * @param g2d Graphics2D
     * @param winners List of PlayerInterface
     * @param map Map PlayerInterface (player) -> Integer (score)
     */
    private void renderWinners(Graphics2D g2d, List<PlayerInterface> winners,
        Map<PlayerInterface, Integer> map) {
        if (winners.size() > 1) {
            int currentIndent = 4;
            g2d.drawString("Winners",
                immutableModel.getCanvas().getWidth() / 3,
                immutableModel.getCanvas().getHeight() - currentIndent * FONTSIZE);
            for (Entry<PlayerInterface, Integer> winner : map.entrySet()) {
                currentIndent--;
                g2d.drawString(
                    buildString(winner),
                    immutableModel.getCanvas().getWidth() / 3,
                    immutableModel.getCanvas().getHeight()
                        - currentIndent * FONTSIZE);
            }
        } else {
            g2d.drawString("Winner",
                immutableModel.getCanvas().getWidth() / 3,
                immutableModel.getCanvas().getHeight() / 2 - FONTSIZE);
            for (Entry<PlayerInterface, Integer> winner : map.entrySet()) {
                g2d.drawString(
                    buildString(winner),
                    immutableModel.getCanvas().getWidth() / 3,
                    immutableModel.getCanvas().getHeight() / 2);
            }
        }
    }

    String buildString(Entry<PlayerInterface, Integer> player) {
        StringBuilder sb = new StringBuilder();
        sb.append(player.getKey().getPlayerID());
        sb.append(": [Score: ");
        sb.append(player.getValue());
        sb.append("]");
        sb.append("\n");
        return sb.toString();
    }

    /**
     * Updates the model and repaints it
     *
     * @param immutableModel ImmutableGameStateModel
     */
    void update(ImmutableGameStateModel immutableModel) {
        this.immutableModel = immutableModel;
        this.gameResults = null;
        this.repaint();
    }

    void update(ImmutableGameStateModel immutableModel, IGameResult result) {
        this.immutableModel = immutableModel;
        this.gameResults = result;
        this.repaint();
    }

    /**
     * Repaints the board
     */
    void refresh() {
        this.repaint();
    }

}
