package model;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.board.EmptyTile;
import model.board.FishTile;
import model.board.Tile;
import model.state.GameState;
import model.state.IGameState;
import model.state.IPenguin;
import model.state.IPlayer;
import model.state.Player;
import model.strategy.IStrategy;
import model.strategy.Strategy;
import model.tree.Action;
import model.tree.MovePenguin;
import model.tree.PassPenguin;
import model.tree.PlacePenguin;
import org.junit.Test;

import static org.junit.Assert.*;


public class StrategyTest {

    private IPlayer player1, player2;
    private List<IPlayer> players;
    private IGameState gameState, gameStateMiniMax,
        gameStateMiniMax2, gameStateMiniMax3, gameStateMiniMax4,
        gameStateHoles;

    private void init() {
        this.player1 = new Player(Color.BLACK, 14, new ArrayList<>());
        this.player2 = new Player(Color.WHITE, 15, new ArrayList<>());

        this.players = Arrays.asList(player1, player2);

        this.gameState = new GameState(7,7, new ArrayList<>(), 0, 2, this.players);

        this.gameStateMiniMax = new GameState(4, 4, this.craftedBoard(), this.players);

        this.gameStateMiniMax2 = new GameState(3,4, this.MiniMaxBoard(), this.players);

        this.gameStateMiniMax3 = new GameState(4,4, this.noMoreMovesBoard(), this.players);

        this.gameStateMiniMax4 = new GameState(3,4, this.onlyBlackCanMove(), this.players);
    }

    private Tile[][] craftedBoard() {
        Tile[][] board = new Tile[4][4];

        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                board[y][x] = new FishTile(x, y, y + 1);
            }
        }
        return board;
    }

    private Tile[][] boardOfHoles() {
        Tile[][] board = new Tile[4][4];

        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                board[y][x] = new EmptyTile(x, y);
            }
        }
        return board;
    }

    private Tile[][] MiniMaxBoard() {
        Tile[][] board = new Tile[3][4];

        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                board[y][x] = new FishTile(x, y, y + 1);
            }
        }
        return board;
    }

    private Tile[][] noMoreMovesBoard() {
        Tile[][] board = new Tile[4][4];

        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                board[y][x] = new FishTile(x, y, y + 1);
                if (y > 1) {
                    board[y][x] = new EmptyTile(x,y);
                }
            }
        }
        return board;
    }

    private Tile[][] onlyBlackCanMove() {
        Tile[][] board = new Tile[3][4];
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                board[y][x] = new FishTile(x, y, y + 1);
                if (y > 1 && x > 0) {
                    board[y][x] = new EmptyTile(x,y);
                }
            }
        }
        return board;
    }


    private void placeNPenguins(int n) {
        for (int i = 0; i < n; i++) {
            this.gameState = new PlacePenguin(this.gameState.playerTurn(),
                new Point(i, 0)).apply(this.gameState);
        }
    }

    private void placeAllPenguins(IStrategy strategy) {
        Action placeNextPenguin;
        for (int i = 0; i < 2 * (6 - this.players.size()); i++) {
            placeNextPenguin = strategy.choosePlacePenguinAction(this.gameState);
            this.gameState = placeNextPenguin.apply(this.gameState);
        }
    }

    private IGameState placeAllPenguinsState(IStrategy strategy, IGameState state) {
        Action placeNextPenguin;
        for (int i = 0; i < 2 * (6 - this.players.size()); i++) {
            placeNextPenguin = strategy.choosePlacePenguinAction(state);
            state = placeNextPenguin.apply(state);
        }
        return state;
    }

    /**
     * Tests for choose PlacePenguinAction that Tests if a Player will place a penguin in the
     * correct place.
     */

    /**
     * Tests that the first player places their penguins in the correct place (top left corner)
     */
    @Test
    public void choosePlacePenguinActionFirstAction() {
        init();

        IStrategy strategy = new Strategy();

        Action action = strategy.choosePlacePenguinAction(this.gameState);

        assertEquals(new PlacePenguin(this.gameState.playerTurn(), new Point(0,0)), action);
    }

    /**
     * Tests that place penguin will throw an error if it fails
     */

    @Test (expected = IllegalArgumentException.class)
    public void choosePlacePenguinFail()  {
        this.gameStateHoles = new GameState(4,4, this.boardOfHoles(), this.players);

        IStrategy strategy = new Strategy();

        Action action = strategy.choosePlacePenguinAction(this.gameStateHoles);
    }

    /**
     * Tests that the player will choose the next possible space in that row.
     */
    @Test
    public void choosePlacePenguinActionThreePlacementIn() {
        init();
        this.placeNPenguins(4);

        IStrategy strategy = new Strategy();

        Action action = strategy.choosePlacePenguinAction(this.gameState);

        assertEquals(new PlacePenguin(this.gameState.playerTurn(), new Point(4,0)), action);
    }

    /**
     * If the row is filled, the player will move down in a zig zag pattern and start from the
     * left-most part of the row.
     */
    @Test
    public void choosePlacePenguinActionNextLine() {
        init();
        this.placeNPenguins(7);

        IStrategy strategy = new Strategy();

        Action action = strategy.choosePlacePenguinAction(this.gameState);

        assertEquals(new PlacePenguin(this.gameState.playerTurn(), new Point(0,1)), action);
    }

    /**
     * Tests for chooseMoveAction that tests if a Player uses MiniMax to move their penguins
     * to the correct place.
     *
     * Here is an example of the MiniMax game we have set up for the test.
     *
     * Each Tile has a number of fish equal to y + 1
     *
     *  B(0,0)  W(1, 0)   B(2, 0)      W(3, 0)            Score Per Tile: 1
     *      B(0, 1)    W(1, 1)    B(2, 1)      W(3, 1)    Score Per Tile: 2
     *  (0, 2)   (1, 2)   (2, 2)      (3, 2)              Score Per Tile: 3
     *      (0, 3)    (1, 3)    (2, 3)      (3, 3)        Score Per Tile: 4
     *
     */

    /**
     * No matter the case, if a player has played an odd number of rounds with tiles that increase
     * in value as you go down, the player that has made more moves should have a higher score.
     */
    @Test
    public void FirstPlayerShouldWinAfterOddRounds() {
        init();
        IStrategy strategy = new Strategy();

        this.gameStateMiniMax = this.placeAllPenguinsState(strategy, this.gameStateMiniMax);
        Action action;
        for (int i = 0; i < 5; i++) {
            action = strategy.chooseMoveAction(this.gameStateMiniMax, 3);

            this.gameStateMiniMax = action.apply(this.gameStateMiniMax);
        }

        boolean blackScoresHigherThanWhite = this.gameStateMiniMax.getPlayers().get(1).getScore() -
            this.gameStateMiniMax.playerTurn().getScore() > 0;

        assertTrue(blackScoresHigherThanWhite);
    }

    /**
     * During the first move, the first player would plan to maximize their gain while minimizing
     * their opponent's potential gain.  If two moves give equivalent gain, the algorithm should move
     * the penguin with the lowest row and column number.
     *
     * In the example below, the expected move of the first player (Black) should be to move from
     * (0,1) --> (0,2) as it would provide the most gain and minimize the opponent's gain.
     * Since this is the first move, it would provide identical gain as the moves from
     * (0,1) --> (1, 2), (2, 1) --> (3, 2), (2, 1) -> (2, 2) but it should be closer to left
     * side to have the lowest rows and column number.
     *
     *      *  B(0,0)     W(1, 0)     B(2, 0)      W(3, 0)             Score Per Tile: 1
     *      *        B(0, 1)     W(1, 1)     B(2, 1)      W(3, 1)      Score Per Tile: 2
     *      *  (0, 2)     (1, 2)       (2, 2)      (3, 2)              Score Per Tile: 3
     */
    @Test
    public void MinMaxingGainFirstMoveWithTieBreak() {
        init();
        IStrategy strategy = new Strategy();

        this.gameStateMiniMax2 = this.placeAllPenguinsState(strategy, this.gameStateMiniMax2);

        IPlayer currentPlayer = this.gameStateMiniMax2.playerTurn();
        IPenguin penguinAt01 = this.gameStateMiniMax2.playerTurn().getPenguins().get(2);

        Action action;

        Action test = new MovePenguin(currentPlayer, penguinAt01, new Point(1,2));

        action = strategy.chooseMoveAction(this.gameStateMiniMax2, 3);

        assertEquals(test, action);
    }

    /**
     * If a player cannot move, they should throw an Illegal State Exception stating that they cannot move.
     * This should only occur if that person has no possible moves.
     */
    @Test (expected = IllegalStateException.class)
    public void MinMaxNoMoreMoves() {
        init();
        IStrategy strategy = new Strategy();

        this.gameStateMiniMax3 = this.placeAllPenguinsState(strategy, this.gameStateMiniMax3);

        Action action;

        action = strategy.chooseMoveAction(this.gameStateMiniMax3, 5);

        Action dummyPass = new PassPenguin(this.gameStateMiniMax3.playerTurn());

        assertEquals(dummyPass,action);

    }

    /**
     * There is only one move that this player can make, so this test checks if it
     * is correctly outputted.
     */

    @Test
    public void OnePlayerExactlyCanGo() {
        init();
        IStrategy strategy = new Strategy();

        this.gameStateMiniMax4 = this.placeAllPenguinsState(strategy, this.gameStateMiniMax4);

        Action lastAction = strategy.chooseMoveAction(this.gameStateMiniMax4, 5);

        IPenguin penguin = this.gameStateMiniMax4.playerTurn().getPenguins().get(2);

       this.gameStateMiniMax4 = lastAction.apply(this.gameStateMiniMax4);

       assertTrue(this.gameStateMiniMax4.isGameOver());

       IPlayer playerWhoMoved = this.gameStateMiniMax4.getPlayers().get(1);

       Action expectedMove = new MovePenguin(playerWhoMoved, penguin, new Point(0,2));

       assertEquals(expectedMove, lastAction);
    }

    /**
     * A player should not be able to pass another player's turn. It should throw an Illegal Argument
     * Exception if a Player is caught cheating in this way.
     */
    @Test (expected = IllegalArgumentException.class)
    public void PlayerTriesToSkipAnotherPlayerTurn() {
        init();
        IStrategy strategy = new Strategy();

        this.gameStateMiniMax4 = this.placeAllPenguinsState(strategy, this.gameStateMiniMax4);

        this.gameStateMiniMax4 = this.gameStateMiniMax4.move(this.gameStateMiniMax4.getPlayers().get(1),
            this.gameStateMiniMax4.getPlayers().get(1).getPenguins().get(0),
            new Point(100,100), true);
    }

}
