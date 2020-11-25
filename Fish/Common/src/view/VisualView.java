package view;

import java.awt.Dimension;
import java.util.List;

import javax.swing.*;

import controller.IController;
import model.board.ICanvas;
import model.games.GameResult;
import model.games.IGameResult;
import model.state.ImmutableGameStateModel;
import model.board.Tile;

/**
 * Represents a visual view of the Fish Game.
 */
public class VisualView extends JFrame implements IView {

    private VisualPanel panel;

    public VisualView(ImmutableGameStateModel immutableModel) {
        if (immutableModel == null) {
            throw new IllegalArgumentException("Model can't be null.");
        }

        this.panel = new VisualPanel(immutableModel);

        ICanvas canvas = immutableModel.getCanvas();
        this.panel.setPreferredSize(new Dimension(canvas.getWidth(), canvas.getHeight()));

        JScrollPane scrollPane = new JScrollPane(this.panel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        this.setTitle("Fish Game");
        this.setSize(canvas.getWidth(), canvas.getHeight());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(scrollPane);
        this.pack();
    }

    @Override
    public void makeVisible() {
        this.setVisible(true);
    }

    @Override
    public void start(Timer timer) {
        timer.start();
        this.panel.refresh();
    }

    @Override
    public void setListener(IController listener) {
        this.panel.addMouseListener(listener);
    }

    @Override
    public void update(ImmutableGameStateModel immutableModel) {
        this.panel.update(immutableModel);
    }

    @Override
    public void update(ImmutableGameStateModel immutableModel, IGameResult result) {
        this.panel.update(immutableModel, result);
    }
}
