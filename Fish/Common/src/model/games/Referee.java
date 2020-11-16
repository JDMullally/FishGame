package model.games;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import model.board.GameBoard;
import model.board.IGameBoard;
import model.state.GameState;
import model.state.IGameState;
import model.state.IPlayer;
import model.state.Player;
import model.tree.Action;
import model.tree.GameEnded;
import model.tree.PassPenguin;
import model.tree.PlayerCheated;
import model.tree.PlayerInterface;

/**
 * Implementation of a referee component, which can run a complete Fish game for a sequence of players.
 *
 * A referee can get the current GameState, ongoing GameActions, the outcome of a Game, and of
 * course run the game itself.
 *
 * Abnormal conditions (cheating) that the referee takes care of resulting in the removal of a
 * player include:
 * - Trying to place a penguin somewhere that isn't on the GameBoard
 * - Trying to place a penguin on another player's penguin
 * - Trying to place more penguins than the player is allowed
 * - Trying to move another player's penguin
 * - Trying to move a penguin when it isn't the player's turn
 * - Trying to move a penguin to a location that isn't on the GameBoard
 * - Trying to move a penguin to a location that is empty (a hole)
 * - Trying to move a penguin to a location already occupied by another penguin
 * - Trying to move a penguin to a location which it cannot reach
 *
 * An Abnormal condition (cheating) that the referee must take care of of resulting in the removal
 * of a player specifically for remote communication include:
 * - Taking too long to make a move (timeout)
 * - Malformed Input
 */
public class Referee implements IReferee {

    private final int timeout;
    private final Map<Color, PlayerInterface> players;
    private final Map<Color, PlayerInterface> cheaters;
    private final List<GameAction> ongoingActions;
    private IGameState gameState;
    private IGameResult gameResult;

    /**
     * Constructor for Referee that takes in a List of Players. The Referee may assume that the
     * sequence is arranged in ascending order of player age.
     *
     * @param players List of PlayerInterface
     */
    public Referee(List<PlayerInterface> players, int rows, int columns) {
        this(players, rows, columns, 0);
    }

    public Referee(List<PlayerInterface> players, int rows, int columns, int numFish) {
        if (players == null) {
            throw new IllegalArgumentException("A Referee cannot start a Game with null Players");
        } else if (players.size() <= 1 || players.size() > 4) {
            throw new IllegalArgumentException("There must be between 2 and 4 players inclusive in the game");
        }

        this.timeout = 5;
        this.players = this.initializePlayerInterfaces(players);
        this.cheaters = new LinkedHashMap<>();
        this.ongoingActions = new ArrayList<>();

        IGameBoard board = new GameBoard(rows, columns,
            new ArrayList<>(), 0, numFish);

        this.createInitialGame(players, rows, columns, board);
    }


    /**
     * Initializes players as a map of Color to PlayerInterface
     *
     * @param players List of PlayerInterface
     * @return Map of Color to PlayerInterface
     */
    private Map<Color, PlayerInterface> initializePlayerInterfaces(List<PlayerInterface> players) {
        Map<Color, PlayerInterface> mappedPlayers = new LinkedHashMap<>();
        for (int i = 0; i < players.size(); i++) {
            switch (i) {
                case 0:
                    mappedPlayers.put(Color.RED, players.get(i));
                    break;
                case 1:
                    mappedPlayers.put(Color.WHITE, players.get(i));
                    break;
                case 2:
                    mappedPlayers.put(new Color(210, 105, 30), players.get(i));
                    break;
                case 3:
                    mappedPlayers.put(Color.BLACK, players.get(i));
                    break;
                default:
                    throw new IllegalArgumentException("There should not be more than 4 players");
            }
        }

        return mappedPlayers;
    }

    /**
     * Initializes players with ages and colors based on the order in which they were provided.
     *
     * @return List of IPlayer
     */
    private List<IPlayer> initializePlayers() {
        return this.players.keySet().stream().map(color -> {
            switch (color.toString()) {
                case "java.awt.Color[r=255,g=0,b=0]":
                    return new Player(color, 1, new ArrayList<>());
                case "java.awt.Color[r=255,g=255,b=255]":
                    return new Player(color, 2, new ArrayList<>());
                case "java.awt.Color[r=210,g=105,b=30]":
                    return new Player(color, 3, new ArrayList<>( ));
                case "java.awt.Color[r=0,g=0,b=0]":
                    return new Player(color, 4, new ArrayList<>());
                default:
                    throw new IllegalArgumentException("Invalid color given");
            }
        }).collect(Collectors.toList());
    }

    @Override
    public IGameResult runGame() {

        // allows players to place penguins until the game is ready to begin
        while (!this.gameState.isGameReady()) {
            this.placementTurn();
        }

        // allows players to move penguins until the game is over
        while (!this.gameState.isGameOver()) {
            this.gameState = this.runTurn();
        }

        Action endGame = new GameEnded(this.gameState);
        this.ongoingActions.add(new GameAction(endGame));
        this.gameResult = this.retrieveGameResult();
        return this.gameResult;
    }


    /**
     * Creates an initial game state with a board, players and no penguins placed yet
     */
    public void createInitialGame(List<PlayerInterface> players, int rows, int columns, IGameBoard board) {
        // creates game board
        IGameBoard gameBoard = board;

        // create players based on their color
        List<IPlayer> gamePlayers = this.initializePlayers();

        // creates game state
        this.gameState = new GameState(rows, columns, gameBoard.getGameBoard(), gamePlayers);
    }

    // TODO make methods consistent on whether they directly mutate gamestate or return it
    /**
     * Runs a single Placement Turn and returns the new state after the move is made.
     *
     * A Placement Turn of Fish means that the current player enters a valid placement of
     * one of their penguins in designated time.
     *
     * @return IGameState
     */
    public IGameState placementTurn() {
        IPlayer curPlayer = this.gameState.playerTurn();
        PlayerInterface curPlayerInterface = this.players.get(curPlayer.getColor());

        Callable<Action> task = new Callable<Action>() {
            public Action call() throws TimeoutException {
                return curPlayerInterface.placePenguin(gameState);
            }
        };

        this.gameState = placeOrMove(task, curPlayer, curPlayerInterface);
        return this.gameState.clone();
    }

    /**
     * Returns the state of the game after running a Round of Fish.
     *
     * A Round of Fish means all non-cheating players play their turn, and if the game doesn't end
     * the current Player's turn of the returned GameState should return the first player who
     * played that round.
     *
     * @return IGameState
     */
    public IGameState runRound() {
        IGameState roundGameState = this.gameState;
        for (int i = 0; i < players.values().size(); i++) {
            if(roundGameState.isGameOver()) {
                break;
            }
            roundGameState = runTurn();
        }
        return roundGameState;
    }

    /**
     * Returns the state of the Game after a running single turn of Fish.
     *
     * A Turn of Fish means that the current player enters a valid move to move
     * one of their penguins in designated time.
     *
     * @return IGameState
     */
    public IGameState runTurn() {
        IGameState newGameState;
        IPlayer curPlayer = this.gameState.playerTurn();
        PlayerInterface curPlayerInterface = this.players.get(curPlayer.getColor());

        // if the player can't move, pass for them
        if (this.gameState.isCurrentPlayerStuck()) {
            Action action = new PassPenguin(curPlayer);
            newGameState = action.apply(this.gameState);
            this.ongoingActions.add(new GameAction(action));
            return newGameState;
        }


        Callable<Action> task = new Callable<Action>() {
            public Action call() throws TimeoutException {
                return curPlayerInterface.movePenguin(gameState);
            }
        };

        return placeOrMove(task, curPlayer, curPlayerInterface);
    }

    /**
     * Returns the GameState after either a Place or Move Action was made.
     * @param task
     * @param curPlayer
     * @param curPlayerInterface
     * @return
     */
    private IGameState placeOrMove(Callable<Action> task, IPlayer curPlayer,
        PlayerInterface curPlayerInterface) {
        IGameState newGameState;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Action> future = executor.submit(task);

        // allow the player to move based on their strategy
        try {
            Action action = future.get(this.timeout, TimeUnit.SECONDS);
            executor.shutdownNow();
            newGameState = action.apply(this.gameState);
            this.ongoingActions.add(new GameAction(action));
        } catch (Exception e) {
            executor.shutdownNow();
            newGameState = this.playerCheated(curPlayer, curPlayerInterface);
        }
        return newGameState;
    }

    /**
     * When a player cheats, they are removed from the game.
     *
     * @param curPlayer IPlayer
     * @param curPlayerInterface IPLayerInterface
     */
    private IGameState playerCheated(IPlayer curPlayer, PlayerInterface curPlayerInterface) {
        Action action = new PlayerCheated(curPlayer);
        IGameState newGameState = action.apply(this.gameState);
        this.players.remove(curPlayer.getColor());
        this.cheaters.put(curPlayer.getColor(), curPlayerInterface);
        this.ongoingActions.add(new GameAction(action));
        return newGameState;
    }

    @Override
    public IGameState getGameState() throws IllegalStateException {
        if (this.gameState == null) {
            throw new IllegalStateException("The game has not started yet");
        }

        return this.gameState;
    }

    @Override
    public List<GameAction> getOngoingActions() {
        return new ArrayList<>(this.ongoingActions);
    }

    @Override
    public IGameResult getGameResult() throws IllegalStateException {
        if (this.gameResult == null) {
            throw new IllegalStateException("The game has not concluded");
        }

        return this.gameResult;
    }

    /**
     * Creates the game result based on the scores of the remaining players and cheaters.
     *
     * @return IGameResult
     */
    private IGameResult retrieveGameResult() {
        // gets players from GameState by score in descending order

        Map<PlayerInterface, Integer> playersI = new LinkedHashMap<>();
        for (IPlayer player : this.gameState.getPlayers()) {
            playersI.put(this.players.get(player.getColor()), player.getScore());
        }

        List<PlayerInterface> cheatersI = new ArrayList<>(this.cheaters.values());

        return new GameResult(playersI, cheatersI);
    }
}
