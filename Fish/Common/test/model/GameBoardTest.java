package model;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class GameBoardTest {

    int holes1Size;
    GameBoard modelHoles;
    GameBoard modelSmall;
    GameBoard modelSmallError;
    GameBoard modelTooManyMin;
    GameBoard modelDefault;
    GameBoard modelMinOnes;
    GameBoard modelHolesAndMinOnes;
    Tile tileOneOne;

    List<Point> holes1;
    List<Point> holesInvalid;


    public void init() {
        this.holes1 = Arrays.asList(new Point(2,2), new Point(2,1));
        this.holesInvalid = Arrays.asList(new Point(100, 100));

        this.holes1Size = this.holes1.size();

        this.modelSmall = new GameBoard(1,1);
        this.modelDefault = new GameBoard(4,3);
        this.modelHoles = new GameBoard(3, 4, this.holes1);
        this.modelHolesAndMinOnes = new GameBoard(3,4, this.holes1,3);
        this.modelMinOnes = new GameBoard(3,4,3);

        tileOneOne = this.modelDefault.getGameBoard()[1][1].clone();
    }

    /**
     * Tests for Constructor
     */
    @Test (expected = IllegalArgumentException.class)
    public void tooManyHoles() {
        this.init();

        this.modelSmallError = new GameBoard(1,1, this.holes1);
    }

    @Test (expected = IllegalArgumentException.class)
    public void emptyBoard() {
        this.init();

        this.modelSmallError = new GameBoard(0,0);
    }

    @Test
    public void outOfBoundsHole() {
        this.init();

        GameBoard modelOutOfBoundHole = new GameBoard(4,3, this.holesInvalid);
        int count = 0;

        Tile[][] testBoard = modelOutOfBoundHole.getGameBoard();
        for (int i = 0; i < testBoard.length; i++) {
            for  (int j = 0; j < testBoard[i].length; j++) {
                if (testBoard[i][j].isEmpty()) {
                    count++;
                }
            }
        }
        assertEquals(0,count);

    }

    @Test (expected = IllegalArgumentException.class)
    public void tooManyMinFish() {
        this.init();

        this.modelTooManyMin = new GameBoard(3,4,40);
    }

    @Test (expected = IllegalArgumentException.class)
    public void badWidthAndHeight() {
        this.init();

        this.modelDefault = new GameBoard(-1,-5);
    }

    @Test
    public void hasHoles() {
        this.init();

        int counter = holes1Size;
        assertNotEquals(0,counter);
        Tile[][] testBoard = this.modelHoles.getGameBoard();
        for (int i = 0; i < testBoard.length; i++) {
            for  (int j = 0; j < testBoard[i].length; j++) {
                if (testBoard[i][j].isEmpty()) {
                    counter--;
                }
            }
        }
        assertEquals(0, counter);
    }

    /**
     * Tests for getGameBoard
     */
    @Test
    public void getGameBoard() {
        this.init();

        Tile[][]testboard = new Tile[1][1];
        testboard[0][0] = this.modelSmall.getTile(new Point(0,0));

        assertEquals(testboard, this.modelSmall.getGameBoard());
    }

    /**
     * Tests for getBoard Width and Height
     */
    @Test
    public void getBoardWidth() {
        this.init();

        assertEquals(1, this.modelSmall.getBoardWidth());
    }

    @Test
    public void getBoardHeight() {
        this.init();

        assertEquals(1, this.modelSmall.getBoardHeight());
    }

    /**
     * Tests for getViablePaths
     */
    @Test
    public void getViablePaths1() {
        this.init();

        List<Tile> down = Arrays.asList(tileOneOne, this.modelDefault.getTile(new Point(3,1)));
        List<Tile> downRight = Arrays.asList(tileOneOne,
            this.modelDefault.getTile(new Point(2,2)),
            this.modelDefault.getTile(new Point(3,2)));
        List<Tile> downLeft = Arrays.asList(tileOneOne,
            this.modelDefault.getTile(new Point(2,1)),
            this.modelDefault.getTile(new Point(3,0)));
        List<Tile> upRight = Arrays.asList(tileOneOne, this.modelDefault.getTile(new Point(0,2)));
        List<Tile> upLeft = Arrays.asList(tileOneOne, this.modelDefault.getTile(new Point(0,1)));
        List<List<Tile>> testPaths = new ArrayList<>();
        testPaths.add(upRight);
        testPaths.add(downRight);
        testPaths.add(down);
        testPaths.add(downLeft);
        testPaths.add(upLeft);

        List<List<Tile>> actualPaths = this.modelDefault.getViablePaths(tileOneOne);

        assertEquals(testPaths, actualPaths);
    }

    //TODO Finish these tests
    @Test
    public void getViablePaths2() {
        this.init();
    }

    /**
     * Tests for getViableTiles
     */
    @Test
    public void testGetViableTiles() {

    }

    @Test
    public void testReplaceTile() {
    }
}
