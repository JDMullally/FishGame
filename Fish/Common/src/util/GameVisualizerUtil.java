package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import model.games.IGameAction;
import model.games.IGameResult;
import model.games.PlayerAI;
import model.games.Referee;
import model.humans.GameObserver;
import model.humans.GameVisualizer;
import model.humans.IGameObserver;
import model.humans.IGameVisualizer;
import model.state.ImmutableGameState;
import model.state.ImmutableGameStateModel;
import model.strategy.Strategy;
import model.tree.PlayerInterface;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * A GameVisualizerUtil handles the functionality of the view for AI Players. It will generate a game based on the given
 * args to generate the AI players. After it creates the AI players, it creates a Referee and starts a game. Observers will be
 * allowed to view the game at any state.
 */
public class GameVisualizerUtil {

    private static int numPlayers;

    public static void main(String[] args) throws ArgumentParserException, InterruptedException {
        List<String> colors = Arrays.asList("Red", "White", "Brown", "Black");
        List<PlayerInterface> players;
        parseArgs(args);
        players = generatePlayers(colors);
        runGame(players);
    }

    /**
     * This function generates the AI players based on the colors given.
     * @param colors
     * @return
     */
    private static List<PlayerInterface> generatePlayers(List<String> colors) {
        List<PlayerInterface> players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            int playerNumber = i + 1;
            players.add(new PlayerAI(new Strategy(), 2,
                playerNumber, colors.get(i)));
        }
        return players;
    }

    /**
     * This function runs a single game. It takes in the list of PlayerInterfaces and creates a Referee.
     * After a Game is complete, the IGameActions are stored in a list for future observers to view a given GameState.
     * The visualizer will tell the observer to update their current GameState view after a brief delay.
     *
     * @param players
     * @throws InterruptedException
     */
    public static void runGame(List<PlayerInterface> players) throws InterruptedException {
        Referee ref = new Referee(players, 4,4);

        ref.runGame();
        List<IGameAction> actions = ref.getOngoingActions();
        IGameResult gameResult = ref.getGameResult();
        ImmutableGameStateModel initial = new ImmutableGameState(ref.getInitialGameState());

        IGameObserver observer1 = new GameObserver();
        List<IGameObserver> observers = new ArrayList<>();
        observers.add(observer1);

        IGameVisualizer visualizer = new GameVisualizer(initial, actions, gameResult, observers);

        visualizer.sendGame();

        TimeUnit.SECONDS.sleep(3);

        System.exit(0);
    }

    /**
     * This function parses the given args.
     * @param args
     * @throws ArgumentParserException
     */
    private static void parseArgs(String[] args) throws ArgumentParserException {
        ArgumentParser parser = ArgumentParsers.newArgumentParser("Main", true);

        // adds expected program arguments to parser
        parser.addArgument("-np", "--numPlayers")
            .dest("numPlayers")
            .type(Integer.class)
            .required(true)
            .help("Number of Players in the Game")
            .choices(2,3,4);


        // parses program arguments
        try {
            Namespace nameSpace = parser.parseArgs(args);
            numPlayers = nameSpace.getInt("numPlayers");

        } catch (ArgumentParserException e) {
            System.out.print("A game can only be run with between 2 and 4 players.\n" +
                "Please enter a valid number of players as an integer. \n");
           System.exit(1);
        }
    }

}
