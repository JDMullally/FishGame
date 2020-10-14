package model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

    @Override
    public List<IPlayer> getPlayers() {
        return new ArrayList<>(this.players);
    }

    @Override
    public int getTurn() {
        return this.turn;
    }

    // TODO
    @Override
    public Tile makeMove(Penguin penguin, Player player,
        Tile newTile, IGameBoard board) throws IllegalArgumentException {
        if (isMoveLegal(penguin, player, newTile)) {
            return null;
        }
        return null;
    }

    //TODO required for makeMove
    private boolean isMoveLegal(Penguin penguin, Player player, Tile newTile) {
        return false;
    }

    //TODO
    @Override
    public boolean isGameOver() {
        return false;
    }

    //TODO
    @Override
    public Penguin placePenguin(Penguin penguin, Player player, Tile tile) {
        return null;
    }


}
