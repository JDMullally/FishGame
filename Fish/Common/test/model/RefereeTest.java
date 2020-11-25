package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.board.GameBoard;
import model.board.IGameBoard;
import model.games.GameAction;
import model.games.IGameAction;
import model.games.IGameResult;
import model.games.IReferee;
import model.games.PlayerAI;
import model.games.Referee;
import model.state.IGameState;
import model.state.IPlayer;
import model.state.Penguin;
import model.strategy.Strategy;
import model.testStrategies.MoveAnotherPlayerPenguin;
import model.testStrategies.MoveOutsideBoard;
import model.testStrategies.PassOwnTurnMakeAnotherPlayerCheat;
import model.testStrategies.PlaceAnotherPlayerPenguin;
import model.testStrategies.PlaceOutsideBoard;
import model.testStrategies.PlacePenguinOnAnotherPlayerPenguin;
import model.testStrategies.PlacesPenguinsMovesWrong;
import model.testStrategies.TimeoutStrategy;
import model.tree.MovePenguin;
import model.tree.PlacePenguin;
import model.tree.PlayerInterface;
import org.junit.Test;
import util.ColorUtil;

public class RefereeTest {

    private IGameBoard newBoard;
    private List<PlayerInterface> players2, players3, players4,
        oneCheater,
        oneBadMove,
        oneCheatsOneBadMove,
        oneMoveOutsideBoard,
        onePlacesPenguinOnAnotherPlayerPenguin,
        oneMoveAnotherPenguin,
        onePlaceOutsideBoard,
        onePassOwnTurnMakeAnotherPlayerCheat,
        timeoutPlayer,
        timeoutPlayerFirst,
        firstPlacesOutsideBoard;

    void init() {
        this.newBoard = new GameBoard(5,5,
            new ArrayList<>());

        PlayerInterface p1 = new PlayerAI(new Strategy(), 2);
        PlayerInterface p2 = new PlayerAI(new Strategy(), 2);
        PlayerInterface p3 = new PlayerAI(new Strategy(), 2);
        PlayerInterface p4 = new PlayerAI(new Strategy(),2 );
        PlayerInterface placeAnotherPlayerPenguin =
            new PlayerAI(new PlaceAnotherPlayerPenguin(),2);

        PlayerInterface placesPenguinsMovesWrong =
            new PlayerAI(new PlacesPenguinsMovesWrong(), 2);

        PlayerInterface moveOutsideBoard =
            new PlayerAI(new MoveOutsideBoard(), 2);

        PlayerInterface placesPenguinOneAnotherPlayerPenguin =
            new PlayerAI(new PlacePenguinOnAnotherPlayerPenguin(), 2);

        PlayerInterface moveAnotherPenguin =
            new PlayerAI(new MoveAnotherPlayerPenguin(),2);

        PlayerInterface placeOutsideBoard =
            new PlayerAI(new PlaceOutsideBoard(),2);

        PlayerInterface passOwnTurnMakeAnotherPlayerCheat =
            new PlayerAI(new PassOwnTurnMakeAnotherPlayerCheat(),2);

        PlayerInterface timeout = new PlayerAI(new TimeoutStrategy(),2);

        this.players2 = new ArrayList<>(Arrays.asList(p1, p2));
        this.players3 = new ArrayList<>(Arrays.asList(p1, p2, p3));
        this.players4 = new ArrayList<>(Arrays.asList(p1, p2, p3, p4));

        this.oneCheater =
            new ArrayList<>(Arrays.asList(p1, p2, placeAnotherPlayerPenguin));

        this.oneBadMove =
            new ArrayList<>(Arrays.asList(p1, p2, p3, placesPenguinsMovesWrong));

        this.oneCheatsOneBadMove =
            new ArrayList<>(Arrays.asList(placesPenguinsMovesWrong, placeAnotherPlayerPenguin));

        this.oneMoveOutsideBoard =
            new ArrayList<>(Arrays.asList(p1, p2, moveOutsideBoard));

        this.onePlacesPenguinOnAnotherPlayerPenguin =
            new ArrayList<>(Arrays.asList(p1, p2, placesPenguinOneAnotherPlayerPenguin));

        this.oneMoveAnotherPenguin = new ArrayList<>(Arrays.asList(p1, p2, moveAnotherPenguin));

        this.onePlaceOutsideBoard = new ArrayList<>(Arrays.asList(p1, p2, placeOutsideBoard));

        this.firstPlacesOutsideBoard = new ArrayList<>(Arrays.asList(p1, p2, placeOutsideBoard));

        this.onePassOwnTurnMakeAnotherPlayerCheat =
            new ArrayList<>(Arrays.asList(p1, p2, passOwnTurnMakeAnotherPlayerCheat));

        this.timeoutPlayer = new ArrayList<>(Arrays.asList(p1, p2,
            timeout));

        this.timeoutPlayerFirst = new ArrayList<>(Arrays.asList(timeout, p1, p2));
    }

    /**
     *********************************************************************************************
     * Tests for createInitialGame
     *********************************************************************************************
     */

    /**
     * Referee shouldn't be able to create a game that's not playable (without enough tiles)
     */
    @Test (expected = IllegalArgumentException.class)
    public void createInitialGameFourPlayers2x3NotEnoughTiles() {
        this.init();

        IReferee ref = new Referee(players4, 2, 3);
    }

    @Test
    public void createInitialGameFourPlayers4x3GameReadyGameOver() {
        this.init();

        IReferee ref = new Referee(players4,4,3);

        IGameState initialGameState = ref.getGameState();

        assertFalse(initialGameState.isGameReady());
        assertFalse(initialGameState.isGameOver());
    }

    @Test
    public void createInitialGameFourPlayers4x3ConfirmDimensions() {
        this.init();

        IReferee ref = new Referee(players4,4,3);

        IGameState initialGameState = ref.getGameState();

        assertEquals(4, initialGameState.getRows());
        assertEquals(3, initialGameState.getColumns());
    }

    @Test
    public void createInitialGameFourPlayers4x3ConfirmPlayerColorsOrder() {
        this.init();

        Color red = ColorUtil.toColor("red");
        Color white = ColorUtil.toColor("white");
        Color brown = ColorUtil.toColor("brown");
        Color black = ColorUtil.toColor("black");

        IReferee ref = new Referee(players4, 4, 3);

        IGameState initialGameState = ref.getGameState();

        List<IPlayer> players = initialGameState.getPlayers();

        assertEquals(red, players.get(0).getColor());
        assertEquals(white, players.get(1).getColor());
        assertEquals(brown, players.get(2).getColor());
        assertEquals(black, players.get(3).getColor());
    }

    /**
     *********************************************************************************************
     * Tests for runPlacementTurn
     *********************************************************************************************
     */

    @Test
    public void runOnePlacementTurnCheater() {
        this.init();
        Referee ref = new Referee(this.firstPlacesOutsideBoard, 3, 3);

        ref.placementTurn();

        assertEquals(3, ref.getGameState().getPlayers().size());
        PlayerInterface cheater = this.firstPlacesOutsideBoard.get(0);
        assertFalse(ref.getGameState().getPlayers().contains(cheater));
    }

    @Test
    public void runOneValidPlacementTurn() {
        this.init();
        Referee ref = new Referee(this.players3, 4, 4);

        IPlayer firstPlayer = ref.getGameState().playerTurn();

        ref.placementTurn();

        assertEquals(1, ref.getOngoingActions().size());

        GameAction placeAction = new GameAction(new PlacePenguin(firstPlayer, new Point(0, 0)));
        assertEquals(placeAction, ref.getOngoingActions().get(0));
    }

    @Test
    public void runTimeoutPlacementTurn() {
        this.init();
        Referee ref = new Referee(this.timeoutPlayerFirst, 3, 5);

        ref.placementTurn();

        assertEquals(2, ref.getGameState().getPlayers().size());
        PlayerInterface cheater = this.timeoutPlayerFirst.get(0);
        assertFalse(ref.getGameState().getPlayers().contains(cheater));
    }

    /**
     *********************************************************************************************
     * Tests for runTurn
     *********************************************************************************************
     */

    @Test
    public void runOneValidMovementTurn() {
        this.init();
        Referee ref = new Referee(this.players3, 4, 4, 4);

        IPlayer firstPlayer = ref.getGameState().playerTurn();

        while (!ref.getGameState().isGameReady()) {
            ref.placementTurn();
        }

        assertEquals(9, ref.getOngoingActions().size());

        ref.runTurn();
        assertEquals(10, ref.getOngoingActions().size());

        GameAction placeAction = new GameAction(
            new MovePenguin(firstPlayer, new Penguin(firstPlayer.getColor(), new Point(3, 0)),
                new Point(3, 2)));
        assertEquals(placeAction, ref.getOngoingActions().get(9));
    }

    @Test
    public void runThreeMovementTurnCheater() {
        this.init();
        Referee ref = new Referee(this.oneMoveOutsideBoard, 4, 5, 3);

        while (!ref.getGameState().isGameReady()) {
            ref.placementTurn();
        }

        assertEquals(3, ref.getGameState().getPlayers().size());

        ref.runTurn();
        assertEquals(3, ref.getGameState().getPlayers().size());

        ref.runTurn();
        assertEquals(3, ref.getGameState().getPlayers().size());

        ref.runTurn();
        assertEquals(2, ref.getGameState().getPlayers().size());
        PlayerInterface cheater = this.oneMoveOutsideBoard.get(2);
        assertFalse(ref.getGameState().getPlayers().contains(cheater));
    }

    /**
     *********************************************************************************************
     * Tests for runGame
     *********************************************************************************************
     */

    /**
     * We should be able to run a standard game for any number of players.
     */
    @Test
    public void runStandardGameTwoPlayers() {
        this.init();

        IReferee ref = new Referee(players2, this.newBoard.getRows(), this.newBoard.getColumns());

        IGameResult result = ref.runGame();

        assertEquals(2, result.getPlayerPlacements().size());
        assertEquals(0, result.getCheaters().size());
    }

    @Test
    public void runStandardGameThreePlayers() {
        this.init();

        IReferee ref = new Referee(players3, this.newBoard.getRows(), this.newBoard.getColumns());

        IGameResult result = ref.runGame();

        assertEquals(3, result.getPlayerPlacements().size());
        assertEquals(0, result.getCheaters().size());
    }

    @Test
    public void runStandardGameFourPlayers() {
        this.init();

        IReferee ref = new Referee(players4, this.newBoard.getRows(), this.newBoard.getColumns());

        IGameResult result = ref.runGame();

        assertEquals(4, result.getPlayerPlacements().size());
        assertEquals(0, result.getCheaters().size());
    }

    /**
     * The following tests for runGame have the Player incorrectly place a Penguin.
     *
     * When a player cheats, they will be removed from the game.  We have one player that has chosen
     * a strategy that asserts that they will cheat.  They should be removed from the game as soon
     * as the Referee calls on them.
     */
    @Test
    public void runGameThreePlayersOneCheater() {
        this.init();

        IReferee ref = new Referee(oneCheater, this.newBoard.getRows(), this.newBoard.getColumns());

        IGameResult result = ref.runGame();


        assertTrue(ref.getGameState().isGameOver());
        assertEquals(2, result.getPlayerPlacements().size());
        assertEquals(1, result.getCheaters().size());
    }

    @Test
    public void runGameThreePlayersOnePlacesPenguinOutsideBoard() {
        this.init();

        IReferee ref = new Referee(onePlaceOutsideBoard, this.newBoard.getRows(),
            this.newBoard.getColumns());

        IGameResult result = ref.runGame();

        assertTrue(ref.getGameState().isGameOver());
        assertEquals(2, result.getPlayerPlacements().size());
        assertEquals(1, result.getCheaters().size());
    }

    @Test
    public void runGameThreePlayersOnePlacesPenguinOnAnotherPlayerPenguin() {
        this.init();

        IReferee ref = new Referee(onePlacesPenguinOnAnotherPlayerPenguin,
            this.newBoard.getRows(), this.newBoard.getColumns());

        IGameResult result = ref.runGame();


        assertTrue(ref.getGameState().isGameOver());
        assertEquals(2, result.getPlayerPlacements().size());
        assertEquals(1, result.getCheaters().size());
    }



    /**
     * The following tests for runGame have the Player place Penguins correctly, but does not move a penguin
     * in a valid manner.
     *
     * When a player cheats after the placement round, they will have their penguins removed.  This
     * test is designed to check if the game breaks if a player's penguins are removed from the game.
     */
    @Test
    public void runGameFourPlayersOneCheatsAfterPlacementPenguinsMovesWrong() {
        this.init();

        IReferee ref = new Referee(oneBadMove, this.newBoard.getRows(), this.newBoard.getColumns());

        IGameResult result = ref.runGame();

        assertTrue(ref.getGameState().isGameOver());
        assertEquals(3, result.getPlayerPlacements().size());
        assertEquals(1, result.getCheaters().size());
    }

    @Test
    public void runGameThreePlayersMoveOutsideBoard() {
        this.init();

        IReferee ref = new Referee(this.oneMoveOutsideBoard, this.newBoard.getRows(),
            this.newBoard.getColumns());

        IGameResult result = ref.runGame();

        assertTrue(ref.getGameState().isGameOver());
        assertEquals(2, result.getPlayerPlacements().size());
        assertEquals(1, result.getCheaters().size());
    }

    @Test
    public void runGameThreePlayersOnePassAnotherPlayerTurn() {
        this.init();

        IReferee ref = new Referee(this.oneMoveAnotherPenguin,
            this.newBoard.getRows(), this.newBoard.getColumns());

        IGameResult result = ref.runGame();


        assertTrue(ref.getGameState().isGameOver());
        assertEquals(2, result.getPlayerPlacements().size());
        assertEquals(1, result.getCheaters().size());
    }

    @Test
    public void runGameThreePlayersOnePassAndMoveAnotherPenguin() {
        this.init();

        IReferee ref = new Referee(this.onePassOwnTurnMakeAnotherPlayerCheat,
            this.newBoard.getRows(), this.newBoard.getColumns());

        IGameResult result = ref.runGame();


        List<IGameAction> actions = ref.getOngoingActions();

        //actions.stream().forEach(action -> 1.println(action));

        assertTrue(ref.getGameState().isGameOver());
        assertEquals(2, result.getPlayerPlacements().size());
        assertEquals(1, result.getCheaters().size());
    }

    /**
     * If all players cheat, the game will be declared over and the two players will be returned as
     * cheaters.
     */

    @Test
    public void allCheatersGameEnds() {
        this.init();

        IReferee ref = new Referee(oneCheatsOneBadMove,
            this.newBoard.getRows(), this.newBoard.getColumns());

        IGameResult result = ref.runGame();


        assertTrue(ref.getGameState().isGameOver());
        assertEquals(0, result.getPlayerPlacements().size());
        assertEquals(2, result.getCheaters().size());

    }

    /**
     * Test for a player timing out during their placement.
     */
    @Test
    public void timeoutTest()  {
        this.init();

        IReferee ref = new Referee(timeoutPlayer,
            this.newBoard.getRows(), this.newBoard.getColumns());

        IGameResult result = ref.runGame();


        List<IGameAction> actions = ref.getOngoingActions();
    }



    /**********************************************************************************************
     * Tests for GetGameResult.
     *********************************************************************************************
     */
    @Test (expected = IllegalStateException.class)
    public void getGameResultBeforeGame() {
        this.init();
        IReferee ref = new Referee(players2, this.newBoard.getRows(), this.newBoard.getColumns());

        ref.getGameResult();
    }

    @Test
    public void getGameResultAfterGame() {
        this.init();
        IReferee ref = new Referee(players2, this.newBoard.getRows(), this.newBoard.getColumns());

        IGameResult result = ref.runGame();

        IGameResult result1 = ref.getGameResult();

        assertEquals(result, result1);
    }

    /**
     *********************************************************************************************
     * Tests for getGameState.
     *********************************************************************************************
     */

    @Test
    public void getGameStateAfterGame() {
        this.init();
        IReferee ref = new Referee(players2, this.newBoard.getRows(), this.newBoard.getColumns());

        IGameResult result = ref.runGame();

        IGameState finalGameState = ref.getGameState();

        assertTrue(finalGameState.isGameOver());
    }

    /**
     *********************************************************************************************
     * Tests for getOngoingActions.
     *********************************************************************************************
     */
    @Test
    public void getOngoingActionsBeforeGame() {
        this.init();
        IReferee ref = new Referee(players2, this.newBoard.getRows(), this.newBoard.getColumns());

        assertTrue(ref.getOngoingActions().isEmpty());
    }

    @Test
    public void getOngoingActionsAfterGame() {
        this.init();
        IReferee ref = new Referee(players2, this.newBoard.getRows(), this.newBoard.getColumns());

        assertTrue(ref.getOngoingActions().isEmpty());
    }
}
