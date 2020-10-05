package view;

import java.awt.*;

import javax.swing.*;

import controller.IController;
import model.Canvas;
import model.Tile;

public class VisualView extends JFrame implements IView {

    private VisualPanel panel;

    public VisualView(Tile[][] board, Canvas canvas) {
        if (board == null) {
            throw new IllegalArgumentException("Board can't be null.");
        }

        this.panel = new VisualPanel(board);
        this.panel.setPreferredSize(new Dimension(canvas.getWidth(), canvas.getHeight()));

        this.setTitle("Fish");
        this.setLayout(new BorderLayout());
        this.setSize(canvas.getWidth(), canvas.getHeight());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(new JScrollPane(this.panel));
        this.pack();
    }

    @Override
    public void makeVisible() {
        this.setVisible(true);
    }

    @Override
    public void start(Timer timer) {
        timer.addActionListener(this.panel);
        timer.start();
    }

    @Override
    public void setListener(IController listener) {
        this.addMouseListener(listener);
    }
}
