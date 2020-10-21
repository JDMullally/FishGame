package model.state;


import static constants.Constants.HEX_SIZE;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;

import model.board.Tile;

/**
 * Penguins are represented with their position on the GameBoard as a Point,
 * the number of fish they have eaten as a score, and the color of their team.
 * A Penguin can be moved, draw itself, confirm it's team
 */
public class Penguin implements IPenguin{

    private final Color color; // penguin's color
    private Point position; // its position on the board

    /**
     * Constructor
     * @param color Team penguin is a part of
     * @param position Position of the Penguin
     */
    public Penguin(Color color, Point position) {
        this.color = color;
        this.position = position;
    }

    /**
     * Calculates the center position of the Penguin relative to it's visual representation.
     *
     * @return Point
     */
    private Point calculateCenter() {
        int xBuffer = this.position.x * HEX_SIZE;
        if (this.position.y % 2 == 1) {
            xBuffer = xBuffer + 2 * HEX_SIZE;
        }

        return new Point((int) ((HEX_SIZE * this.position.x * 3) + xBuffer + HEX_SIZE * 1.5) + 1, (HEX_SIZE * this.position.y)+ HEX_SIZE);
    }

    @Override
    public Point getPosition() {
        return this.position;
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public void move(Tile tile) {
        this.move(tile.getPosition());
    }

    @Override
    public void move(Point point) {
        this.position = new Point(point);
    }

    @Override
    public Shape drawPenguin() {
        GeneralPath penguin = new GeneralPath();
        Point center = this.calculateCenter();

        penguin.moveTo(center.x - HEX_SIZE / 4.0, center.y + HEX_SIZE / 8.0);
        penguin.lineTo(center.x - HEX_SIZE / 2.0, center.y);
        penguin.lineTo(center.x - HEX_SIZE / 4.0, center.y - HEX_SIZE / 8.0);

        penguin.curveTo(center.x, center.y - (3 * HEX_SIZE) / 4.0,
            center.x + (3 * HEX_SIZE) / 4.0, center.y - (3 * HEX_SIZE) / 4.0,
            center.x + (3 * HEX_SIZE) / 4.0, center.y + HEX_SIZE / 4.0);

        penguin.lineTo(center.x + (3 * HEX_SIZE) / 4.0, center.y + (3 * HEX_SIZE) / 4.0);
        penguin.lineTo(center.x - HEX_SIZE / 4.0, center.y + (3 * HEX_SIZE) / 4.0);
        penguin.closePath();

        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(- HEX_SIZE / 4.0, 0);
        penguin.transform(affineTransform);

        return new GeneralPath(penguin);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Penguin) {
            Penguin other = (Penguin) o;
            return this.color.getRGB() == other.getColor().getRGB()
                && this.position.x == other.position.x
                && this.position.y == other.position.y;
        }
        return false;
    }

    @Override
    public IPenguin clone() {
        return new Penguin(new Color(this.color.getRGB()), new Point(this.position));
    }
}
