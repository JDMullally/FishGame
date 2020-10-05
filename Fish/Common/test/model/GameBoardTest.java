package model;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class GameBoardTest {

    GameBoard modelHoles;
    GameBoard modelSmall;
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


        this.modelDefault = new GameBoard(4,3);
        this.modelHoles = new GameBoard(3, 4, this.holes1);
        //this.modelSmall = new GameBoard(1,1, this.holes1);
        this.modelHolesAndMinOnes = new GameBoard(3,4, this.holes1,3);
        this.modelMinOnes = new GameBoard(3,4,3);
        //this.modelTooManyMin = new GameBoard(3,4,40);

        tileOneOne = this.modelDefault.getGameBoard()[1][1].clone();
    }


    @Test
    public void getGameBoard() {
    }

    @Test
    public void getBoardWidth() {
    }

    @Test
    public void getBoardHeight() {
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

        System.out.println(testPaths);
        System.out.println(actualPaths);

        assertEquals(testPaths, actualPaths);
    }

    @Test
    public void getViableTiles() {
    }

    /**
     * Tests for getViableTiles
     */
    @Test
    public void testGetViableTiles() {

    }


    @Test
    public void replaceTile() {
    }

    @Test
    public void testReplaceTile() {
    }
}
