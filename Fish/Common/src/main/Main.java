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

    private static int width;
    private static int height;
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
            model = new GameBoard(width, height);
        } else if (minOneFishTiles == null) {
            model = new GameBoard(width, height, holes);
        } else if (holes == null) {
            model = new GameBoard(width, height, minOneFishTiles);
        } else {
            model = new GameBoard(width, height, holes, minOneFishTiles);
        }

        IView view = new VisualView(model.getGameBoard(), model.getCanvas());
        Controller controller = new Controller();
        controller.control(model, view);
    }

    /**
     * Parses program arguments:
     * -width [int]
     * -height [int]
     * -holes [List<int,int>] , example: '{[1,2], [5,6]}'
     * -minOneFishTiles [int]
     * @param args program arguments
     */
    private static void parseArgs(String[] args) throws ArgumentParserException {
        // creates argument parser
        ArgumentParser parser = ArgumentParsers.newArgumentParser("Main", true);

        // adds expected program arguments to parser
        parser.addArgument("--width")
                .dest("width")
                .type(Integer.class)
                .required(true)
                .help("Width of the GameBoard");
        parser.addArgument("--height")
                .dest("height")
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
            width = nameSpace.getInt("width");
            height = nameSpace.getInt("height");
            minOneFishTiles = nameSpace.getInt("minOneFishTiles");
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            throw e;
        }
    }
}
