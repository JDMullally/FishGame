package model.games;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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
 */
public class Referee implements IReferee {

    private Map<Color, PlayerInterface> players;
    private Map<Color, PlayerInterface> cheaters;
    private List<GameAction> ongoingActions;
    private IGameState gameState;
    private IGameResult gameResult;

    /**
     * Constructor for Referee that takes in a List of Players. The Referee may assume that the
     * sequence is arranged in ascending order of player age.
     *
     * @param players List of PlayerInterface
     */
    public Referee(List<PlayerInterface> players) {
        if (players == null) {
            throw new IllegalArgumentException("A Referee cannot start a Game with null Players");
        } else if (players.size() <= 1 || players.size() > 4) {
            throw new IllegalArgumentException("There must be between 2 and 4 players inclusive in the game");
        }

        this.players = this.initializePlayerInterfaces(players);
        this.cheaters = new HashMap<>();
        this.ongoingActions = new ArrayList<>();
    }

    /**
     * Initializes players as a map of Color to PlayerInterface
     *
     * @param players List of PlayerInterface
     * @return Map of Color to PlayerInterface
     */
    private Map<Color, PlayerInterface> initializePlayerInterfaces(List<PlayerInterface> players) {
        Map<Color, PlayerInterface> mappedPlayers = new HashMap<>();
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

    private List<IPlayer> initializePlayers() {
        return this.players.keySet().stream().map(color -> {
            switch (color.toString()) {
                case "java.awt.Color[r=255,g=0,b=0]":
                    return new Player(color, 1, new ArrayList<>());
                case "java.awt.Color[r=255,g=255,b=255]":
                    return new Player(color, 2, new ArrayList<>());
                case "java.awt.Color[r=210,g=105,b=30]":
                    return new Player(color, 3, new ArrayList<>());
                case "java.awt.Color[r=0,g=0,b=0]":
                    return new Player(color, 4, new ArrayList<>());
                default:
                    throw new IllegalArgumentException("Invalid color given");
            }
        }).collect(Collectors.toList());
    }

    /**
     * Creates an initial game state by asking players to place penguins until all of their penguins
     * have been placed
     */
    private void createIntitialGame(int rows, int columns) {
        // creates game board
        IGameBoard gameBoard = new GameBoard(rows, columns);

        // create players based on their color
        List<IPlayer> gamePlayers = this.initializePlayers();

        // creates game state
        this.gameState = new GameState(rows, columns, gameBoard.getGameBoard(), gamePlayers);

        // allows players to place penguins until the game is ready to begin
        while (!this.gameState.isGameReady()) {
            IPlayer curPlayer = this.gameState.playerTurn();
            PlayerInterface curPlayerInterface = this.players.get(curPlayer.getColor());
            try {
                Action action = curPlayerInterface.placePenguin(this.gameState);
                this.gameState = action.apply(this.gameState);
                this.ongoingActions.add(new GameAction(action));
            } catch (Exception e) {
                this.playerCheated(curPlayer, curPlayerInterface);
            }
        }
    }

    /**
     * When a player cheats, they are removed from the game.
     *
     * @param curPlayer IPlayer
     * @param curPlayerInterface IPLayerInterface
     */
    private void playerCheated(IPlayer curPlayer, PlayerInterface curPlayerInterface) {
        Action action = new PlayerCheated(curPlayer);
        this.gameState = action.apply(this.gameState);
        this.players.remove(curPlayer.getColor());
        this.cheaters.put(curPlayer.getColor(), curPlayerInterface);
        this.ongoingActions.add(new GameAction(action));
    }

    /**
     * Creates the game result based on the scores of the remaining players and cheaters.
     *
     * @return IGameResult
     */
    private IGameResult retrieveGameResult() {
        // gets players from GameState by score in descending order
        List<PlayerInterface> playersI = this.gameState.getPlayers()
                .stream()
                .sorted(Comparator.comparingInt(IPlayer::getScore).reversed())
                .map(player -> this.players.get(player.getColor()))
                .collect(Collectors.toList());
        List<PlayerInterface> cheatersI = new ArrayList<>(this.cheaters.values());

        return new GameResult(playersI, cheatersI);
    }

    @Override
    public IGameResult runGame(int rows, int columns) {
        this.createIntitialGame(rows, columns);

        // allows players to move penguins until the game is over
        while (!this.gameState.isGameOver()) {
            IPlayer curPlayer = this.gameState.playerTurn();
            PlayerInterface curPlayerInterface = this.players.get(curPlayer.getColor());

            // if the player can't move, pass for them
            if (this.gameState.isCurrentPlayerStuck()) {
                Action action = new PassPenguin(curPlayer);
                this.gameState = action.apply(this.gameState);
                this.ongoingActions.add(new GameAction(action));
            }

            // allow the player to move based on their strategy
            try {
                Action action = curPlayerInterface.movePenguin(this.gameState);
                this.gameState = action.apply(this.gameState);
                this.ongoingActions.add(new GameAction(action));
            } catch (Exception e) {
                this.playerCheated(curPlayer, curPlayerInterface);
            }
        }

        this.gameResult = this.retrieveGameResult();
        return this.gameResult;
    }

    @Override
    public IGameResult getGameResult() throws IllegalStateException {
        if (this.gameResult == null) {
            throw new IllegalStateException("The game has not concluded");
        }

        return this.gameResult;
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
}
