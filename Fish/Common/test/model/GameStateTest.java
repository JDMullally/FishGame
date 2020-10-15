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
 *
 */
public class GameStateTest {

    IGameState gameState, emptyState;
    Penguin peng1, peng2, peng3, peng4, peng5;
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

    @Test
    public void getPlayers() {
        init();
        List<IPlayer> testPlayers = this.gameState.getPlayers();

        assertEquals(this.players, testPlayers);
    }


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

    @Test
    public void playerTurn() {
        init();
        List<IPlayer> players = this.gameState.getPlayers();
        IPlayer player = players.get(this.gameState.getTurn() % players.size());

        assertEquals(player, this.gameState.playerTurn());
    }

    /**
     * Tests for Get Possible Moves
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
        init();
        placeAllPenguins();
        IPlayer player = this.gameState.getPlayers().get(turn1);

        Map<IPenguin, List<Tile>> moves= this.gameState.getPossibleMoves(player);

        List<Tile> peng1moves = moves.get(peng1);

        assertEquals(this.gameState.getViableTiles(this.peng1.getPosition()), peng1moves);
    }

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
    public void move() {
        init();
        int turn = this.gameState.getTurn();

        placeAllPenguins();

        IPenguin penguin = this.gameState.getPlayers().get(turn).getPenguins().get(turn);
        Point pos = penguin.getPosition();
        assertEquals(new Point(0,0), pos);

        IPlayer player = this.gameState.getPlayers().get(turn);


        this.gameState.move(player, penguin, new Point(0,2), false);

        assertEquals(new Point(0,2), penguin.getPosition());
    }

    /**
     * Game Over tests
     */
    @Test
    public void isGameOver1() {
        init();
        placeAllPenguins();
        boolean gameOver = this.gameState.isGameOver();

        assertEquals(false, gameOver);

    }

    @Test
    public void isGameOver2() {
        init();
        placeAllPenguins();
        boolean gameOver = this.emptyState.isGameOver();

        assertEquals(true, gameOver);

    }
}
