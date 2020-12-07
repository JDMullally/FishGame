package model.testUtil;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.state.GameState;
import model.state.IGameState;
import model.state.IPenguin;
import model.state.IPlayer;
import model.state.Penguin;
import model.state.Player;

public class ClientUtil {

    public static IGameState createTestGameState4PlayersInvalid() {
        IPenguin peng1 = new Penguin(Color.WHITE, new Point(0, 0));
        IPenguin peng2 = new Penguin(Color.WHITE,new Point(4, 0));
        List<IPenguin> penguinList1 = Arrays.asList(peng1, peng2);

        IPenguin peng3 = new Penguin(Color.RED, new Point(1, 0));
        IPenguin peng4 = new Penguin(Color.RED, new Point(0, 1));
        List<IPenguin> penguinList2 = Arrays.asList(peng3, peng4);

        IPenguin peng5 = new Penguin(Color.BLACK, new Point(2, 0));
        IPenguin peng6 = new Penguin(Color.BLACK, new Point(1, 1));
        List<IPenguin> penguinList3= Arrays.asList(peng5, peng6);

        IPenguin peng7 = new Penguin(new Color(210, 105, 30), new Point(3, 0));
        IPenguin peng8 = new Penguin(new Color(210, 105, 30), new Point(2, 1));
        List<IPenguin> penguinList4= Arrays.asList(peng7, peng8);

        IPlayer p1 = new Player(Color.WHITE,20, penguinList1);
        IPlayer p2 = new Player(Color.RED,20, penguinList2);
        IPlayer p3 = new Player(Color.BLACK,20, penguinList3);
        IPlayer p4 = new Player(new Color(210, 105, 30),20, penguinList4);

        List<IPlayer> players = Arrays.asList(p1, p2, p3, p4);

        IGameState gameState = new GameState(2,2, new ArrayList<>(),
            0, 2, players);

        return gameState.clone();
    }



    /**
     * Creates a GameState where all the Penguins are placed and returns it. Great for testing!
     * @return IGameState is the new GameState.
     */
    public static IGameState createTestGameState2Players() {
        IPenguin peng1 = new Penguin(Color.WHITE, new Point(1, 0));
        IPenguin peng2 = new Penguin(Color.WHITE,new Point(2, 0));
        IPenguin peng3 = new Penguin(Color.WHITE, new Point(3, 0));
        IPenguin peng4 = new Penguin(Color.WHITE, new Point(4, 0));

        List<IPenguin> penguinList1 = Arrays.asList(peng1, peng2, peng3, peng4);

        IPenguin peng5 = new Penguin(Color.BLACK, new Point(1, 1));
        IPenguin peng6 = new Penguin(Color.BLACK, new Point(2, 1));
        IPenguin peng7 = new Penguin(Color.BLACK, new Point(3, 1));
        IPenguin peng8 = new Penguin(Color.BLACK, new Point(4, 1));

        List<IPenguin> penguinList2= Arrays.asList(peng5, peng6, peng7, peng8);

        IPlayer p1 = new Player(Color.WHITE,20, penguinList1);
        IPlayer p2 = new Player(Color.BLACK,20, penguinList2);

        List<IPlayer> players = Arrays.asList(p1, p2);

        IGameState gameState = new GameState(5,5, new ArrayList<>(),
            0, 2, players);

        return gameState.clone();
    }


    /**
     * Creates a GameState where all the Penguins are placed and returns it. Great for testing!
     * @return IGameState is the new GameState.
     */
    public static IGameState createTestGameState4Players() {
        IPenguin peng1 = new Penguin(Color.WHITE, new Point(0, 0));
        IPenguin peng2 = new Penguin(Color.WHITE,new Point(4, 0));
        List<IPenguin> penguinList1 = Arrays.asList(peng1, peng2);

        IPenguin peng3 = new Penguin(Color.RED, new Point(1, 0));
        IPenguin peng4 = new Penguin(Color.RED, new Point(0, 1));
        List<IPenguin> penguinList2 = Arrays.asList(peng3, peng4);

        IPenguin peng5 = new Penguin(Color.BLACK, new Point(2, 0));
        IPenguin peng6 = new Penguin(Color.BLACK, new Point(1, 1));
        List<IPenguin> penguinList3= Arrays.asList(peng5, peng6);

        IPenguin peng7 = new Penguin(new Color(210, 105, 30), new Point(3, 0));
        IPenguin peng8 = new Penguin(new Color(210, 105, 30), new Point(2, 1));
        List<IPenguin> penguinList4= Arrays.asList(peng7, peng8);

        IPlayer p1 = new Player(Color.WHITE,20, penguinList1);
        IPlayer p2 = new Player(Color.RED,20, penguinList2);
        IPlayer p3 = new Player(Color.BLACK,20, penguinList3);
        IPlayer p4 = new Player(new Color(210, 105, 30),20, penguinList4);

        List<IPlayer> players = Arrays.asList(p1, p2, p3, p4);

        IGameState gameState = new GameState(5,5, new ArrayList<>(),
            0, 2, players);

        return gameState.clone();
    }


    /**
     * Creates a GameState where all the Penguins are placed and returns it. Great for testing!
     * @return IGameState is the new GameState.
     */
    public static IGameState createTestGameState3Players() {
        IPenguin peng1 = new Penguin(Color.WHITE, new Point(0, 0));
        IPenguin peng2 = new Penguin(Color.WHITE,new Point(3, 0));
        IPenguin peng3 = new Penguin(Color.WHITE, new Point(1, 1));

        List<IPenguin> penguinList1 = Arrays.asList(peng1, peng2, peng3);

        IPenguin peng4 = new Penguin(Color.BLACK, new Point(1, 0));
        IPenguin peng5 = new Penguin(Color.BLACK, new Point(4, 0));
        IPenguin peng6 = new Penguin(Color.BLACK, new Point(2, 1));

        List<IPenguin> penguinList2 = Arrays.asList(peng4, peng5, peng6);

        IPenguin peng7 = new Penguin(Color.RED, new Point(2, 0));
        IPenguin peng8 = new Penguin(Color.RED, new Point(0, 1));
        IPenguin peng9 = new Penguin(Color.RED, new Point(3, 1));

        List<IPenguin> penguinList3= Arrays.asList(peng7, peng8, peng9);

        IPlayer p1 = new Player(Color.WHITE,20, penguinList1);
        IPlayer p2 = new Player(Color.BLACK,20, penguinList2);
        IPlayer p3 = new Player(Color.RED,20, penguinList3);

        List<IPlayer> players = Arrays.asList(p1, p2, p3);

        IGameState gameState = new GameState(5,5, new ArrayList<>(),
            0, 2, players);

        return gameState.clone();
    }

    public static IGameState createTestGameStateNoPenguins() {

        IPlayer p1 = new Player(Color.WHITE,20, new ArrayList<>());
        IPlayer p2 = new Player(Color.BLACK,20, new ArrayList<>());

        List<IPlayer> players = Arrays.asList(p1, p2);

        IGameState gameState = new GameState(5,5, new ArrayList<>(),
            0, 2, players);

        return gameState.clone();
    }

}
