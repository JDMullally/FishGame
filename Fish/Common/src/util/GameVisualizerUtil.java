package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import model.games.GameAction;
import model.games.IGameResult;
import model.games.PlayerAI;
import model.games.Referee;
import model.state.IGameState;
import model.state.ImmutableGameState;
import model.state.ImmutableGameStateModel;
import model.strategy.Strategy;
import model.tree.PlayerInterface;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import view.IView;
import view.VisualView;

public class GameVisualizer {

    private static int numPlayers;


    public static void main(String[] args) throws ArgumentParserException, InterruptedException {
        List<String> colors = Arrays.asList("Red", "White", "Brown", "Black");
        List<PlayerInterface> players;
        parseArgs(args);
        players = generatePlayers(colors);
        runGame(players);
    }

    private static List<PlayerInterface> generatePlayers(List<String> colors) {
        List<PlayerInterface> players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            int playerNumber = i + 1;
            players.add(new PlayerAI(new Strategy(), 2,
                playerNumber, colors.get(i)));
        }
        return players;
    }

    public static void runGame(List<PlayerInterface> players) throws InterruptedException {
        Referee ref = new Referee(players, 4,4);

        IGameState initialState = ref.getGameState();

        ImmutableGameStateModel gameStateModel = new ImmutableGameState(initialState);

        ref.runGame();

        List<GameAction> actions = ref.getOngoingActions();

        IGameResult gameResult = ref.getGameResult();

        renderGame(actions, gameStateModel, gameResult);

        //System.exit(0);
    }

    private static void renderGame(List<GameAction> actions, ImmutableGameStateModel gameStateModel,
        IGameResult gameResult) throws InterruptedException {
        IView view = new VisualView(gameStateModel);
        view.makeVisible();

        for (GameAction action: actions) {
            TimeUnit.MILLISECONDS.sleep(500);
            gameStateModel = gameStateModel.getNextGameState(action);
            view.update(gameStateModel);
        }

        view.update(gameStateModel, gameResult);
        TimeUnit.SECONDS.sleep(3);
        //System.exit(0);
    }

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
