package model;



import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests all public methods for the GameState Class
 */
public class GameStateTest {

    IGameState gameState, emptyState;
    Penguin peng1, peng2, peng3, peng4, peng5, peng6, peng7, peng8;
    List<IPlayer> players;
    Player p1;
    int turn1;

    private void init() {
        this.peng1 = new Penguin(Color.WHITE, null);
        this.peng2 = new Penguin(Color.WHITE,null);
        this.peng3 = new Penguin(Color.WHITE, null);
        this.peng4 = new Penguin(Color.WHITE, null);
        this.peng5 = new Penguin(Color.WHITE, null);

         List<IPenguin> penguinList1 = Arrays.asList(peng1, peng2, peng3, peng4, peng5);

        this.p1 = new Player(Color.WHITE,20, penguinList1);

        this.players = Arrays.asList(p1);

        this.gameState = new GameState(5,5, new ArrayList<>(), 0, 2, this.players);
        this.emptyState = new GameState(5,5, this.getFullHoleList(5,5), 0, 2, this.players);
        this.turn1 = this.gameState.getTurn();
    }

    /**
     * Gets a gameState where there is one penguin that can't move, but the rest can.
     */
    private IGameState getOnePenguinCannotMove() {
        this.peng1 = new Penguin(Color.WHITE, null);
        this.peng2 = new Penguin(Color.WHITE,null);
        this.peng3 = new Penguin(Color.WHITE, null);
        this.peng4 = new Penguin(Color.WHITE, null);
        this.peng5 = new Penguin(Color.WHITE, null);

        List<IPenguin> penguinList1 = Arrays.asList(peng1, peng2, peng3, peng4, peng5);

        this.p1 = new Player(Color.WHITE,20, penguinList1);

        this.players = Arrays.asList(p1);

        IGameState state = new GameState(5,5,
            new ArrayList<>(), 0, 0, this.players);

        for (IPlayer player : state.getPlayers()) {
            for (int i = 0; i < player.getPenguins().size(); i++)
                state.placePenguin(player.getPenguins().get(i),
                    player, state.getTile(new Point(0, i)));
        }

        return state;
    }

    /**
     * Gets a gameState where there are holes all around the penguins so they cannot move
     * @return IGameState
     */
    private IGameState getHolesAroundPenguin() {
        this.peng1 = new Penguin(Color.WHITE, null);
        this.peng2 = new Penguin(Color.WHITE,null);
        this.peng3 = new Penguin(Color.WHITE, null);
        this.peng4 = new Penguin(Color.WHITE, null);
        this.peng5 = new Penguin(Color.WHITE, null);

        List<IPenguin> penguinList1 = Arrays.asList(peng1, peng2, peng3, peng4, peng5);

        this.p1 = new Player(Color.WHITE,20, penguinList1);

        this.players = Arrays.asList(p1);

        List<Point> holes = Arrays.asList(new Point(1,0), new Point(1,1), new Point(1,2), new Point(1,3), new Point(1,4));

        IGameState state = new GameState(5,5, holes, 0, 0, this.players);

        for (IPlayer player : state.getPlayers()) {
            for (int i = 0; i < player.getPenguins().size(); i++)
                state.placePenguin(player.getPenguins().get(i),
                    player, state.getTile(new Point(0, i)));
        }

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

    /**
     * Places penguins diagonally along the board.
     */
    private void placeAllPenguins() {
        init();
        for (IPlayer player : this.gameState.getPlayers()) {
            for (int i = 0; i < player.getPenguins().size(); i++)
                this.gameState.placePenguin(player.getPenguins().get(i),
                    player, this.gameState.getTile(new Point(i, i)));
        }
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
        this.peng1 = new Penguin(Color.WHITE, null);
        this.peng2 = new Penguin(Color.WHITE,null);
        this.peng3 = new Penguin(Color.WHITE, null);
        this.peng4 = new Penguin(Color.WHITE, null);
        this.peng5 = new Penguin(Color.WHITE, null);
        List<IPenguin> penguinList1 = Arrays.asList(peng1, peng2, peng3, peng4, peng5);

        IPlayer player1 = new Player(Color.BLACK, 4, penguinList1);

        List<IPlayer> players = Arrays.asList(player1);
        this.gameState = new GameState(5,5,
            new ArrayList<>(), 2,2, players);
    }


    @Test (expected = IllegalArgumentException.class)
    public void gameStateConstructorTwoPlayersSameColor() {
        this.peng1 = new Penguin(Color.WHITE, null);
        this.peng2 = new Penguin(Color.WHITE, null);
        this.peng3 = new Penguin(Color.WHITE, null);
        this.peng4 = new Penguin(Color.WHITE, null);

        List<IPenguin> penguinList1 = Arrays.asList(peng1, peng2, peng3, peng4);

        this.peng5 = new Penguin(Color.WHITE, null);
        this.peng6 = new Penguin(Color.WHITE, null);
        this.peng7 = new Penguin(Color.WHITE, null);
        this.peng8 = new Penguin(Color.WHITE, null);

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
     * Tests for getTurn
     */
    @Test
    public void getTurn() {
        init();
        int turn = this.gameState.getTurn();
        assertEquals(0, turn);

        placeAllPenguins();

        IPlayer player = this.gameState.getPlayers().get(turn);

        IPenguin penguin = player.getPenguins().get(0);

        this.gameState.move(player, penguin, new Point(0,2), false);

        turn = this.gameState.getTurn();

        assertEquals(1, turn);
    }

    /**
     * Tests for playerTurn
     */
    @Test
    public void playerTurn() {
        init();
        List<IPlayer> players = this.gameState.getPlayers();
        IPlayer player = players.get(this.gameState.getTurn() % players.size());

        assertEquals(player, this.gameState.playerTurn());
    }

    /**
     * Tests for getPossibleMoves
     */
    @Test (expected = NullPointerException.class)
    public void getPossibleMovesNull() {
        init();

        IPlayer player = this.gameState.getPlayers().get(turn1);

        Map<IPenguin, List<Tile>> moves= this.gameState.getPossibleMoves(player);

        List<Tile> peng1moves = moves.get(peng1);

        assertEquals(this.gameState.getViableTiles(this.peng1.getPosition()), peng1moves);
    }

    @Test
    public void getPossibleMoves() {
        placeAllPenguins();
        IPlayer player = this.gameState.getPlayers().get(turn1);

        Map<IPenguin, List<Tile>> moves= this.gameState.getPossibleMoves(player);

        List<Tile> peng1moves = moves.get(peng1);

        assertEquals(this.gameState.getViableTiles(this.peng1.getPosition()), peng1moves);
    }

    /**
     * Tests for addPlayer
     */
    @Test
    public void addPlayer() {
        init();
        IGameState testBoard = new GameState(5,5, new ArrayList<>(), 0, 2);
        testBoard.addPlayer(this.p1);
        IPlayer addedPlayer = testBoard.getPlayers().get(0);

        assertEquals(addedPlayer, this.gameState.getPlayers().get(0));
    }

    @Test (expected = IllegalArgumentException.class)
    public void addPlayerNull() {
        init();

        this.gameState.addPlayer(null);
    }

    /**
     * place Penguin methods.
     * A Penguin should always be able to be placed during placement rounds,
     * especially if it's current position is null;
     */
    @Test
    public void placePenguin() {
        init();

        IPenguin penguin1 = this.gameState.getPlayers().get(turn1).getPenguins().get(turn1);

        assertNull(penguin1.getPosition());

        Point newPos = new Point(0,0);
        this.gameState.placePenguin(penguin1,
            this.gameState.getPlayers().get(turn1),
            this.gameState.getTile(newPos));

        assertEquals(newPos, penguin1.getPosition());

    }

    @Test (expected = IllegalArgumentException.class)
    public void placePenguinNullPenguin() {
        init();

        Point newPos = new Point(0,0);
        this.gameState.placePenguin(null,
            this.gameState.getPlayers().get(turn1),
            this.gameState.getTile(newPos));
    }

    @Test (expected = IllegalArgumentException.class)
    public void placePenguinNullPlayer() {
        init();

        IPenguin penguin1 = this.gameState.getPlayers().get(turn1).getPenguins().get(turn1);
        Point newPos = new Point(0,0);
        this.gameState.placePenguin(penguin1,
            null,
            this.gameState.getTile(newPos));
    }

    @Test (expected = IllegalArgumentException.class)
    public void placePenguinNullTile() {
        init();

        IPenguin penguin1 = this.gameState.getPlayers().get(turn1).getPenguins().get(turn1);
        Point newPos = new Point(0,0);
        this.gameState.placePenguin(penguin1,
            this.gameState.getPlayers().get(turn1),
            null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void placePenguinOnPenguin() {
        init();

        IPenguin penguin1 = this.gameState.getPlayers().get(turn1).getPenguins().get(0);
        Point newPos = new Point(0,0);
        this.gameState.placePenguin(penguin1,
            this.gameState.getPlayers().get(turn1),
            this.gameState.getTile(newPos));


        IPenguin differentPenguin = this.gameState.getPlayers().get(turn1).getPenguins().get(1);

        this.gameState.placePenguin(differentPenguin,
            this.gameState.getPlayers().get(turn1),
            this.gameState.getTile(newPos));
    }

    @Test (expected = IllegalArgumentException.class)
    public void placePenguinAlreadyOnBoard() {
        init();

        IPenguin penguin1 = this.gameState.getPlayers().get(turn1).getPenguins().get(0);
        Point newPos = new Point(0,0);
        this.gameState.placePenguin(penguin1,
            this.gameState.getPlayers().get(turn1),
            this.gameState.getTile(newPos));

        Point newPos2 = new Point(2,2);
        this.gameState.placePenguin(penguin1,
            this.gameState.getPlayers().get(turn1),
            this.gameState.getTile(newPos2));
    }

    /**
     * Tests for move method. Only a placed penguin should be able to be moved.
     */
    @Test
    public void moveWorking() {
        placeAllPenguins();

        int turn = this.gameState.getTurn();

        IPenguin penguin = this.gameState.getPlayers().get(turn).getPenguins().get(turn);
        Point pos = penguin.getPosition();
        assertEquals(new Point(0,0), pos);

        IPlayer player = this.gameState.getPlayers().get(turn);


        this.gameState.move(player, penguin, new Point(0,2), false);

        assertEquals(new Point(0,2), penguin.getPosition());
    }

    /**
     * You should not be able to move to a point where another penguin is.
     */
    @Test (expected = IllegalArgumentException.class)
    public void moveToOtherPenguin() {
        placeAllPenguins();

        int turn = this.gameState.getTurn();

        IPenguin penguin = this.gameState.getPlayers().get(turn).getPenguins().get(turn);

        IPlayer player = this.gameState.getPlayers().get(turn);

        this.gameState.move(player, penguin, new Point(2,2), false);
    }

    /**
     * You should not be able to move to a point that is empty
     */
    @Test (expected = IllegalArgumentException.class)
    public void moveToEmpty() {
        placeAllPenguins();

        int turn = this.gameState.getTurn();

        IPenguin penguin = this.gameState.getPlayers().get(turn).getPenguins().get(turn);

        IPlayer player = this.gameState.getPlayers().get(turn);

        this.gameState.move(player, penguin, new Point(0,2), false);

        this.gameState.move(player, penguin, new Point(0,0), false);
    }

    /**
     * If you pass on your turn, you should still increment the Turn without moving.
     */
    @Test
    public void movePass() {
        placeAllPenguins();

        int turn = this.gameState.getTurn();

        IPenguin penguin = this.gameState.getPlayers().get(turn).getPenguins().get(turn);

        Point testPos = new Point(0,0);
        Point pos = penguin.getPosition();
        assertEquals(testPos, pos);

        IPlayer player = this.gameState.getPlayers().get(turn);

        Tile tile = new EmptyTile(1,1);

        this.gameState.move(player, penguin, tile, true);

        assertEquals(testPos, penguin.getPosition());

        //Turn still increments
        assertEquals(1, this.gameState.getTurn());
    }


    /**
     * Game Over tests
     */
    @Test
    public void isGameOverNormalBoard() {
        placeAllPenguins();
        boolean gameOver = this.gameState.isGameOver();

        assertFalse(gameOver);
    }

    @Test
    public void isGameOverBoardEmpty() {
        placeAllPenguins();
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
        placeAllPenguins();

        IPlayer player = this.gameState.getPlayers().get(this.turn1);
        IPenguin penguin = player.getPenguins().get(this.turn1);

        this.gameState.move(player, penguin, new Point(0,2), false);

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
