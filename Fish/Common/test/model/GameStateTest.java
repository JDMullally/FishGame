package model;


import com.google.gson.JsonObject;
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import sun.security.provider.PolicyParser.PermissionEntry;

import static org.junit.Assert.*;


public class GameStateTest {

    IGameState gameState;
    IGameBoard board;
    Penguin peng1, peng2, peng3, peng4;
    List<IPlayer> players;
    Player p1, p2;

    public void init() {
        this.board = new GameBoard(5,5);
        this.peng1 = new Penguin(Color.WHITE, new Point(1,1));
        this.peng2 = new Penguin(Color.WHITE, new Point(1,2));
        this.peng3 = new Penguin(Color.BLACK, new Point(3,1));
        this.peng4 = new Penguin(Color.BLACK, new Point(1,3));
        List<Penguin> penguinList1 = Arrays.asList(peng1, peng2);
        List<Penguin> penguinList2 = Arrays.asList(peng3, peng4);
        this.p1 = new Player(20, "Billy", penguinList1, Color.WHITE);
        this.p1 = new Player(21, "Billy", penguinList2, Color.BLACK);

        players = Arrays.asList(p1, p2);
        gameState = new GameState(board, players);
    }

    @Test
    public void convertToJSon() {
        init();

        JsonObject jsonObject = gameState.GameStateToJson();

        System.out.println(board);
        System.out.println(jsonObject);

    }

}
