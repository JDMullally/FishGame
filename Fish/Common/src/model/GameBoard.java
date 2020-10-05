package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GameBoard implements IGameBoard {

    private int width; // width of the board
    private int height; // height of the board
    private Tile[][] board; // the game board

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

    /**
     * Returns a new game board with the specified parameters.
     * @param width
     * @param height
     * @param holes
     * @param minOneFishTiles
     * @return Tile[][]
     */
    private Tile[][] generateBoard(int width, int height, List<Point> holes, int minOneFishTiles) {
        if (width < 1 || height < 1) {
            throw new IllegalArgumentException("Width and Height must be greater than zero");
        }
        if (minOneFishTiles > width * height - holes.size()) {
            throw new IllegalArgumentException("The minimum number of one fish tiles must be less than or equal to the maximum"
                    + " number of tiles on the board minus the number of holes: "
                    + (width * height - holes.size()));
        }

        Tile[][] board = new Tile[width][height];

        // generate random board with holes
        Random rand = new Random();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (!holes.isEmpty() && holes.contains(new Point(i, j))) {
                    board[i][j] = new EmptyTile(i, j);
                } else {
                    int fish = rand.nextInt(5) + 1;
                    if (fish == 1) {
                        minOneFishTiles--;
                    }
                    board[i][j] = new FishTile(i, j, fish);
                }
            }
        }

        // guarentee there is a minimum number of one fish tiles
        while (minOneFishTiles > 0) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            int fish = board[x][y].getFish();
            if (fish > 1) {
                board[x][y] = new FishTile(x, y, 1);
                minOneFishTiles--;
            }
        }

        return board;
    }

    /**
     * Returns true if there is a valid Tile at the specified point
     * @param point
     * @return boolean
     */
    private boolean validTile(Point point) {
        return point.x >= 0
                && point.x < width
                && point.y >= 0
                && point.y < height
                && !board[point.x][point.y].isEmpty();
    }

    @Override
    public Tile[][] getGameBoard() {
        return this.board.clone();
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
                        newPosition = new Point(curPosition.x - 2, curPosition.y);
                        if (validTile(newPosition)) {
                            curPosition = newPosition;
                            break;
                        } else {
                            direction = Direction.DIAGONAL_UP_RIGHT;
                            break buildPath;
                        }
                    case DIAGONAL_UP_RIGHT:
                        if (curPosition.x % 2 == 0) {
                            newPosition = new Point(curPosition.x - 1, curPosition.y);
                        } else {
                            newPosition = new Point(curPosition.x - 1, curPosition.y + 1);
                        }

                        if (validTile(newPosition)) {
                            curPosition = newPosition;
                            break;
                        } else {
                            direction = Direction.DIAGONAL_DOWN_RIGHT;
                            break buildPath;
                        }
                    case DIAGONAL_DOWN_RIGHT:
                        if (curPosition.x % 2 == 0) {
                            newPosition = new Point(curPosition.x + 1, curPosition.y);
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
                        newPosition = new Point(curPosition.x + 2, curPosition.y);
                        if (validTile(newPosition)) {
                            curPosition = newPosition;
                            break;
                        } else {
                            direction = Direction.DIAGONAL_DOWN_LEFT;
                            break buildPath;
                        }
                    case DIAGONAL_DOWN_LEFT:
                        if (curPosition.x % 2 == 0) {
                            newPosition = new Point(curPosition.x + 1, curPosition.y - 1);
                        } else {
                            newPosition = new Point(curPosition.x + 1, curPosition.y);
                        }

                        if (validTile(newPosition)) {
                            curPosition = newPosition;
                            break;
                        } else {
                            direction = Direction.DIAGONAL_UP_LEFT;
                            break buildPath;
                        }
                    case DIAGONAL_UP_LEFT:
                        if (curPosition.x % 2 == 0) {
                            newPosition = new Point(curPosition.x - 1, curPosition.y - 1);
                        } else {
                            newPosition = new Point(curPosition.x - 1, curPosition.y);
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
        return this.getViablePaths(new Point(point))
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
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
            return this.board.equals(other.board)
                && this.height == other.height
                && this.width == other.width;
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
