package model.games;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.awt.*;
import java.util.ArrayList;
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
import model.tree.GameTree;
import model.tree.IGameTree;
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
    private IGameTree gameTree;
    private IGameState initialGameState;
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

        this.timeout = 1;
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
                    informPlayersOfColor(Color.RED, players.get(i));
                    break;
                case 1:
                    mappedPlayers.put(Color.WHITE, players.get(i));
                    informPlayersOfColor(Color.WHITE, players.get(i));
                    break;
                case 2:
                    Color brown = new Color(210, 105, 30);
                    mappedPlayers.put(brown, players.get(i));
                    informPlayersOfColor(brown, players.get(i));
                    break;
                case 3:
                    mappedPlayers.put(Color.BLACK, players.get(i));
                    informPlayersOfColor(Color.BLACK, players.get(i));
                    break;
                default:
                    throw new IllegalArgumentException("There should not be more than 4 players");
            }
        }

        this.informPlayersOfOtherPlayers(mappedPlayers);
        return mappedPlayers;
    }

    /**
     * Implements a callable that calls playerColor that informs a player of their own
     * color. The callable has a set timeout, but in this implementation it is one second.
     *
     * @param color Color of the player
     * @param player PlayerInterface that is being informed
     */

    private void informPlayersOfColor(Color color, PlayerInterface player) {
        Callable<Boolean> task = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                player.playerColor(color);
                return true;
            }};
        executeTask(task);
    }

    /**
     * Implements a callable that calls otherPlayerColors that informs a player of the other player
     * colors. The callable has a set timeout, but in this implementation it is one second.
     *
     * @param mappedPlayers a map of all Players in the Game and their Colors.
     */
    private void informPlayersOfOtherPlayers(Map<Color, PlayerInterface> mappedPlayers) {
        for (Entry<Color, PlayerInterface> entry : mappedPlayers.entrySet()) {
            PlayerInterface player = entry.getValue();
            Color color = entry.getKey();

            Callable<Boolean> task = new Callable<Boolean>() {
                @Override
                public Boolean call() throws TimeoutException {
                    player.otherPlayerColors(getOtherColors(color, mappedPlayers));
                    return true;
                }};

            executeTask(task);
        }
    }

    /**
     * Executes a callable with a SingleThreadExecutor.  Once the task is complete or times out,
     * the thread executor is shutdown.
     *
     * @param task Callable task that is being executed.
     */
    private void executeTask(Callable<Boolean> task) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Boolean> future = executor.submit(task);
        try {
            future.get(this.timeout, TimeUnit.SECONDS);
            executor.shutdownNow();
        } catch (Exception e) {
            executor.shutdownNow();
        }
    }

    /**
     * Gets all other class
     * @param color
     * @return
     */
    private List<Color> getOtherColors(Color color, Map<Color, PlayerInterface> mappedPlayers) {
        List<Color> colors = new ArrayList<>(mappedPlayers.keySet());
        colors.remove(color);
        return colors;
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
        while (!this.gameTree.getState().isGameReady()) {
            this.placementTurn();
        }

        // allows players to move penguins until the game is over
        while (!this.gameTree.getState().isGameOver()) {
            this.runTurn();
        }

        Action endGame = new GameEnded(this.gameTree.getState());
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
        IGameState gameState = new GameState(rows, columns, gameBoard.getGameBoard(), gamePlayers);
        this.initialGameState = gameState;
        this.gameTree = new GameTree(gameState);
    }

    /**
     * Runs a single Placement Turn and returns the new state after the move is made.
     *
     * A Placement Turn of Fish means that the current player enters a valid placement of
     * one of their penguins in designated time.
     *
     * @return IGameState
     */
    public void placementTurn() {
        IGameState gameState = this.gameTree.getState();
        IPlayer curPlayer = gameState.playerTurn();
        PlayerInterface curPlayerInterface = this.players.get(curPlayer.getColor());

        Callable<Action> task = new Callable<Action>() {
            public Action call() throws TimeoutException {
                return curPlayerInterface.placePenguin(gameTree.getState());
            }
        };

        gameState = placeOrMove(task, curPlayer, curPlayerInterface);
        this.gameTree = new GameTree(gameState);
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
        IGameState currentGameState = this.gameTree.getState();
        IPlayer curPlayer = currentGameState.playerTurn();
        PlayerInterface curPlayerInterface = this.players.get(curPlayer.getColor());

        // if the player can't move, pass for them
        if (currentGameState.isCurrentPlayerStuck()) {
            Action action = new PassPenguin(curPlayer);
            newGameState = action.apply(currentGameState);
            this.ongoingActions.add(new GameAction(action));
            this.gameTree = new GameTree(newGameState);
            return this.gameTree.getState().clone();
        }


        Callable<Action> task = new Callable<Action>() {
            public Action call() throws TimeoutException {
                return curPlayerInterface.movePenguin(gameTree.getState());
            }
        };

        this.gameTree = new GameTree(placeOrMove(task, curPlayer, curPlayerInterface));
        return this.gameTree.getState().clone();
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

        List<PlayerInterface> otherPlayers = new ArrayList<>(this.players.values());
        otherPlayers.remove(curPlayerInterface);

        // allow the player to move based on their strategy
        try {
            Action action = future.get(this.timeout, TimeUnit.SECONDS);

            newGameState = this.gameTree.queryAction(this.gameTree.getState(), action);
            executor.shutdownNow();
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
        IGameState newGameState = action.apply(this.gameTree.getState());

        List<PlayerInterface> otherPlayers = new ArrayList<>(this.players.values());
        otherPlayers.remove(curPlayerInterface);

        for (PlayerInterface player : otherPlayers) {
            player.clearOnGoingAction();
        }

        this.players.remove(curPlayer.getColor());
        this.cheaters.put(curPlayer.getColor(), curPlayerInterface);
        this.ongoingActions.add(new GameAction(action));
        return newGameState;
    }

    @Override
    public IGameState getInitialGameState() throws IllegalStateException {
        if (this.initialGameState == null) {
            throw new IllegalStateException("The game has not started yet");
        }

        return this.initialGameState.clone();
    }

    @Override
    public IGameState getGameState() throws IllegalStateException {
        if (this.gameTree.getState() == null) {
            throw new IllegalStateException("The game has not started yet");
        }

        return this.gameTree.getState().clone();
    }

    @Override
    public List<IGameAction> getOngoingActions() {
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
        for (IPlayer player : this.gameTree.getState().getPlayers()) {
            playersI.put(this.players.get(player.getColor()), player.getScore());
        }

        List<PlayerInterface> cheatersI = new ArrayList<>(this.cheaters.values());

        return new GameResult(playersI, cheatersI);
    }
}
