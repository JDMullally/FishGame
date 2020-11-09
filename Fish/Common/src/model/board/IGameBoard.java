package model.board;

import java.awt.Point;
import java.util.List;

/**
 * Represents the entire GameBoard. A GameBoard can generate a board of Tiles,
 * show it's canvas for a graphical representation, show it's game board, get it's dimensions,
 * get a tile from a specific point, Find viable paths from a given point, and remove Tiles by
 * replacing them with Empty Tiles.
 */
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
    int getRows();

    /**
     * Returns the maximum height of the board.
     * @return int
     */
    int getColumns();

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
     * Replaces a Tile at the given Point with an EmptyTile and returns the old Tile.
     * @param point
     * @return Tile
     */
    Tile replaceTile(Point point);
}
