package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class GameBoard implements IGameBoard {

    private int width;
    private int height;
    private List<List<Tile>> board;

    /**
     * Constructor that only takes in a width and height of the board.
     * @param width
     * @param height
     */
    public GameBoard(int width, int height) {
        this.width = width;
        this.height = height;
        this.board = this.generateBoard(width, height, new ArrayList<>(), 0);
    }

    /**
     * Constructor that additionally takes a list of holes with the boards dimensions.
     * @param width
     * @param height
     * @param holes
     */
    public GameBoard(int width, int height, List<Point> holes) {
        this.width = width;
        this.height = height;
        this.board = this.generateBoard(width, height, new ArrayList<>(holes), 0);
    }

    /**
     * Constructor that additionally takes in a minimum number of one fish
     * tiles along with the dimensions of the board.
     * @param width
     * @param height
     * @param minOneFishTiles
     */
    public GameBoard(int width, int height, int minOneFishTiles) {
        this.width = width;
        this.height = height;
        this.board = this.generateBoard(width, height, new ArrayList<>(), minOneFishTiles);
    }

    /**
     * Constructor that takes the minimum number of one fish tiles,
     * a list of holes and the dimensions of the board.
     * @param width
     * @param height
     * @param holes
     * @param minOneFishTiles
     */
    public GameBoard(int width, int height, List<Point> holes, int minOneFishTiles) {
        this.width = width;
        this.height = height;
        this.board = this.generateBoard(width, height, new ArrayList<>(holes), minOneFishTiles);
    }

    //TODO make this generate something.
    private List<List<Tile>> generateBoard(int width, int height, List<Point> holes, int minOneFishTiles) {
        return null;
    }

    @Override
    public List<List<Tile>> getGameBoard() {
        return new ArrayList<>(this.board);
    }

    @Override
    public int getBoardWidth() {
        return this.width;
    }

    @Override
    public int getBoardHeight() {
        return this.height;
    }


    @Override
    public List<Tile> getViablePaths(Tile tile) {
        return null;
    }

    @Override
    public List<Tile> getViablePaths(Point point) {
        return null;
    }

    @Override
    public Tile replaceTile(Tile tile) {

        return null;
    }

    @Override
    public Tile replaceTile(Point point) {
        Tile oldTile = this.board.get(point.x).get(point.y);
        Tile newTile = new EmptyTile(new Point(point));
        this.board.get(point.x).set(point.y, newTile);
        return oldTile;
    }
}
