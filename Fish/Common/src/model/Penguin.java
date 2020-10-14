package model;


import static constants.Constants.HEX_SIZE;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import javafx.scene.shape.Circle;

/**
 * Penguins are represented with their position on the GameBoard as a Point,
 * the number of fish they have eaten as a score, and the color of their team.
 * A Penguin can be moved, draw itself, confirm it's team
 */
public class Penguin {

    private Point position;
    private int score;
    private Color team;

    /**
     * Constructor
     * @param team Team penguin is a part of.
     * @param position Position of the Penguin
     */
    public Penguin(Color team, Point position) {
        this.score = 0;
        this.team = team;
        this.position = position;
    }

    /**
     * Moves Penguin to given Tile, eats the fish at that Tile
     * and returns the Penguin's new Position.
     * @param tile where Penguin is moving to.
     * @return Point where the Penguin now resides.
     */
    public Point movePenguin(Tile tile) {
        this.position = tile.getPosition().getLocation();
        this.score += tile.getFish();
        return this.position.getLocation();
    }

    public Color getTeam() {
        return this.team;
    }

    /**
     * Calculates the center position of the Penguin relative to it's visual representation.
     * @return Point
     */
    private Point calculateCenter() {
        int xBuffer = this.position.x * HEX_SIZE;
        if (this.position.y % 2 == 1) {
            xBuffer = xBuffer + 2 * HEX_SIZE;
        }

        return new Point((int) ((HEX_SIZE * this.position.x * 3) + xBuffer + HEX_SIZE * 1.5) + 1, (HEX_SIZE * this.position.y)+ HEX_SIZE);
    }

    public int getScore() {
        return this.score;
    }

    /**
     * Returns true if the input team color is the same as this Penguin's team color
     * @param team Color of a team.
     * @return boolean if given color is the same as the team color.
     */
    public boolean checkTeam(Color team) {
        return this.team.equals(team);
    }

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
        //penguin.lineTo(center.x,center.y + (HEX_SIZE));
        //penguin.lineTo(center.x,center.y + (HEX_SIZE) / 2.0);
        penguin.closePath();

        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(- HEX_SIZE / 4.0, 0);
        penguin.transform(affineTransform);


        return new GeneralPath(penguin);
    }
}
