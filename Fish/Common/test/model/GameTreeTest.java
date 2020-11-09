package model;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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
import model.tree.MovePenguin;
import model.tree.PassPenguin;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameTreeTest {

    private IGameState gameState, emptyState, smallerGameState;
    private Penguin peng1, peng2, peng3, peng4, peng5, peng6, peng7, peng8;
    private List<IPlayer> players;
    private Player p1, p2;
    private IGameTree gameTree, smallGameTree;
    private Action moveDown, pass;
    private Function<IGameState, IGameTree> func, func2, func3, failfunc;
    //private Function<IGameState, IGameState> func3;


    private void init() {
        this.peng1 = new Penguin(Color.WHITE, new Point(0, 1));
        this.peng2 = new Penguin(Color.WHITE,new Point(0, 2));
        this.peng3 = new Penguin(Color.WHITE, new Point(0, 3));
        this.peng4 = new Penguin(Color.WHITE, new Point(0, 4));

        List<IPenguin> penguinList1 = Arrays.asList(peng1, peng2, peng3, peng4);

        this.peng5 = new Penguin(Color.BLACK, new Point(1, 1));
        this.peng6 = new Penguin(Color.BLACK, new Point(1, 2));
        this.peng7 = new Penguin(Color.BLACK, new Point(1, 3));
        this.peng8 = new Penguin(Color.BLACK, new Point(1, 4));

        List<IPenguin> penguinList2= Arrays.asList(peng5, peng6, peng7, peng8);

        this.p1 = new Player(Color.WHITE,20, penguinList1);
        this.p2 = new Player(Color.BLACK,10, penguinList2);

        this.players = Arrays.asList(this.p1, this.p2);

        this.gameState = new GameState(8,8, new ArrayList<>(), 0, 2, this.players);

        this.smallerGameState = new GameState(8,2, new ArrayList<>(), 0, 2, this.players);

        this.gameTree = new GameTree(this.gameState);

        this.smallGameTree = new GameTree(this.smallerGameState);

        this.func = new Function<IGameState, IGameTree>() {
            @Override
            public IGameTree apply(IGameState gameState) {
                return new GameTree(gameState.clone());
            }
        };

    }

    private void init2() {
        this.peng1 = new Penguin(Color.WHITE, new Point(0, 0));
        this.peng2 = new Penguin(Color.WHITE, new Point(1, 0));
        this.peng3 = new Penguin(Color.WHITE, new Point(2, 0));
        this.peng4 = new Penguin(Color.WHITE, new Point(3, 0));

        List<IPenguin> penguinList1 = Arrays.asList(peng1, peng2, peng3, peng4);

        this.peng5 = new Penguin(Color.BLACK, new Point(4, 0));
        this.peng6 = new Penguin(Color.BLACK, new Point(5, 0));
        this.peng7 = new Penguin(Color.BLACK, new Point(6, 0));
        this.peng8 = new Penguin(Color.BLACK, new Point(7, 0));

        List<IPenguin> penguinList2= Arrays.asList(peng5, peng6, peng7, peng8);

        this.p1 = new Player(Color.WHITE,20, penguinList1);
        this.p2 = new Player(Color.BLACK,20, penguinList2);

        this.players = Arrays.asList(this.p1, this.p2);

        this.gameState = new GameState(8,8, new ArrayList<>(), 0, 2, this.players);

        IPlayer current = this.gameState.playerTurn();
        IPenguin firstPenguinInList = current.getPenguins().get(0);
        Point newPos = new Point(firstPenguinInList.getPosition().x, firstPenguinInList.getPosition().y + 2);
        this.pass = new PassPenguin(current);

        this.gameTree = new GameTree(this.gameState.clone());

        this.func2 = new Function<IGameState, IGameTree>() {
            @Override
            public IGameTree apply(IGameState gameState) {
                //System.out.println(gameState.playerTurn());
                Action pass = new PassPenguin(gameState.playerTurn());
                IGameState tryMoveDown = new GameTree(gameState.clone()).queryAction(gameState.clone(), pass);
                try {
                    return new GameTree(tryMoveDown);
                } catch (IllegalArgumentException e) {
                    return new GameTree(gameState);
                }
            }
        };

        this.func3 = new Function<IGameState, IGameTree>() {
            @Override
            public IGameTree apply(IGameState gameState) {
                IPlayer player = gameState.playerTurn();
                IPenguin penguin = player.getPenguins().get(0);
                Point penguinPoint = penguin.getPosition();
                List<Tile> viableTiles = gameState.getViableTiles(penguinPoint);
                if (viableTiles.isEmpty()) {
                    return new GameTree(gameState);
                } else {
                   return new GameTree(gameState.move(player, penguin, viableTiles.get(0), false));
                }
            }
        };

        this.failfunc = new Function<IGameState, IGameTree>() {
            public IGameTree apply(IGameState gameState) {
                IPlayer player = gameState.playerTurn();
                IPenguin penguin = player.getPenguins().get(0);
                Point newPoint = new Point(penguin.getPosition().x, penguin.getPosition().y + 2);
                return new GameTree(gameState.move(player, penguin, newPoint, false));
            }
        };
    }


/**************************************************************************************************/
/***************************************** TESTS **************************************************/
/**************************************************************************************************/

    /**
     * Test for getState
     */

    /**
     * General test for getState that tests if we can get the state.
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
        IPlayer current = state.playerTurn();
        IPenguin lastPenguinInList = current.getPenguins().get(current.getPenguins().size() - 1);
        Point newPos = new Point(lastPenguinInList.getPosition().x, lastPenguinInList.getPosition().y + 2);
        state.move(current, lastPenguinInList, newPos, false);

        assertNotEquals(this.gameTree.getState().playerTurn(), state.playerTurn());
    }

    /**
     * Tests for queryAction
     */

    /**
     * Tests a basic action with queryAction that checks that the new state is not equivalent to
     * the old state.
     */
    @Test
    public void queryAction() {
        init();
        IGameState state = this.gameTree.getState();
        IPlayer current = state.playerTurn();
        IPenguin lastPenguinInList = current.getPenguins().get(current.getPenguins().size() - 1);
        Point newPos = new Point(lastPenguinInList.getPosition().x, lastPenguinInList.getPosition().y + 2);
        this.moveDown = new MovePenguin(current, lastPenguinInList, newPos);

        IGameState newState = this.gameTree.queryAction(state, this.moveDown);

        assertNotEquals(state, newState);
    }

    /**
     * Query Action should throw an error if the action is invalid.
     * Here I have a player try to move a penguin off the map.
     */
    @Test (expected = IllegalArgumentException.class)
    public void queryActionIncorrect() {
        init();
        IGameState state = this.gameTree.getState();
        IPlayer current = state.playerTurn();
        IPenguin firstPenguinInList = current.getPenguins().get(0);
        Point newPos = new Point(firstPenguinInList.getPosition().x, firstPenguinInList.getPosition().y - 2);

        //Player should not be able to move their first penguin up.
        Action moveUp = new MovePenguin(current, firstPenguinInList, newPos);

        IGameState newState = this.gameTree.queryAction(state, moveUp);
    }

    /**
     * Query Action should throw an error if the action is invalid.  This test has the player try to
     * move back to their old position after their opponent has passed.
     */
    @Test (expected = IllegalArgumentException.class)
    public void queryActionMovingOntoIncorrectTileWithTurns() {
        init();
        IGameState state = this.gameTree.getState();
        IPlayer current = state.playerTurn();
        IPlayer nextPlayer = state.getPlayers().get(1);
        IPenguin lastPenguinInList = current.getPenguins().get(current.getPenguins().size() - 1);
        IPenguin lastPenguinInPlayer2List = nextPlayer.getPenguins().get(current.getPenguins().size() - 1);
        Point oldPos = new Point(lastPenguinInList.getPosition());

        Point newPos = new Point(lastPenguinInList.getPosition().x, lastPenguinInList.getPosition().y + 2);
        this.moveDown = new MovePenguin(current, lastPenguinInList, newPos);
        Action otherPlayerPass = new PassPenguin(nextPlayer);
        Action moveUp = new MovePenguin(current, lastPenguinInList, oldPos);

        //First move by player 1
        IGameState newState = this.gameTree.queryAction(state, this.moveDown);
        IGameTree newTree = new GameTree(newState);

        //Pass by player 2
        newState = newTree.queryAction(newState, otherPlayerPass);
        newTree = new GameTree(newState);

        //Player tries to move back to their previous spot
        newState = newTree.queryAction(newState, moveUp);
    }

    /**
     * Checks that querying actions gives a new state that increments it's turn no matter the action.
     * In this example, we just used a pass action.
     */
    @Test
    public void queryActionPass() {
        init();

        IGameState state = this.gameTree.getState();
        IPlayer current = state.playerTurn();
        IPenguin lastPenguinInList = current.getPenguins().get(current.getPenguins().size() - 1);
        Point newPos = new Point(lastPenguinInList.getPosition().x, lastPenguinInList.getPosition().y + 2);
        this.moveDown = new PassPenguin(current);

        IGameState newState = this.gameTree.queryAction(state, this.moveDown);

        assertNotEquals(state.playerTurn(), newState.playerTurn());
    }

    /**
     * Tests for applyFunction
     */

    /**
     * Simple function that returns a list of GameTrees that hold GameStates reachable
     * from the current GameState.
     */
    @Test
    public void applyFunctionToTurnGameStatesIntoGameTree() {
        init();

        Map<Action, IGameTree> mapOfGameTree =
                this.gameTree.applyFunction(this.gameTree.getState(), this.func);

        boolean createsGameTrees = true;
        for (IGameTree gameTree: mapOfGameTree.values()) {
            createsGameTrees = createsGameTrees && gameTree instanceof GameTree;
        }
        assertTrue(createsGameTrees);
    }

    /**
     * If apply function works as intended, this should be turn three, so we will return to player 1.
     * Uses a simple pass action to test that turns are working as intended.
     */
    @Test
    public void applyFunctionIfNextPlayerPass() {
        init2();

        IGameState prevState = this.gameTree.getState();
        Map<Action, IGameTree> mapOfGameTreeMoveDown =
            this.gameTree.applyFunction(this.gameTree.getState(), this.func2);

        boolean backToFirstPlayer = true;

        for (IGameTree treeNode :  mapOfGameTreeMoveDown.values()) {
            backToFirstPlayer = backToFirstPlayer && treeNode.getState().playerTurn().equals(prevState.playerTurn());
        }

        assertTrue(backToFirstPlayer);
    }


    /**
     * We apply a function to all reachable states that has penguin move to the next possible position.
     * This test checks that players should acquire some score during these moves.
     */
    @Test
    public void applyFunctionConditionalMoveAndCheckNewScore() {
        init2();

        IGameState state1 = this.gameTree.getState();

        int currentScore = state1.playerTurn().getScore();

        Map<Action, IGameTree> mapOfGameTreeFirstMove =
            this.gameTree.applyFunction(this.gameTree.getState(), this.func3);

        boolean otherPlayersScore = true;

        for (IGameTree treeNode :  mapOfGameTreeFirstMove.values()) {
            otherPlayersScore = otherPlayersScore && (treeNode.getState().playerTurn().getScore() != currentScore
                && treeNode.getState().getPlayers().get(1).getScore() != currentScore);
        }

        assertTrue(otherPlayersScore);
    }

    /**
     * A function must be designed with the rules in mind and understand it will be applied to all
     * reachable states.
     */
    @Test (expected = IllegalArgumentException.class)
    public void applyFunctionBadFunction() {
        init2();

        Map<Action, IGameTree> mapOfGameTree =
            this.gameTree.applyFunction(this.gameTree.getState(), this.failfunc);

        for (IGameTree tree: mapOfGameTree.values()) {
            tree.applyFunction(tree.getState(), this.failfunc);
        }
    }

    // TODO add more apply tests

    /**
     * Test for getSubstates
     */

    /**
     * Test to check that getSubstates does not return null before the substates are created with
     * getCompleteTree
     */
    @Test
    public void getSubstatesEmpty() {
        init();

        assertEquals(this.gameTree.getSubstates().size(), 0);
    }

    /**
     * Test to check that getSubstates does return a list of IGameTree after the substates are
     * created with getCompleteTree
     */
    @Test
    public void getSubstatesCalledCreateTree() {
        init();

        this.gameTree.createCompleteTree();

        boolean substatesNotEmpty = !this.gameTree.getSubstates().isEmpty();

        assertTrue(substatesNotEmpty);
    }

    /**
     * Test for create Complete tree.
     */

    /**
     * Tests that an initial GameTree has empty substates and calling createCompleteTree
     * fills the substates in the tree.
     */
    @Test
    public void createTreeEmptySubStateThenFilledSubStates() {
        init();

        Map<Action, IGameTree> substates = this.gameTree.getSubstates();

        assertTrue(substates.isEmpty());

        this.gameTree = this.gameTree.createCompleteTree();

        //System.out.println(this.gameTree.getSubstates());

        assertFalse(this.gameTree.getSubstates().isEmpty());
    }

    /**
     * Tests that additional calls of createCompleteTree increase the depth of the tree by 1.
     */
    @Test
    public void createTreeDepth2() {
        init();
        int desiredDepth = 2;
        for (int i = 0; i < desiredDepth; i++) {
            this.smallGameTree = this.smallGameTree.createCompleteTree();
        }

        boolean goesToDepthTwo = false;
        Map<Action, IGameTree> substates = this.smallGameTree.getSubstates();
        if(substates.isEmpty()) {
            assertFalse(goesToDepthTwo);
        } else {
            for (Map.Entry<Action, IGameTree> tree : substates.entrySet()) {
                goesToDepthTwo = goesToDepthTwo || !tree.getValue().getSubstates().isEmpty();
            }
        }
        assertTrue(goesToDepthTwo);
    }

    /**
     * Test for createTreeToDepth
     */

    /**
     * Tests that createTree to Depth works identically to createCompleteTree called once and should
     * create the same number of sub states.
     */
    @Test
    public void createTreeToDepth1() {
        init();

        IGameTree newTree = new GameTree(this.gameState.clone());

        this.gameTree.createCompleteTree();

        newTree.createTreeToDepth(newTree.getState(), 1);

        assertEquals(this.gameTree.getSubstates().size(), newTree.getSubstates().size());
    }
}
