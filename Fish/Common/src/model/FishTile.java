package model;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

import static constants.Constants.HEX_SIZE;

public class FishTile implements Tile {

    private Point position; // position of the tile relative to the board coordinates
    private int fish; // number of fish on the tile

    private Point center; // the center of the tile relative to it's visual representation
    private Polygon visualHexagon; // a visual representation of the tile
    private List<GeneralPath> visualFish; // a visual representation of the fish on the tile

    /**
     * Constructor that takes in the position of the Tile and the number of fish it has.
     * @param position
     * @param fish
     */
    public FishTile(Point position, int fish) {
        this(position.x, position.y, fish);
    }

    /**
     * Constructor that takes in the position as an x and y coordinate of the
     * Tile and the number of fish it has.
     * @param x
     * @param y
     * @param fish
     */
    public FishTile(int x, int y, int fish) {
        this.position = new Point(x, y);
        this.fish = fish;
        this.center = this.calculateCenter();
        this.visualHexagon = this.generateHexagon();
        this.visualFish = this.generateFish();
    }

    /**
     * Calculates the center position of the tile relative to it's visual representation.
     * @return Point
     */
    private Point calculateCenter() {
        int xBuffer = this.position.x * HEX_SIZE;
        if (this.position.y % 2 == 1) {
            xBuffer = xBuffer + 2 * HEX_SIZE;
        }

        return new Point((int) ((HEX_SIZE * this.position.x * 3) + xBuffer + HEX_SIZE * 1.5) + 1, (HEX_SIZE * this.position.y)+ HEX_SIZE);
    }

    /**
     * Generates a visual representation of the Tile as a hexagon.
     * @return Polygon
     */
    private Polygon generateHexagon() {
        Polygon hexagon = new Polygon();
        int yBuffer = (HEX_SIZE * this.position.y);
        int xBuffer = this.position.x * HEX_SIZE * 4;
        if (this.position.y % 2 == 1) {
            xBuffer = xBuffer + 2 * HEX_SIZE;
        }

        hexagon.addPoint(xBuffer, HEX_SIZE + yBuffer);
        hexagon.addPoint(HEX_SIZE + xBuffer, yBuffer);
        hexagon.addPoint((2 * HEX_SIZE) + xBuffer, yBuffer);
        hexagon.addPoint((3 * HEX_SIZE) + xBuffer, HEX_SIZE + yBuffer);
        hexagon.addPoint((2 * HEX_SIZE) + xBuffer, (2 * HEX_SIZE) + yBuffer);
        hexagon.addPoint(HEX_SIZE + xBuffer, (2 * HEX_SIZE) + yBuffer);

        return hexagon;
    }

    /**
     * Generates a visual representation of the fish on the Tile as a list of Shapes.
     * @return
     */
    private List<GeneralPath> generateFish() {
        GeneralPath shape = new GeneralPath();
        Point center = this.getCenter();

        // draws a single fish in the center of the tile
        shape.moveTo(center.x - HEX_SIZE / 2, center.y);
        shape.curveTo(center.x - 5, center.y - 4, center.x + 5, center.y - 4, center.x + HEX_SIZE / 2 - 4, center.y - 2);
        shape.lineTo(center.x + HEX_SIZE / 2, center.y - 4);
        shape.lineTo(center.x + HEX_SIZE / 2, center.y + 4);
        shape.lineTo(center.x + HEX_SIZE / 2 - 4, center.y + 2);
        shape.curveTo(center.x + 5, center.y + 4, center.x - 5, center.y + 4, center.x - HEX_SIZE / 2, center.y);
        shape.closePath();

        // adds starting height to first fish
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.translate(0, -5 * (this.fish - 1));
        shape.transform(affineTransform);

        // creates multiple fish
        List<GeneralPath> fishList = new ArrayList<>();
        for (int i = 0; i < this.fish; i++) {
            GeneralPath fish = new GeneralPath(shape);

            // shift fish up by an amount
            AffineTransform affineTransform2 = new AffineTransform();
            affineTransform2.translate(0, i * 10);
            fish.transform(affineTransform2);

            fishList.add(fish);
        }
        return fishList;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public int getFish() {
        return this.fish;
    }

    @Override
    public Point getPosition() {
        return new Point(this.position);
    }

    @Override
    public Point getCenter() {
        return new Point(this.center);
    }

    @Override
    public Polygon getVisualHexagon() {
        return new Polygon(this.visualHexagon.xpoints, this.visualHexagon.ypoints, this.visualHexagon.npoints);
    }

    @Override
    public List<Shape> getVisualFish() {
        List<Shape> shapes = new ArrayList<>();
        for (Shape fish : this.visualFish) {
            shapes.add(new GeneralPath(fish));
        }
        return shapes;
    }

    @Override
    public Tile clone() {
        return new FishTile(new Point(this.position), this.fish);
    }

    @Override
    public String toString() {
        return "(" + this.getPosition() + ", Fish: " + this.getFish() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FishTile) {
            FishTile other = (FishTile) o;
            return this.position.equals(other.position) && this.fish == other.fish;
        }
        return false;
    }
}
