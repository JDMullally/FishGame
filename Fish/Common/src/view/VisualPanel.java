package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import model.Tile;

public class VisualPanel extends JPanel implements ActionListener {

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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        // TODO
    }
}
