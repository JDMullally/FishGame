package model;

import java.awt.Point;
import java.util.List;

public interface IGameBoard {

    /**
     * Returns the GameBoard as a 2D array of Tiles.
     * @return 2D array of Tile
     */
    Tile[][] getGameBoard();

    /**
     * Returns the GameBoards canvas.
     * @return Canvas
     */
    Canvas getCanvas();

    /**
     * Returns the maximum width of the board.
     * @return int
     */
    int getBoardWidth();

    /**
     * Returns the maximum height of the board.
     * @return int
     */
    int getBoardHeight();

    /**
     * Returns a Tile at a given point on the board.
     * @param point
     * @return Tile
     */
    Tile getTile(Point point);
    /**
     * Returns all viable paths from the current Tile.
     * @param tile
     * @return List of List of Tile
     */
    List<List<Tile>> getViablePaths(Tile tile);

    /**
     * Returns all viable paths from the current Point.
     * @param point
     * @return List of List of Tile
     */
    List<List<Tile>> getViablePaths(Point point);

    /**
     * Returns all viable Tiles that can be moved to from the current Tile.
     * @param tile
     * @return List of Tile
     */
    List<Tile> getViableTiles(Tile tile);

    /**
     * Returns all viable Tiles that can be moved to from the current Point.
     * @param point
     * @return List of Tile
     */
    List<Tile> getViableTiles(Point point);

    /**
     * Replaces a Tile with an EmptyTile and returns the old Tile.
     * @param tile
     * @return Tile
     */
    Tile replaceTile(Tile tile);

    /**
     * Replaces a Tile at the given Point with an EmptyTile and returns the old Tile.
     * @param point
     * @return Tile
     */
    Tile replaceTile(Point point);
}
