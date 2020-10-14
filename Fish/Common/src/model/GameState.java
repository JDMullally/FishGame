package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * GameState represents a Fish Game, which more specifically is made up of a GameBoard and a
 * List of Players playing the game.
 */
public class GameState extends GameBoard implements IGameState {

    private List<IPlayer> players; // the players of the game

    /*
     * the n'th turn of the game
     * i.e. It is a player's move when ((turn % players.size()) == index of a given player)
     */
    private int turn;

    /**
     * Constructor initializes a GameState with all arguments for a GameBoard and with a List of
     * Players.
     *
     * @param rows
     * @param columns
     * @param holes
     * @param minOneFishTiles
     * @param sameFish
     * @param players
     */
    public GameState(int rows, int columns, List<Point> holes, int minOneFishTiles, int sameFish, List<IPlayer> players) {
        super(rows, columns, holes, minOneFishTiles, sameFish);

        if (players == null) {
            throw new IllegalArgumentException("Players cannot be null");
        } else if (players.size() <= 0 || players.size() > 4) {
            throw new IllegalArgumentException("There must be between 1 and 4 players");
        }

        this.players = players;
        this.turn = 0;

        this.sortPlayers();
    }

    /**
     * Sorts players by age.
     */
    private void sortPlayers() {
        this.players.sort(Comparator.comparingInt(IPlayer::getAge));
    }

    /**
     * Returns true if a Player's Penguin can move to the specified Point.
     *
     * @param penguin IPenguin
     * @param player IPlayer
     * @param endpoint point to move to
     * @return boolean
     */
    private boolean isMoveLegal(IPenguin penguin, IPlayer player, Point endpoint) {
        return false; // TODO
    }

    @Override
    public List<IPlayer> getPlayers() {
        return new ArrayList<>(this.players);
    }

    @Override
    public int getTurn() {
        return this.turn;
    }

    @Override
    public Map<Penguin, List<Tile>> getPossibleMoves(Player player) {
        return null; // TODO
    }

    @Override
    public Penguin placePenguin(Penguin penguin, Player player, Tile tile) {
        return null; //TODO
    }

    @Override
    public boolean move(Player player, Penguin penguin, Tile currentTile, Tile newTile) throws IllegalArgumentException {
        return false; //TODO
    }

    @Override
    public boolean move(Player player, Penguin penguin, Point currentPoint, Point newPoint) throws IllegalArgumentException {
        return false; //TODO
    }

    @Override
    public boolean isGameOver() {
        return false; //TODO
    }

}
