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
 */
public class GameBoard implements IGameBoard {

    private int rows; // rows of the board
    private int columns; // columns of the board
    private Tile[][] board; // the game board
    private Canvas canvas; // the game board canvas

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
        Tile[][] board = new Tile[columns][rows];
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                if (!holes.isEmpty() && holes.contains(new Point(i, j))) {
                    board[i][j] = new EmptyTile(i, j);
                } else {
                    int fish = sameFish == 0 ? rand.nextInt(5) + 1 : sameFish;
                    if (fish == 1) {
                        minOneFishTiles--;
                    }
                    board[i][j] = new FishTile(i, j, fish);
                }
            }
        }

        // guarentee there is a minimum number of one fish tiles
        while (sameFish == 0 && minOneFishTiles > 0) {
            int x = rand.nextInt(columns);
            int y = rand.nextInt(rows);
            int fish = board[x][y].getFish();
            if (fish > 1) {
                board[x][y] = new FishTile(x, y, 1);
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
        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                int fish = jsonArray.get(j).getAsJsonArray().get(i).getAsInt();
                if (fish == 0) {
                    board[i][j] = new EmptyTile(i, j);
                } else {
                    board[i][j] = new FishTile(i, j, fish);
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
                && !board[point.x][point.y].isEmpty();
    }

    @Override
    public Tile[][] getGameBoard() {
        return this.board.clone();
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
        return this.board[point.x][point.y].clone();
    }

    @Override
    public List<List<Tile>> getViablePaths(Tile tile) {
        return this.getViablePaths(tile.getPosition());
    }

    @Override
    public List<List<Tile>> getViablePaths(Point point) {
        List<List<Tile>> paths = new ArrayList<>();

        Direction direction = Direction.UP;

        // for each direction
        while (direction != null) {
            Point curPosition = new Point(point);
            List<Tile> path = new ArrayList<>();

            // while we can continue on a current path
            buildPath: while (true) {
                path.add(this.board[curPosition.x][curPosition.y]);

                Point newPosition;
                // for a given direction
                switch (direction) {
                    case UP:
                        newPosition = new Point(curPosition.x, curPosition.y - 2);
                        if (validTile(newPosition)) {
                            curPosition = newPosition;
                            break;
                        } else {
                            direction = Direction.DIAGONAL_UP_RIGHT;
                            break buildPath;
                        }
                    case DIAGONAL_UP_RIGHT:
                        if (curPosition.y % 2 == 0) {
                            newPosition = new Point(curPosition.x, curPosition.y - 1);
                        } else {
                            newPosition = new Point(curPosition.x + 1, curPosition.y - 1);
                        }

                        if (validTile(newPosition)) {
                            curPosition = newPosition;
                            break;
                        } else {
                            direction = Direction.DIAGONAL_DOWN_RIGHT;
                            break buildPath;
                        }
                    case DIAGONAL_DOWN_RIGHT:
                        if (curPosition.y % 2 == 0) {
                            newPosition = new Point(curPosition.x, curPosition.y + 1);
                        } else {
                            newPosition = new Point(curPosition.x + 1, curPosition.y + 1);
                        }

                        if (validTile(newPosition)) {
                            curPosition = newPosition;
                            break;
                        } else {
                            direction = Direction.DOWN;
                            break buildPath;
                        }
                    case DOWN:
                        newPosition = new Point(curPosition.x, curPosition.y + 2);
                        if (validTile(newPosition)) {
                            curPosition = newPosition;
                            break;
                        } else {
                            direction = Direction.DIAGONAL_DOWN_LEFT;
                            break buildPath;
                        }
                    case DIAGONAL_DOWN_LEFT:
                        if (curPosition.y % 2 == 0) {
                            newPosition = new Point(curPosition.x - 1, curPosition.y + 1);
                        } else {
                            newPosition = new Point(curPosition.x, curPosition.y + 1);
                        }

                        if (validTile(newPosition)) {
                            curPosition = newPosition;
                            break;
                        } else {
                            direction = Direction.DIAGONAL_UP_LEFT;
                            break buildPath;
                        }
                    case DIAGONAL_UP_LEFT:
                        if (curPosition.y % 2 == 0) {
                            newPosition = new Point(curPosition.x - 1, curPosition.y - 1);
                        } else {
                            newPosition = new Point(curPosition.x, curPosition.y - 1);
                        }

                        if (validTile(newPosition)) {
                            curPosition = newPosition;
                            break;
                        } else {
                            direction = null;
                            break buildPath;
                        }
                    default:
                        throw new IllegalArgumentException("Invalid direction");
                }
            }

            // add the path if it has at least two positions
            if (path.size() > 1) {
                paths.add(path);
            }
        }

        return paths;
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
    public Tile replaceTile(Tile tile) {
        return this.replaceTile(tile.getPosition());
    }

    @Override
    public Tile replaceTile(Point point) {
        Tile oldTile = this.board[point.x][point.y];
        this.board[point.x][point.y] = new EmptyTile(new Point(point));
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