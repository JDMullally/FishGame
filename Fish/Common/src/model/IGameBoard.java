package model;

import java.awt.Point;
import java.util.List;

public interface IGameBoard {

    /**
     * Returns the GameBoard as a 2D array of Tiles.
     * @return 2D array of Tile
     */
    public Tile[][] getGameBoard();

    /**
     * Returns the maximum width of the board.
     * @return int
     */
    public int getBoardWidth();

    /**
     * Returns the maximum height of the board.
     * @return int
     */
    public int getBoardHeight();

    /**
     * Returns a Tile at a given point on the board.
     * @param point
     * @return Tile
     */
    public Tile getTile(Point point);
    /**
     * Returns all viable paths from the current Tile.
     * @param tile
     * @return List of List of Tile
     */
    public List<List<Tile>> getViablePaths(Tile tile);

    /**
     * Returns all viable paths from the current Point.
     * @param point
     * @return List of List of Tile
     */
    public List<List<Tile>> getViablePaths(Point point);

    /**
     * Returns all viable Tiles that can be moved to from the current Tile.
     * @param tile
     * @return List of Tile
     */
    public List<Tile> getViableTiles(Tile tile);

    /**
     * Returns all viable Tiles that can be moved to from the current Point.
     * @param point
     * @return List of Tile
     */
    public List<Tile> getViableTiles(Point point);

    /**
     * Replaces a Tile with an EmptyTile and returns the old Tile.
     * @param tile
     * @return Tile
     */
    public Tile replaceTile(Tile tile);

    /**
     * Replaces a Tile at the given Point with an EmptyTile and returns the old Tile.
     * @param point
     * @return Tile
     */
    public Tile replaceTile(Point point);

}
