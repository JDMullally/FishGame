package model;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class GameBoardTest {

    private int holes1Size;
    private GameBoard modelHoles;
    private GameBoard modelSmall;
    private GameBoard empty;
    private GameBoard modelSmallError;
    private GameBoard modelTooManyMin;
    private GameBoard modelDefault;
    private GameBoard modelMinOnes;
    private GameBoard modelHolesAndMinOnes;
    private Tile tileOneOne;

    private List<Point> holes1;
    private List<Point> holesEmpty;
    private List<Point> holesInvalid;


    private void init() {
        this.holes1 = Arrays.asList(new Point(2, 2), new Point(2, 1));
        this.holesEmpty = Collections.singletonList(new Point(1, 1));
        this.holesInvalid = Collections.singletonList(new Point(100, 100));

        this.holes1Size = this.holes1.size();

        this.empty = new GameBoard(1, 1, this.holesEmpty);
        this.modelSmall = new GameBoard(1, 1);
        this.modelDefault = new GameBoard(4, 3);
        this.modelHoles = new GameBoard(4, 3, this.holes1);
        this.modelHolesAndMinOnes = new GameBoard(3, 4, this.holes1, 3, 0);
        this.modelMinOnes = new GameBoard(3, 4, 3);
        tileOneOne = this.modelDefault.getGameBoard()[1][1].clone();
    }

    /**
     * Tests for generateBoard through the Constructor
     */
    @Test(expected = IllegalArgumentException.class)
    public void tooManyHoles() {
        this.init();

        this.modelSmallError = new GameBoard(1, 1, this.holes1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyBoard() {
        this.init();

        this.modelSmallError = new GameBoard(0, 0);
    }

    @Test
    public void outOfBoundsHole() {
        this.init();

        GameBoard modelOutOfBoundHole = new GameBoard(4, 3, this.holesInvalid);
        int count = 0;

        Tile[][] testBoard = modelOutOfBoundHole.getGameBoard();
        for (Tile[] tiles : testBoard) {
            for (Tile tile : tiles) {
                if (tile.isEmpty()) {
                    count++;
                }
            }
        }
        assertEquals(0, count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void tooManyMinFish() {
        this.init();

        this.modelTooManyMin = new GameBoard(3, 4, 40);
    }

    @Test(expected = IllegalArgumentException.class)
    public void badWidthAndHeight() {
        this.init();

        this.modelDefault = new GameBoard(-1, -5);
    }

    @Test
    public void hasHoles() {
        this.init();

        int counter = holes1Size;
        assertNotEquals(0, counter);
        Tile[][] testBoard = this.modelHoles.getGameBoard();
        for (Tile[] tiles : testBoard) {
            for (Tile tile : tiles) {
                if (tile.isEmpty()) {
                    counter--;
                }
            }
        }
        assertEquals(0, counter);
    }

    @Test
    public void testSameFish() {
        this.init();

        this.modelDefault = new GameBoard(3, 4, new ArrayList<>(), 0, 1);

        boolean allOnes = true;

        Tile[][] testBoard = this.modelDefault.getGameBoard();
        for (Tile[] row : testBoard) {
            for (Tile tile : row) {
                if (tile.getFish() != 1) {
                    allOnes = false;
                }
            }
        }
        assertTrue(allOnes);
    }

    @Test
    public void testMinOneAndSameFish() {
        this.init();

        this.modelDefault = new GameBoard(3, 4, new ArrayList<>(), 10, 4);

        boolean allFours = true;

        Tile[][] testBoard = this.modelDefault.getGameBoard();
        for (Tile[] row : testBoard) {
            for (Tile tile : row) {
                if (tile.getFish() != 4) {
                    allFours = false;
                }
            }
        }
        assertTrue(allFours);
    }

    @Test
    public void testSameFishAndHoles() {
        this.init();

        this.modelDefault = new GameBoard(3, 4, Collections.singletonList(new Point(1, 1)), 0, 4);

        boolean noHoles = true;

        Tile[][] testBoard = this.modelDefault.getGameBoard();
        for (Tile[] row : testBoard) {
            for (Tile tile : row) {
                if (tile.getFish() != 4) {
                    noHoles = false;
                }
            }
        }

        assertFalse(noHoles);
    }

    /**
     * Tests for getGameBoard
     */
    @Test
    public void getGameBoard() {
        this.init();

        Tile[][] testBoard = new Tile[1][1];
        testBoard[0][0] = this.modelSmall.getTile(new Point(0, 0));

        assertEquals(testBoard, this.modelSmall.getGameBoard());
    }

    /**
     * Tests for getBoard Width and Height
     */
    @Test
    public void getRows() {
        this.init();

        assertEquals(1, this.modelSmall.getRows());
    }

    @Test
    public void getColumns() {
        this.init();

        assertEquals(1, this.modelSmall.getColumns());
    }

    /**
     * Tests for getViablePaths
     */
    /*@Test
    public void getViablePaths1() {
        this.init();

        //creates lists of expected tiles in the direction of the variable.
        List<Tile> down = Arrays.asList(tileOneOne, this.modelDefault.getTile(new Point(1, 3)));
        List<Tile> downRight = Arrays.asList(tileOneOne,
            this.modelDefault.getTile(new Point(2, 2)),
            this.modelDefault.getTile(new Point(2, 3)));
        List<Tile> downLeft = Arrays.asList(tileOneOne,
            this.modelDefault.getTile(new Point(1, 2)),
            this.modelDefault.getTile(new Point(0, 3)));
        List<Tile> upRight = Arrays.asList(tileOneOne, this.modelDefault.getTile(new Point(2, 0)));
        List<Tile> upLeft = Arrays.asList(tileOneOne, this.modelDefault.getTile(new Point(1, 0)));
        List<List<Tile>> testPaths = new ArrayList<>();

        //adds the paths to test paths in the same order the getViablePaths does.
        testPaths.add(upRight);
        testPaths.add(downRight);
        testPaths.add(down);
        testPaths.add(downLeft);
        testPaths.add(upLeft);

        //calls getViablePaths to compare to the list we created.
        List<List<Tile>> actualPaths = this.modelDefault.getViablePaths(tileOneOne);

        //compares the two lists to see if the function works.
        assertEquals(testPaths, actualPaths);
    }*/


    @Test
    public void getViablePaths2() {
        this.init();
        //gets viable paths of a board with a single empty tile.
        List<List<Tile>> actual = this.empty.getViablePaths(new Point(0, 0));

        List<List<Tile>> empty = new ArrayList<>();

        assertEquals(empty, actual);
    }

    /*@Test
    public void getViablePaths3() {
        this.init();
        //gets viable paths of a board from the empty tile of model Holes (empty at 2,2 and 2,1)
        List<List<Tile>> actual = this.modelHoles.getViablePaths(new Point(2, 2));

        //builds expected paths with expected paths
        List<List<Tile>> expected = new ArrayList<>();

        List<Tile> up = Arrays.asList(this.modelHoles.getTile(new Point(2, 2)),
            this.modelHoles.getTile(new Point(2, 0)));
        List<Tile> downRight = Arrays.asList(this.modelHoles.getTile(new Point(2, 2)),
            this.modelHoles.getTile(new Point(2, 3)));
        List<Tile> downLeft = Arrays.asList(this.modelHoles.getTile(new Point(2, 2)),
            this.modelHoles.getTile(new Point(1, 3)));
        List<Tile> upLeft = Arrays.asList(this.modelHoles.getTile(new Point(2, 2)),
            this.modelHoles.getTile(new Point(1, 1)),
            this.modelHoles.getTile(new Point(1, 0)));

        //adds them in order
        expected.add(up);
        expected.add(downRight);
        expected.add(downLeft);
        expected.add(upLeft);

        assertEquals(actual, expected);
    }*/ // TODO: fix

    @Test
    public void getViablePathsOutOfOrder() {
        this.init();
        //gets viable paths of a board from the empty tile of model Holes (empty at 2,2 and 2,1)
        List<List<Tile>> actual = this.modelHoles.getViablePaths(new Point(2, 2));

        //builds expected paths with expected paths
        List<List<Tile>> outofOrder = new ArrayList<>();

        List<Tile> up = Arrays.asList(this.modelHoles.getTile(new Point(2, 2)),
            this.modelHoles.getTile(new Point(2, 0)));
        List<Tile> downRight = Arrays.asList(this.modelHoles.getTile(new Point(2, 2)),
            this.modelHoles.getTile(new Point(2, 3)));
        List<Tile> downLeft = Arrays.asList(this.modelHoles.getTile(new Point(2, 2)),
            this.modelHoles.getTile(new Point(1, 3)));
        List<Tile> upLeft = Arrays.asList(this.modelHoles.getTile(new Point(2, 2)),
            this.modelHoles.getTile(new Point(1, 1)),
            this.modelHoles.getTile(new Point(1, 0)));

        //adds them in order
        outofOrder.add(downRight);
        outofOrder.add(downLeft);
        outofOrder.add(upLeft);
        outofOrder.add(up);

        assertNotEquals(outofOrder, actual);
    }

    /**
     * Tests for getViableTiles
     */
    /*@Test
    public void testGetViableTiles() {
        init();
        List<Tile> actual = this.modelHoles.getViableTiles(new Point(1, 1));

        Tile start = this.modelHoles.getTile(new Point(1, 1));
        List<Tile> expected = new ArrayList<>();
        expected.add(start);
        expected.add(this.modelHoles.getTile(new Point(2, 0)));
        expected.add(start);
        expected.add(this.modelHoles.getTile(new Point(1, 3)));
        expected.add(start);
        expected.add(this.modelHoles.getTile(new Point(1, 2)));
        expected.add(this.modelHoles.getTile(new Point(0, 3)));
        expected.add(start);
        expected.add(this.modelHoles.getTile(new Point(1, 0)));

        assertEquals(expected, actual);
    }*/

    @Test
    public void testReplaceTile1() {
        init();

        Tile tileCopy = this.modelDefault.getTile(new Point(1, 1)).clone();
        Tile removedTile = this.modelDefault.replaceTile(new Point(1, 1));

        assertEquals(tileCopy, removedTile);
    }

    @Test
    public void testReplaceTile2() {
        init();
        System.out.println(this.modelDefault);
        Tile tileCopy = this.modelHoles.getTile(new Point(2, 1)).clone();
        Tile removedMissingTile = this.modelHoles.replaceTile(new Point(2, 1));

        assertEquals(tileCopy, removedMissingTile);
    }
}
