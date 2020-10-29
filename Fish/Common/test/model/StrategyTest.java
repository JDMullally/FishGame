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
import model.tree.PlacePenguin;
import org.junit.Test;
import sun.print.PSPrinterJob.EPSPrinter;

import static org.junit.Assert.*;


public class StrategyTest {

    private IPlayer player1, player2;
    private List<IPlayer> players;
    private IGameState gameState, gameStateMinimax,
        gameStateMinimax2, gameStateMinimax3, gameStateMinimax4;

    private void init() {
        this.player1 = new Player(Color.BLACK, 14, new ArrayList<>());
        this.player2 = new Player(Color.WHITE, 15, new ArrayList<>());

        this.players = Arrays.asList(player1, player2);

        this.gameState = new GameState(7,7, new ArrayList<>(), 0, 2, this.players);

        this.gameStateMinimax = new GameState(4, 4, this.craftedBoard(), this.players);

        this.gameStateMinimax2 = new GameState(3,4, this.minimaxBoard(), this.players);

        this.gameStateMinimax3 = new GameState(4,4, this.noMoreMovesBoard(), this.players);

        this.gameStateMinimax4 = new GameState(3,4, this.onlyBlackCanMove(), this.players);
    }

    private Tile[][] craftedBoard() {
        Tile[][] board = new Tile[4][4];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new FishTile(i, j, j + 1);
            }
        }
        return board;
    }

    private Tile[][] minimaxBoard() {
        Tile[][] board = new Tile[4][3];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new FishTile(i, j, j + 1);
            }
        }
        return board;
    }

    private Tile[][] noMoreMovesBoard() {
        Tile[][] board = new Tile[4][4];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new FishTile(i, j, j + 1);
                if (j > 1) {
                    board[i][j] = new EmptyTile(i,j);
                }
            }
        }
        return board;
    }

    private Tile[][] onlyBlackCanMove() {
        Tile[][] board = new Tile[4][3];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = new FishTile(i, j, j + 1);
                if (j > 1 && i > 0) {
                    board[i][j] = new EmptyTile(i,j);
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
     * Tests for chooseMoveAction that tests if a Player uses minimax to move their penguins
     * to the correct place.
     *
     * Here is an example of the minimax game we have set up for the test.
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

        this.gameStateMinimax = this.placeAllPenguinsState(strategy, this.gameStateMinimax);
        Action action;
        for (int i = 0; i < 5; i++) {
            action = strategy.chooseMoveAction(this.gameStateMinimax, 3);

            this.gameStateMinimax = action.apply(this.gameStateMinimax);
        }

        boolean blackScoresHigherThanWhite = this.gameStateMinimax.getPlayers().get(1).getScore() -
            this.gameStateMinimax.playerTurn().getScore() > 0;

        assertTrue(blackScoresHigherThanWhite);
    }

    /**
     * During the first move, the first player would plan to maximize their gain while minimizing
     * their opponent's potential gain.  If two moves give equivalent gain, the algorithm should move
     * the penguin with the lowest row and column number.
     *
     * In the example below, the expected move of the first player (Black) should be to move from
     * (0,1) --> (1,2) as it would provide the most gain and minimize the opponent's gain.
     * Since this is the first move, it would provide identical gain as the moves from
     * (0,1) --> (0, 2), (2, 1) --> (3, 2), (2, 1) -> (2, 2) but it should be closer to left
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

        this.gameStateMinimax2 = this.placeAllPenguinsState(strategy, this.gameStateMinimax2);

        IPlayer currentPlayer = this.gameStateMinimax2.playerTurn();
        IPenguin penguinAt01 = this.gameStateMinimax2.playerTurn().getPenguins().get(2);

        Action action;

        Action test = new MovePenguin(currentPlayer, penguinAt01, new Point(1,2), false);

        action = strategy.chooseMoveAction(this.gameStateMinimax2, 3);

        assertEquals(test, action);
    }

    /**
     * If there are no more places for this Player to move their Penguin, they will return a
     * MovePenguin that has them pass their turn.
     */
    @Test
    public void MinMaxNoMoreMoves() {
        init();
        IStrategy strategy = new Strategy();

        this.gameStateMinimax3 = this.placeAllPenguinsState(strategy, this.gameStateMinimax3);

        Action action;

        action = strategy.chooseMoveAction(this.gameStateMinimax3, 5);

        Action dummyPass = new MovePenguin(this.gameStateMinimax3.playerTurn(),
            this.gameStateMinimax3.playerTurn().getPenguins().get(0),
            this.gameStateMinimax3.playerTurn().getPenguins().get(0).getPosition(), true);

        assertEquals(dummyPass,action);

    }

    /**
     * If a player cannot move, they should return a special MovePenguin Object with a pass flag.
     * This should only occur if that person has no possible moves.  If the other player can make
     * a move, they should still be able to move.
     */
    @Test
    public void OnePlayerExactlyCanGoButItIsNotTheirTurn() {
        init();
        IStrategy strategy = new Strategy();

        this.gameStateMinimax4 = this.placeAllPenguinsState(strategy, this.gameStateMinimax4);

        //First we need the game in a state where it is White's turn, but they have no moves,
        // so we first skip Black's turn.

        assertEquals(this.gameStateMinimax4.playerTurn().getColor(), new Color(0,0,0));

        this.gameStateMinimax4 = this.gameStateMinimax4.move(this.gameStateMinimax4.playerTurn(),
            this.gameStateMinimax4.getPlayers().get(1).getPenguins().get(0), new Point(100,100), true);

        IPlayer whitePlayer = this.gameStateMinimax4.playerTurn();

        Action pass = strategy.chooseMoveAction(this.gameStateMinimax4, 5);

        this.gameStateMinimax4 = pass.apply(this.gameStateMinimax4);

        IPlayer whitePlayerAfterTurn = this.gameStateMinimax4.getPlayers().get(players.size() - 1);

        //no change in score indicates that a pass occured.
        assertEquals(new MovePenguin(whitePlayer, whitePlayer.getPenguins().get(0),
            whitePlayer.getPenguins().get(0).getPosition(), true), pass);

        Action lastAction = strategy.chooseMoveAction(this.gameStateMinimax4, 5);

        IPenguin penguin = this.gameStateMinimax4.playerTurn().getPenguins().get(0);

       this.gameStateMinimax4 = lastAction.apply(this.gameStateMinimax4);

       assertTrue(this.gameStateMinimax4.isGameOver());

        //System.out.println(this.gameStateMinimax4);

       IPlayer playerWhoMoved = this.gameStateMinimax4.getPlayers().get(1);

       Action expectedMove = new MovePenguin(playerWhoMoved, penguin,
           new Point(0,2), false);

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

        this.gameStateMinimax4 = this.placeAllPenguinsState(strategy, this.gameStateMinimax4);

        this.gameStateMinimax4 = this.gameStateMinimax4.move(this.gameStateMinimax4.getPlayers().get(1),
            this.gameStateMinimax4.getPlayers().get(1).getPenguins().get(0),
            new Point(100,100), true);
    }

}
