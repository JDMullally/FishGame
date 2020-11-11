package model.board;

import com.google.gson.JsonArray;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static constants.Constants.HEX_SIZE;

/**
 * Class represents the entire GameBoard.
 * A GameBoard knows it's dimensions and has access to all of it's piece which are represented by Tiles.
 * A GameBoard can generate a board of Tiles, Find viable paths from a given point, and remove Tiles.
 * A GameBoard's Tiles are hexagonal and slide between each other as they move along the y axis.
 *
 * Our array keeps track of Points with the following conversion.
 * Board[y][x] = Point(x,y)
 *
 * An example 4x3 GameBoard represented graphically with x,y positions can be seen below:
 *
 * (0, 0)      (1, 0)      (2, 0)
 *       (0, 1)      (1, 1)      (2, 1)
 * (0, 2)      (1, 2)      (2, 2)
 *       (0, 3)      (1, 3)      (2, 3)
 */
public class GameBoard implements IGameBoard {

    private final int rows; // rows of the board
    private final int columns; // columns of the board
    private final Tile[][] board; // the game board (row major)
    private final Canvas canvas; // the game board canvas

    /**
     * Constructor that only takes in a rows and columns of the board.
     * @param rows
     * @param columns
     */
    public GameBoard(int rows, int columns) {
        this(rows, columns, new ArrayList<>(), 0, 0);
    }

    /**
     * Constructor that additionally takes a list of holes with the boards dimensions.
     * @param rows
     * @param columns
     * @param holes
     */
    public GameBoard(int rows, int columns, List<Point> holes) {
        this(rows, columns, new ArrayList<>(holes), 0, 0);
    }

    /**
     * Constructor that additionally takes in a minimum number of one fish
     * tiles along with the dimensions of the board.
     * @param rows
     * @param columns
     * @param minOneFishTiles
     */
    public GameBoard(int rows, int columns, int minOneFishTiles) {
        this(rows, columns, new ArrayList<>(), minOneFishTiles, 0);
    }

    /**
     * Constructor that takes the minimum number of one fish tiles, if all of the tiles should have
     * the same number of fish, a list of holes, and the dimensions of the board.
     * @param rows
     * @param columns
     * @param holes
     * @param minOneFishTiles
     * @param sameFish
     */
    public GameBoard(int rows, int columns, List<Point> holes, int minOneFishTiles, int sameFish) {
        this.rows = rows;
        this.columns = columns;
        this.board = this.generateBoard(rows, columns, new ArrayList<>(holes), minOneFishTiles, sameFish);
        this.canvas = this.generateCanvas(rows, columns);
    }

    /**
     * Constructor that takes in rows and columns of the board, as well as the board itself.
     *
     * @param rows
     * @param columns
     * @param board
     */
    public GameBoard(int rows, int columns, Tile[][] board) {
        this.rows = rows;
        this.columns = columns;
        this.board = board;
        this.canvas = this.generateCanvas(rows, columns);
    }

    /**
     * Constructor that takes in rows and columns of the board, as well as the board as a well
     * formated JsonArray.
     *
     * @param rows
     * @param columns
     * @param jsonArray
     */
    public GameBoard(int rows, int columns, JsonArray jsonArray) {
        this.rows = rows;
        this.columns = columns;
        this.board = this.generateBoard(rows, columns, jsonArray);
        this.canvas = this.generateCanvas(rows, columns);
    }

    /**
     * Returns a new game board with the specified parameters.
     *
     * @param rows
     * @param columns
     * @param holes
     * @param minOneFishTiles
     * @param sameFish
     * @return Tile[][]
     */
    private Tile[][] generateBoard(int rows, int columns, List<Point> holes, int minOneFishTiles, int sameFish) {
        if (rows < 1 || columns < 1) {
            throw new IllegalArgumentException("Width and Height must be greater than zero");
        }
        if (minOneFishTiles > rows * columns - holes.size()) {
            throw new IllegalArgumentException("The minimum number of one fish tiles must be less than or equal to the maximum"
                    + " number of tiles on the board minus the number of holes: "
                    + (rows * columns - holes.size()));
        }
        if (sameFish < 0 || sameFish > 5) {
            throw new IllegalArgumentException("The number of sameFish must be between 1 and 5 inclusive");
        }

        Random rand = new Random();

        // generate random board with holes
        Tile[][] board = new Tile[rows][columns];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                if (!holes.isEmpty() && holes.contains(new Point(x, y))) {
                    board[y][x] = new EmptyTile(x, y);
                } else {
                    int fish = sameFish == 0 ? rand.nextInt(5) + 1 : sameFish;
                    if (fish == 1) {
                        minOneFishTiles--;
                    }
                    board[y][x] = new FishTile(x, y, fish);
                }
            }
        }

        // guarentee there is a minimum number of one fish tiles
        while (sameFish == 0 && minOneFishTiles > 0) {
            int x = rand.nextInt(columns);
            int y = rand.nextInt(rows);
            int fish = board[y][x].getFish();
            if (fish > 1) {
                board[y][x] = new FishTile(x, y, 1);
                minOneFishTiles--;
            }
        }

        return board;
    }

    /**
     * Returns a new game board with the specified parameters.
     *
     * @param rows
     * @param columns
     * @param jsonArray
     * @return Tile[][]
     */
    private Tile[][] generateBoard(int rows, int columns, JsonArray jsonArray) {
        if (rows < 1 || columns < 1) {
            throw new IllegalArgumentException("Width and Height must be greater than zero");
        }

        Tile[][] board = new Tile[columns][rows];
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                int fish = jsonArray.get(y).getAsJsonArray().get(x).getAsInt();
                if (fish == 0) {
                    board[y][x] = new EmptyTile(x, y);
                } else {
                    board[y][x] = new FishTile(x, y, fish);
                }
            }
        }

        return board;
    }

    /**
     * Returns a new Canvas as a function of the rows and columns of the board.
     * @param rows
     * @param columns
     * @return Canvas
     */
    private Canvas generateCanvas(int rows, int columns) {
        int canvasWidth = columns * 4 * HEX_SIZE + HEX_SIZE + 3;
        int canvasHeight = (rows + 1) * HEX_SIZE + 3;
        return new Canvas(0, 0, canvasWidth, canvasHeight);
    }

    /**
     * Returns true if there is a valid Tile at the specified point
     * @param point
     * @return boolean
     */
    private boolean validTile(Point point) {
        return point.x >= 0
                && point.x < columns
                && point.y >= 0
                && point.y < rows
                && !board[point.y][point.x].isEmpty();
    }

    @Override
    public Tile[][] getGameBoard() {
        return Arrays.stream(this.board).map(Tile[]::clone).toArray($ -> this.board.clone());
    }

    @Override
    public Canvas getCanvas() {
        return this.canvas;
    }

    @Override
    public int getRows() {
        return this.rows;
    }

    @Override
    public int getColumns() {
        return this.columns;
    }

    @Override
    public Tile getTile(Point point) {
        return this.board[point.y][point.x].clone();
    }

    @Override
    public List<List<Tile>> getViablePaths(Tile tile) {
        return this.getViablePaths(tile.getPosition());
    }

    @Override
    public List<List<Tile>> getViablePaths(Point point) {
        List<List<Tile>> paths = new ArrayList<>();
        List<Tile> path;
        for (Direction dir: Direction.values()) {
            path = this.getPath(dir, point);
            if (path.size() > 1) {
                paths.add(path);
            }
        }
        return paths;
    }

    /**
     * Returns a List of Tiles reachable from the given point in the current direction.
     *
     * @param dir Direction
     * @param point Point
     * @return List of Tile
     */
    private List<Tile> getPath(Direction dir, Point point) {
        List<Tile> path = new ArrayList<>();
        Point current = point;
        do {
            path.add(this.board[current.y][current.x]);
            current = dir.apply(current);
        } while (validTile(current));
        return path;
    }

    @Override
    public List<Tile> getViableTiles(Tile tile) {
        return this.getViableTiles(tile.getPosition());
    }

    @Override
    public List<Tile> getViableTiles(Point point) {
        List<Tile> tiles = this.getViablePaths(new Point(point))
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        tiles.removeIf(tile -> tile.getPosition().x == point.x && tile.getPosition().y == point.y);

        return tiles;
    }

    @Override
    public Tile removeTile(Point point) {
        Tile oldTile = this.board[point.y][point.x];
        this.board[point.y][point.x] = new EmptyTile(new Point(point));
        return oldTile.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GameBoard) {
            GameBoard other = (GameBoard) o;
            return Arrays.deepEquals(this.board, other.board)
                && this.columns == other.columns
                && this.rows == other.rows;
        }
        return false;
    }

    @Override
    public String toString() {
        String lineSeparator = System.lineSeparator();
        StringBuilder sb = new StringBuilder();

        for (Tile[] row : this.board) {
            sb.append(Arrays.toString(row))
                .append(lineSeparator);
        }
        return sb.toString();
    }
}
