package model.board;

import java.awt.Point;
import java.util.function.Function;

/**
 * Represents a direction to move from one hexagon to another.
 */
public enum Direction implements Function<Point, Point> {
    UP(0, -2),
    DIAGONAL_UP_RIGHT(1, -1),
    DIAGONAL_DOWN_RIGHT(1, 1),
    DOWN(0, 2),
    DIAGONAL_DOWN_LEFT(-1, 1),
    DIAGONAL_UP_LEFT(-1, -1);

    private int xOffset;
    private int yOffset;

    Direction(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    @Override
    public Point apply(Point point) {
        int x = point.x;
        int y = point.y + this.yOffset;
        if (point.y % 2 == 0 && this.xOffset < 0 || point.y % 2 == 1 && this.xOffset > 0) {
            x = point.x + this.xOffset;
        }
        return new Point(x,y);
    }
}
