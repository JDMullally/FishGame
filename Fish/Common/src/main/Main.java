package main;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import controller.Controller;
import model.state.GameState;
import model.state.IPlayer;
import model.state.ImmutableGameState;
import model.state.ImmutableGameStateModel;
import model.state.Player;
import util.ColorUtil;
import view.IView;
import view.VisualView;

/**
 * Entry point for the application starts the Fish Game.
 */
public class Main {

    private static int rows;
    private static int columns;
    private static List<Point> holes;
    private static int minOneFishTiles;
    private static int sameFish;
    private static List<IPlayer> players;

    /**
     * Entry point for the Fish Game.
     * @param args program arguments
     */
    public static void main(String[] args) throws ArgumentParserException {
        // parses arguments
        parseArgs(args);

        // sets game board, view, model and runs them
        GameState model = new GameState(rows, columns, holes, minOneFishTiles, sameFish, players);
        ImmutableGameStateModel immutableModel = new ImmutableGameState(model);

        IView view = new VisualView(immutableModel);
        Controller controller = new Controller();
        controller.control(model, view);
    }

    /**
     * Parses program arguments:
     * --rows [int]
     * --columns [int]
     * --holes [List<Integer>] , example: '--holes 1 2 5 6' means points at [1,2] and [5,6]
     * --minOneFishTiles [int]
     * --sameFish [int] (between 1-5 inclusive)
     * --players [List<String>] , example: '--players 'red' 'brown' means players with colors red and brown
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
                .help("Rows of the GameBoard");
        parser.addArgument("-c", "--columns")
                .dest("columns")
                .type(Integer.class)
                .required(true)
                .help("Columns of the GameBoard");
        parser.addArgument("-ho", "--holes")
                .dest("holes")
                .type(Integer.class)
                .nargs("*")
                .required(false)
                .help("Holes in the GameBoard (as points)");
        parser.addArgument("-moft", "--minOneFishTiles")
                .dest("minOneFishTiles")
                .type(Integer.class)
                .required(false)
                .help("Minimum number of one fish tiles");
        parser.addArgument("-sf", "--sameFish")
                .dest("sameFish")
                .type(Integer.class)
                .required(false)
                .help("If all of the tiles should have the same number of fish and how many");
        parser.addArgument("-p", "--players")
                .dest("players")
                .type(String.class)
                .nargs("*")
                .required(true)
                .help("The players playing the game");

        // parses program arguments
        try {
            Namespace nameSpace = parser.parseArgs(args);

            Integer minOneFishTilesTemp = nameSpace.getInt("minOneFishTiles");
            Integer sameFishTemp = nameSpace.getInt("sameFish");

            rows = nameSpace.getInt("rows");
            columns = nameSpace.getInt("columns");
            holes = toPointList(nameSpace.getList("holes"));
            minOneFishTiles = minOneFishTilesTemp == null ? 0 : minOneFishTilesTemp;
            sameFish = sameFishTemp == null ? 0 : sameFishTemp;
            players = toPlayers(nameSpace.getList("players"));
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            throw e;
        }
    }

    /**
     * Converts a List of Integers to a list of Points.
     * @param list List of Integers
     * @return List<Point>
     */
    private static List<Point> toPointList(List<Integer> list) {
        if (list == null) {
            return new ArrayList<>();
        } if (list.size() % 2 == 1) {
            throw new IllegalArgumentException("--holes : " + list + " --- List of Points must be even");
        }

        List<Point> points = new ArrayList<>();
        for (int i = 1; i < list.size(); i += 2) {
            points.add(new Point(list.get(i - 1), list.get(i)));
        }

        return points;
    }

    /**
     * Converts a List of Strings to a list of IPlayer's.
     * @param list List of String
     * @return List<Point>
     */
    private static List<IPlayer> toPlayers(List<String> list) {
        if (list == null) {
            return new ArrayList<>();
        }

        List<IPlayer> players = new ArrayList<>();
        for (int i = 0; i < list.size(); i ++) {
            players.add(new Player(ColorUtil.toColor(list.get(i)), i + 1, new ArrayList<>()));
        }

        return players;
    }
}
