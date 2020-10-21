package model;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import model.board.GameBoard;
import model.board.IGameBoard;
import model.board.Tile;
import model.state.GameState;
import model.state.IGameState;
import model.state.IPenguin;
import model.state.IPlayer;
import model.state.Penguin;
import model.state.Player;
import model.tree.Action;
import model.tree.GameTree;

import model.tree.IGameTree;
import model.tree.Move;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameTreeTest {

    private IGameState gameState, emptyState;
    private Penguin peng1, peng2, peng3, peng4, peng5, peng6, peng7, peng8;
    private List<IPlayer> players;
    private Player p1, p2;
    private IGameTree<IGameTree> gameTree;
    private Action moveDown;
    private Function<IGameState, IGameTree> func;


    private void init() {
        this.peng1 = new Penguin(Color.WHITE, null);
        this.peng2 = new Penguin(Color.WHITE,null);
        this.peng3 = new Penguin(Color.WHITE, null);
        this.peng4 = new Penguin(Color.WHITE, null);

        List<IPenguin> penguinList1 = Arrays.asList(peng1, peng2, peng3, peng4);

        this.peng5 = new Penguin(Color.BLACK, null);
        this.peng6 = new Penguin(Color.BLACK, null);
        this.peng7 = new Penguin(Color.BLACK, null);
        this.peng8 = new Penguin(Color.BLACK, null);

        List<IPenguin> penguinList2= Arrays.asList(peng5, peng6, peng7, peng8);

        this.p1 = new Player(Color.WHITE,20, penguinList1);
        this.p2 = new Player(Color.BLACK,20, penguinList2);

        this.players = Arrays.asList(this.p1, this.p2);

        this.gameState = new GameState(8,8, new ArrayList<>(), 0, 2, this.players);

        this.placeAllPenguins();

        this.gameTree = new GameTree<IGameTree>(this.gameState);

        this.func = new Function<IGameState, IGameTree>() {
            @Override
            public IGameTree apply(IGameState gameState) {
                return new GameTree(gameState.clone());
            }
        };
    }

    private void placeAllPenguins() {
        for (int j = 0; j < this.players.size(); j++) {
            IPlayer player = this.players.get(j);
            for (int i = 0; i < player.getPenguins().size(); i++)
                this.gameState.placePenguin(player.getPenguins().get(i), player,
                    this.gameState.getTile(new Point(j, i)));
        }
    }

    /**
     * Test for getState
     */
    @Test
    public void getState() {
        init();
        IGameState state = this.gameTree.getState();
        assertEquals(this.gameState, state);
    }

    /**
     * Tests that we cannot mutate our state after receiving it from getState.
     */
    @Test
    public void getStateChanged() {
        init();
        IGameState state = this.gameTree.getState();
        assertEquals(8, state.getTurn());
        IPlayer current = state.playerTurn();
        IPenguin lastPenguinInList = current.getPenguins().get(current.getPenguins().size() - 1);
        Point newPos = new Point(lastPenguinInList.getPosition().x, lastPenguinInList.getPosition().y + 2);
        state.move(current, lastPenguinInList, newPos, false);

        assertNotEquals(this.gameTree.getState().getTurn(), state.getTurn());
    }

    /**
     * Tests for queryAction
     */
    @Test
    public void queryAction() {
        init();
        IGameState state = this.gameTree.getState();
        IPlayer current = state.playerTurn();
        IPenguin lastPenguinInList = current.getPenguins().get(current.getPenguins().size() - 1);
        Point newPos = new Point(lastPenguinInList.getPosition().x, lastPenguinInList.getPosition().y + 2);
        this.moveDown = new Move(current, lastPenguinInList, newPos, false);

        IGameState newState = this.gameTree.queryAction(state, this.moveDown);

        assertNotEquals(state, newState);
    }

    /**
     * Query Action should throw an error if the action is invalid
     */
    @Test (expected = IllegalArgumentException.class)
    public void queryActionIncorrect() {
        init();
        IGameState state = this.gameTree.getState();
        IPlayer current = state.playerTurn();
        IPenguin firstPenguinInList = current.getPenguins().get(0);
        Point newPos = new Point(firstPenguinInList.getPosition().x, firstPenguinInList.getPosition().y - 2);

        //Player should not be able to move their first penguin up.
        Action moveUp = new Move(current, firstPenguinInList, newPos, false);

        IGameState newState = this.gameTree.queryAction(state, moveUp);
    }

    /**
     * Query Action should throw an error if the action is invalid
     */
    @Test (expected = IllegalArgumentException.class)
    public void queryActionMovingOntoIncorrectTile() {
        init();
        IGameState state = this.gameTree.getState();
        IPlayer current = state.playerTurn();
        IPenguin lastPenguinInList = current.getPenguins().get(current.getPenguins().size() - 1);
        Point oldPos = new Point(lastPenguinInList.getPosition());

        Point newPos = new Point(lastPenguinInList.getPosition().x, lastPenguinInList.getPosition().y + 2);
        this.moveDown = new Move(current, lastPenguinInList, newPos, false);
        Action moveUp = new Move(current, lastPenguinInList, oldPos, false);

        IGameState newState = this.gameTree.queryAction(state, this.moveDown);
        IGameTree newTree = new GameTree(newState);
        newState = newTree.queryAction(newState, moveUp);
    }

    @Test
    public void queryActionPass() {
        init();

        IGameState state = this.gameTree.getState();
        IPlayer current = state.playerTurn();
        IPenguin lastPenguinInList = current.getPenguins().get(current.getPenguins().size() - 1);
        Point newPos = new Point(lastPenguinInList.getPosition().x, lastPenguinInList.getPosition().y + 2);
        this.moveDown = new Move(current, lastPenguinInList, newPos, true);

        IGameState newState = this.gameTree.queryAction(state, this.moveDown);

        assertEquals(state.getTurn() + 1, newState.getTurn());
    }

    @Test
    public void applyFunctionToTurnGameStatesIntoGameTree() {
        init();

        List<IGameTree> listOfGameTree =
            this.gameTree.applyFunction(this.gameTree.getState(), this.func);

        boolean createsGameTrees = true;
        for (IGameTree gameTree: listOfGameTree) {
            createsGameTrees = createsGameTrees && gameTree != null;
        }
        assertTrue(createsGameTrees);
    }
}
