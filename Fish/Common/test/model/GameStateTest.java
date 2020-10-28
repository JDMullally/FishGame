package model;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.Test;

import model.board.EmptyTile;
import model.board.Tile;
import model.state.GameState;
import model.state.IGameState;
import model.state.IPenguin;
import model.state.IPlayer;
import model.state.Penguin;
import model.state.Player;

import static org.junit.Assert.*;

/**
 * Tests all public methods for the GameState Class
 */
public class GameStateTest {

    IGameState gameState, emptyState;
    Penguin peng1, peng2, peng3, peng4, peng5, peng6, peng7, peng8;
    List<IPlayer> players;
    Player p1, p2;

    private void init() {
        this.peng1 = new Penguin(Color.WHITE, new Point(1, 1));
        this.peng2 = new Penguin(Color.WHITE,new Point(1, 1));
        this.peng3 = new Penguin(Color.WHITE, new Point(1, 1));
        this.peng4 = new Penguin(Color.WHITE, new Point(1, 1));

        List<IPenguin> penguinList1 = Arrays.asList(peng1, peng2, peng3, peng4);

        this.peng5 = new Penguin(Color.BLACK, new Point(1, 1));
        this.peng6 = new Penguin(Color.BLACK, new Point(1, 1));
        this.peng7 = new Penguin(Color.BLACK, new Point(1, 1));
        this.peng8 = new Penguin(Color.BLACK, new Point(1, 1));

        List<IPenguin> penguinList2= Arrays.asList(peng5, peng6, peng7, peng8);

        this.p1 = new Player(Color.WHITE,20, penguinList1);
        this.p2 = new Player(Color.BLACK,20, penguinList2);

        this.players = Arrays.asList(this.p1, this.p2);

        this.gameState = new GameState(8,8, new ArrayList<>(), 0, 2, this.players);
    }

    /**
     * Gets a gamestate with penguin positions in a diagnoal pattern.
     */
    private void init2() {
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
        this.p2 = new Player(Color.BLACK,20, penguinList2);

        this.players = Arrays.asList(this.p1, this.p2);

        this.gameState = new GameState(8,8, new ArrayList<>(), 0, 2, this.players);
    }

    /**
     * Gets a gameState where there is one penguin that can't move, but the rest can.
     */
    private IGameState getOnePenguinCannotMove() {
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
        this.p2 = new Player(Color.BLACK,20, penguinList2);

        this.players = Arrays.asList(this.p1, this.p2);

        IGameState state = new GameState(8,8,
                new ArrayList<>(), 0, 0, this.players);

        return state;
    }

    /**
     * Gets a gameState where there are holes all around the penguins so they cannot move
     * @return IGameState
     */
    private IGameState getHolesAroundPenguin() {
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
        this.p2 = new Player(Color.BLACK,20, penguinList2);

        this.players = Arrays.asList(this.p1, this.p2);

        List<Point> holes = Arrays.asList(new Point(2,0), new Point(2,1),
                new Point(2,2), new Point(2,3), new Point(2, 4),
                new Point(2, 5), new Point(0, 0), new Point(1, 0));

        IGameState state = new GameState(5,4, holes, 0, 0, this.players);

        return state;
    }

    /**
     * Gets a list of holes for our GameState Constructor.
     * @param maxRow max number of rows.
     * @param maxCol max number of columns.
     * @return List of Points that represent holes.
     */
    private List<Point> getFullHoleList(int maxRow, int maxCol) {
        List<Point> holes = new ArrayList<>();
        for(int rows = 0; rows < maxRow; rows++) {
            for (int cols = 0; cols < maxCol; cols++) {
                holes.add(new Point(rows, cols));
            }
        }
        return holes;
    }
/**************************************************************************************************/
/***************************************** TESTS **************************************************/
/**************************************************************************************************/

    /**
     * Constructor Tests
     */
    @Test (expected = IllegalArgumentException.class)
    public void gameStateConstructorNullPlayers() {
        this.gameState = new GameState(5,5,
                new ArrayList<>(), 2,2, null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void gameStateConstructorNoPlayers() {
        this.gameState = new GameState(5,5,
                new ArrayList<>(), 2,2, new ArrayList<>());

    }

    @Test (expected = IllegalArgumentException.class)
    public void gameStateConstructorTooManyPlayers() {
        IPlayer player1 = new Player(Color.BLACK, 4, new ArrayList<>());
        IPlayer player2 = new Player(Color.BLACK, 4, new ArrayList<>());
        IPlayer player3 = new Player(Color.BLACK, 4, new ArrayList<>());
        IPlayer player4 = new Player(Color.BLACK, 4, new ArrayList<>());
        IPlayer player5 = new Player(Color.BLACK, 4, new ArrayList<>());
        List<IPlayer> players = Arrays.asList(player1, player2, player3, player4, player5);
        this.gameState = new GameState(5,5,
                new ArrayList<>(), 2,2, players);
    }

    @Test (expected = IllegalArgumentException.class)
    public void gameStateConstructorWrongNumberPenguins() {
        IPlayer player1 = new Player(Color.BLACK, 4, new ArrayList<>());
        List<IPlayer> players = Arrays.asList(player1);
        this.gameState = new GameState(5,5,
                new ArrayList<>(), 2,2, players);
    }

    @Test (expected = IllegalArgumentException.class)
    public void gameStateConstructorInvalidPenguinColors() {
        this.peng1 = new Penguin(Color.WHITE, new Point(1,1));
        this.peng2 = new Penguin(Color.WHITE,new Point(1,1));
        this.peng3 = new Penguin(Color.WHITE, new Point(1,1));
        this.peng4 = new Penguin(Color.WHITE, new Point(1,1));
        this.peng5 = new Penguin(Color.WHITE, new Point(1,1));
        List<IPenguin> penguinList1 = Arrays.asList(peng1, peng2, peng3, peng4, peng5);

        IPlayer player1 = new Player(Color.BLACK, 4, penguinList1);

        List<IPlayer> players = Arrays.asList(player1);
        this.gameState = new GameState(5,5,
                new ArrayList<>(), 2,2, players);
    }


    @Test (expected = IllegalArgumentException.class)
    public void gameStateConstructorTwoPlayersSameColor() {
        this.peng1 = new Penguin(Color.WHITE, new Point(1,1));
        this.peng2 = new Penguin(Color.WHITE, new Point(1,1));
        this.peng3 = new Penguin(Color.WHITE, new Point(1,1));
        this.peng4 = new Penguin(Color.WHITE, new Point(1,1));

        List<IPenguin> penguinList1 = Arrays.asList(peng1, peng2, peng3, peng4);

        this.peng5 = new Penguin(Color.WHITE, new Point(1,1));
        this.peng6 = new Penguin(Color.WHITE, new Point(1,1));
        this.peng7 = new Penguin(Color.WHITE, new Point(1,1));
        this.peng8 = new Penguin(Color.WHITE, new Point(1,1));

        List<IPenguin> penguinList2 = Arrays.asList(peng5, peng6, peng7, peng8);

        IPlayer player1 = new Player(Color.WHITE, 4, penguinList1);
        IPlayer player2 = new Player(Color.WHITE, 4, penguinList2);

        List<IPlayer> players = Arrays.asList(player1, player2);

        this.gameState = new GameState(5,5,
                new ArrayList<>(), 2,2, players);
    }

    @Test
    public void getPlayers() {
        init();
        List<IPlayer> testPlayers = this.gameState.getPlayers();

        assertEquals(this.players, testPlayers);
    }

    /**
     * Tests for playerTurn
     */
    @Test
    public void playerTurn() {
        init();
        List<IPlayer> players = this.gameState.getPlayers();
        IPlayer player = players.get(0);

        assertEquals(player, this.gameState.playerTurn());
    }

    /**
     * Tests for getPossibleMoves
     */

    //Deprecated Test : You can never get possible moves with a null Penguin.
    public void getPossibleMovesNull() {
        init();

        IPlayer player = this.gameState.getPlayers().get(0);

        Map<IPenguin, List<Tile>> moves= this.gameState.getPossibleMoves(player);

        List<Tile> peng1moves = moves.get(peng1);

        assertEquals(this.gameState.getViableTiles(this.peng1.getPosition()), peng1moves);
    }

    @Test
    public void getPossibleMoves() {
        init2();
        IPlayer player = this.gameState.playerTurn();

        Map<IPenguin, List<Tile>> moves= this.gameState.getPossibleMoves(player);

        List<Tile> peng1moves = moves.get(peng1);

        assertNotEquals(this.gameState.getViableTiles(this.peng1.getPosition()), peng1moves);
    }

    /**
     * place Penguin methods.
     * A Penguin should always be able to be placed during placement rounds,
     */

    /**
     * Test for placing a penguin on the board that is already placed.
     */
    @Test (expected = IllegalArgumentException.class)
    public void placePenguinAlreadyPlaced() {
        init();

        IPenguin penguin1 = this.gameState.getPlayers().get(0).getPenguins().get(0);


        Point newPos = new Point(0,0);
        this.gameState.placePenguin(
                this.gameState.getPlayers().get(0),
                this.gameState.getTile(newPos));

        assertEquals(newPos, penguin1.getPosition());

    }

    @Test (expected = IllegalArgumentException.class)
    public void placePenguinNullPlayer() {
        init();

        IPenguin penguin1 = this.gameState.getPlayers().get(0).getPenguins().get(0);
        Point newPos = new Point(0,0);
        this.gameState.placePenguin(
                null,
                this.gameState.getTile(newPos));
    }

    @Test (expected = IllegalArgumentException.class)
    public void placePenguinNullTile() {
        init();

        IPenguin penguin1 = this.gameState.getPlayers().get(0).getPenguins().get(0);
        Point newPos = null;
        this.gameState.placePenguin(
                this.gameState.getPlayers().get(0),
                newPos);
    }

    @Test (expected = IllegalArgumentException.class)
    public void placePenguinOnPenguin() {
        init();

        IPenguin penguin1 = this.gameState.getPlayers().get(0).getPenguins().get(0);
        Point newPos = new Point(0,0);
        this.gameState.placePenguin(
                this.gameState.getPlayers().get(0),
                this.gameState.getTile(newPos));


        IPenguin differentPenguin = this.gameState.getPlayers().get(0).getPenguins().get(1);

        this.gameState.placePenguin(
                this.gameState.getPlayers().get(0),
                this.gameState.getTile(newPos));
    }

    @Test (expected = IllegalArgumentException.class)
    public void placePenguinAlreadyOnBoard() {
        init();

        IPenguin penguin1 = this.gameState.getPlayers().get(0).getPenguins().get(0);
        Point newPos = new Point(0,0);
        this.gameState.placePenguin(
                this.gameState.getPlayers().get(0),
                this.gameState.getTile(newPos));

        Point newPos2 = new Point(2,2);
        this.gameState.placePenguin(
                this.gameState.getPlayers().get(0),
                this.gameState.getTile(newPos2));
    }

    /**
     * Tests for move method. Only a placed penguin should be able to be moved.
     */
    @Test
    public void moveWorking() {
        init2();

        IPlayer player = this.gameState.playerTurn();
        IPenguin penguin = player.getPenguins().get(3);
        Point pos = penguin.getPosition();
        assertEquals(new Point(0,4), pos);


        this.gameState.move(player, penguin, new Point(0,5), false);

        assertEquals(new Point(0,5), penguin.getPosition());
    }

    /**
     * You should not be able to move to a point where another penguin is.
     */
    @Test (expected = IllegalArgumentException.class)
    public void moveToOtherPenguin() {
        init2();

        IPenguin penguin = this.gameState.playerTurn().getPenguins().get(0);

        IPlayer player = this.gameState.playerTurn();
        if (this.gameState.getPossibleMoves(this.gameState.playerTurn()).isEmpty()) {
            System.out.println("can't make it door stuck");
        } else {
            this.gameState.move(player, penguin, new Point(2,2), false);
        }
    }

    /**
     * You should not be able to move to a point that is empty
     */
    @Test (expected = IllegalArgumentException.class)
    public void moveToEmpty() {
        init2();

        IPenguin penguin = this.gameState.playerTurn().getPenguins().get(0);

        IPlayer player = this.gameState.playerTurn();

        this.gameState.move(player, penguin, new Point(0,2), false);

        this.gameState.move(player, penguin, new Point(0,0), false);
    }

    /**
     * If you pass on your turn, you should still increment the Turn without moving.
     */
    @Test
    public void movePass() {
        init2();

        IPenguin penguin = this.gameState.playerTurn().getPenguins().get(0);
        IPlayer player = this.gameState.playerTurn();
        Tile tile = new EmptyTile(1,1);

        this.gameState.move(player, penguin, tile, true);

        //Turn still increments
        assertNotEquals(player, this.gameState.playerTurn());
    }


    /**
     * Game Over tests
     */
    @Test
    public void isGameOverNormalBoard() {
        init2();
        boolean gameOver = this.gameState.isGameOver();

        assertFalse(gameOver);
    }

    //Deprecated Test : Board can never be fully empty.
    public void isGameOverBoardEmpty() {
        init2();
        boolean gameOver = this.emptyState.isGameOver();

        assertTrue(gameOver);
    }

    @Test
    public void isGameOverHolesAroundPenguins() {
        IGameState gameState = getHolesAroundPenguin();

        assertTrue(gameState.isGameOver());
    }

    @Test
    public void penguinsMovedALittle() {
        init2();

        IPlayer player = this.gameState.playerTurn();
        IPenguin penguin = this.gameState.playerTurn().getPenguins().get(3);

        this.gameState.move(player, penguin, new Point(0,5), false);

        assertFalse(this.gameState.isGameOver());
    }

    /**
     * Two of the Penguins are blocked by the other
     * three penguins in that column, but the game should not end.
     */
    @Test
    public void isGameOverOnePenguinCannotMove() {
        IGameState gameState = getOnePenguinCannotMove();

        assertFalse(gameState.isGameOver());
    }
}

