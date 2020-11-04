package model.games;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import model.board.Tile;
import model.state.GameState;
import model.state.IGameState;
import model.state.IPlayer;
import model.tree.Action;
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

    private List<PlayerInterface> players;
    private List<PlayerInterface> cheaters;
    private List<GameAction> ongoingActions;
    private IGameState gameState;
    private IGameResult gameResult;

    /**
     * Constructor for Referee that takes in a List of Players. The Referee may assume that the
     * sequence is arranged in ascending order of player age.
     * @param players List of PlayerInterface
     */
    public Referee(List<PlayerInterface> players) {
        if(players == null) {
            throw new IllegalArgumentException("A Referee cannot start a Game with null Players");
        }

        this.players = players;
        this.cheaters = new ArrayList<>();
        this.ongoingActions = new ArrayList<>();
    }

    /**
     * Returns the PlayerInterface for a given IPlayer.
     *
     * @param curPlayer IPlayer
     * @return PlayerInterface
     */
    private PlayerInterface getPlayerInterface(IPlayer curPlayer) {
        for (PlayerInterface playerInterface : this.players) {
            IPlayer player = playerInterface.getPlayer();
            if (player.getColor().getRGB() == curPlayer.getColor().getRGB() && player.getAge() == curPlayer.getAge()) {
                return playerInterface;
            }
        }

        throw new IllegalArgumentException("PlayerInterface doesn't exist for the given IPlayer");
    }

    /**
     * Creates an initial game state by asking players to place penguins until all of their penguins
     * have been placed
     */
    private void createIntitialGame(int rows, int columns, Tile[][] board) {
        // creates GameState
        List<IPlayer> gamePlayers = this.players.stream().map(PlayerInterface::getPlayer).collect(Collectors.toList());
        this.gameState = new GameState(rows, columns, board, gamePlayers);

        // allows players to place penguins until the game is ready to begin
        while (!this.gameState.isGameReady()) {
            IPlayer curPlayer = this.gameState.playerTurn();
            PlayerInterface curPlayerInterface = this.getPlayerInterface(curPlayer);
            try {
                Action action = curPlayerInterface.placePenguin(this.gameState);
                this.gameState = action.apply(this.gameState);
                this.ongoingActions.add(new GameAction(action));
            } catch (Exception e) {
                this.gameState.removePlayer(curPlayer);
                this.players.remove(curPlayerInterface);
                this.cheaters.add(curPlayerInterface);
                this.ongoingActions.add(new GameAction(new PlayerCheated(curPlayer)));
            }
        }
    }

    @Override
    public IGameResult runGame(int rows, int columns, Tile[][] board) {
        this.createIntitialGame(rows, columns, board);

        // allows players to move penguins until the game is over
        while (!this.gameState.isGameOver()) {
            IPlayer curPlayer = this.gameState.playerTurn();
            PlayerInterface curPlayerInterface = this.getPlayerInterface(curPlayer);
            try {
                Action action = curPlayerInterface.movePenguin(this.gameState);
                this.gameState = action.apply(this.gameState);
                this.ongoingActions.add(new GameAction(action));
            } catch (Exception e) {
                this.gameState.removePlayer(curPlayer);
                this.players.remove(curPlayerInterface);
                this.cheaters.add(curPlayerInterface);
                this.ongoingActions.add(new GameAction(new PlayerCheated(curPlayer)));
            }
        }

        return null;
    }

    @Override
    public IGameResult getGameResult() throws IllegalStateException {
        return this.gameResult;
    }

    @Override
    public IGameState getGameState() throws IllegalStateException {
        return this.gameState;
    }

    @Override
    public List<GameAction> getOngoingActions() {
        return new ArrayList<>(this.ongoingActions);
    }
}
