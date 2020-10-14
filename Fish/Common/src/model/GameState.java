package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
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
    private boolean isMoveLegal(IPenguin penguin, IPlayer player, Point endpoint) throws IllegalArgumentException {
        // Checks if it is the input Player's turn
        if (!this.playerTurn().equals(player)) {
            throw new IllegalArgumentException("Invalid Move: Not this Player's turn");
        }
        // Checks if the input Player is moving their own penguin.
        if (player.getColor().getRGB() != penguin.getColor().getRGB()) {
            throw new IllegalArgumentException("Invalid Move: That's not this Player's Penguin");
        }

        // Checks if the endpoint is within the bounds of the board.
        int rows = this.getRows();
        int cols = this.getColumns();
        if (endpoint.x < 0 || endpoint.x >= rows || endpoint.y < 0 || endpoint.y >= cols) {
            throw new IllegalArgumentException("Invalid Move: That Point is outside the Board");
        }

        // Checks if the Penguin can actually move to the endpoint.
        for (Tile tile : this.getViableTiles(penguin.getPosition())) {
            Point point = tile.getPosition();
            if (point.x == endpoint.x && point.y == endpoint.y) {
                return true;
            }
        }

        throw new IllegalArgumentException("Invalid Move: Penguin cannot move there.");
    }

    /**
     * Returns true if a point has a penguin on it.
     *
     * @param point
     * @return boolean
     */
    private boolean pointContainsPenguin(Point point) {
        for (IPlayer player : this.players) {
            for (IPenguin penguin: player.getPenguins()) {
                if (point.equals(penguin.getPosition())) {
                    return true;
                }
            }
        }
        return false;
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
    public IPlayer playerTurn() {
        return this.players.get(this.turn % this.players.size());
    }

    @Override
    public Map<IPenguin, List<Tile>> getPossibleMoves(IPlayer player) {
        Map<IPenguin, List<Tile>> possibleMoves = new HashMap<>();
        for (IPenguin p : player.getPenguins()) {
            possibleMoves.put(p, this.getViableTiles(p.getPosition()));
        }
        return possibleMoves;
    }

    @Override
    public IPenguin placePenguin(IPenguin penguin, IPlayer player, Tile tile) throws IllegalArgumentException {
        if(penguin == null || player == null || tile == null) {
            throw new IllegalArgumentException("Enter a valid Penguin, Player and Tile");
        } else if (this.pointContainsPenguin(tile.getPosition())) {
            throw new IllegalArgumentException("Can't place a Penguin on another Penguin");
        }
        Tile removed = this.replaceTile(tile);
        penguin.addScore(removed.getFish());
        return penguin;
    }

    @Override
    public boolean move(IPlayer player, IPenguin penguin, Tile newTile, boolean pass) throws IllegalArgumentException {
        return this.move(player, penguin, newTile.getPosition(), pass);
    }

    @Override
    public boolean move(IPlayer player, IPenguin penguin, Point newPoint, boolean pass) throws IllegalArgumentException {
        if (pass) {
            this.turn++;
            return true;
        }
        else if(isMoveLegal(penguin, player, newPoint)) {
            this.turn++;
            Tile removed = this.replaceTile(newPoint);
            penguin.addScore(removed.getFish());
            penguin.move(newPoint);
            return true;
        }
        return false;
    }

    @Override
    public boolean isGameOver() {
        for (IPlayer player : this.players) {
            for (IPenguin penguin: player.getPenguins()) {
                if (!this.getViableTiles(penguin.getPosition()).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

}
