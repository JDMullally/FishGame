package main;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.awt.Point;
import java.util.List;

import controller.Controller;
import model.GameBoard;
import model.IGameBoard;
import view.IView;
import view.VisualView;

/**
 * Entry point for the application starts the Fish Game.
 */
public class Main {

    private static int rows;
    private static int columns;
    private static List<Point> holes;
    private static Integer minOneFishTiles;

    /**
     * Entry point for the Fish Game.
     * @param args program arguments
     */
    public static void main(String[] args) throws ArgumentParserException {
        parseArgs(args);

        // sets game board
        IGameBoard model;
        if (holes == null && minOneFishTiles == null) {
            model = new GameBoard(rows, columns);
        } else if (minOneFishTiles == null) {
            model = new GameBoard(rows, columns, holes);
        } else if (holes == null) {
            model = new GameBoard(rows, columns, minOneFishTiles);
        } else {
            model = new GameBoard(rows, columns, holes, minOneFishTiles);
        }

        IView view = new VisualView(model.getGameBoard(), model.getCanvas());
        Controller controller = new Controller();
        controller.control(model, view);
    }

    /**
     * Parses program arguments:
     * -rows [int]
     * -columns [int]
     * -holes [List<int,int>] , example: '{[1,2], [5,6]}'
     * -minOneFishTiles [int]
     * @param args program arguments
     */
    private static void parseArgs(String[] args) throws ArgumentParserException {
        // creates argument parser
        ArgumentParser parser = ArgumentParsers.newArgumentParser("Main", true);

        // adds expected program arguments to parser
        parser.addArgument("-r", "--rows")
                .dest("rows")
                .type(Integer.class)
                .required(true)
                .help("Width of the GameBoard");
        parser.addArgument("-c", "--columns")
                .dest("columns")
                .type(Integer.class)
                .required(true)
                .help("Height of the GameBoard");
        // TODO: List of points
        parser.addArgument("--minOneFishTiles")
                .dest("minOneFishTiles")
                .type(Integer.class)
                .required(false)
                .help("Minimum number of one fish tiles");

        // parses program arguments
        try {
            Namespace nameSpace = parser.parseArgs(args);
            rows = nameSpace.getInt("rows");
            columns = nameSpace.getInt("columns");
            minOneFishTiles = nameSpace.getInt("minOneFishTiles");
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            throw e;
        }
    }
}
