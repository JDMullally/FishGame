package model;

import com.google.gson.JsonArray;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GameState represents a Fish Game, which more specifically is made up of a GameBoard and a
 * List of Players playing the game.
 * Implementation that represents the GameState for Fish.
 * A GameState should be able to change the GameBoard, show it's Players, add Players to the game,
 * let Players place Penguins, get the possible moves for a Penguin, and check if the game is over.
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
     * @param rows rows on the board
     * @param columns columns on the board
     * @param holes holes on the board
     * @param minOneFishTiles number of min one fish tiles
     * @param sameFish same fish number
     */
    public GameState(int rows, int columns, List<Point> holes, int minOneFishTiles, int sameFish) {
        super(rows, columns, holes, minOneFishTiles, sameFish);

        this.players = new ArrayList<>();
        this.turn = 0;
    }

    /**
     * Constructor that allows the addition of a full list of players.
     * @param rows number of rows on the board
     * @param columns number of columns on the board
     * @param holes list of holes on the board
     * @param minOneFishTiles minimum number of one fish tiles
     * @param sameFish makes the whole board have the same number of fish (equal to sameFish)
     * @param players List of Players
     */
    public GameState(int rows, int columns, List<Point> holes, int minOneFishTiles, int sameFish, List<IPlayer> players) {
        super(rows, columns, holes, minOneFishTiles, sameFish);

        if (players == null) {
            throw new IllegalArgumentException("Players cannot be null");
        }
        this.players = new ArrayList<>(players);
        this.turn = 0;

        this.sortPlayers();
        this.validatePlayers();
    }

    /**
     * Constructor initializes GameState with rows, columns, and a well formatted JsonArray.
     *
     * @param rows number of rows on the board
     * @param columns number of columns on the board
     * @param jsonArray a JsonArray representing a game board
     */
    public GameState(int rows, int columns, JsonArray jsonArray) {
        super(rows, columns, jsonArray);
    }

    /**
     * Sorts players by age.
     */
    private void sortPlayers() {
        this.players.sort(Comparator.comparingInt(IPlayer::getAge));
    }

    /**
     * Validates that all players are initialized correctly on the basis of unique color, having a
     * valid number of players (1-4), and each player having exactly (6 - players.size()) penguins.
     */
    private void validatePlayers() {
        if (this.players.isEmpty() || this.players.size() <= 1) {
            throw new IllegalArgumentException("Cannot have a game with zero or one players");
        } else if (this.players.size() > 4) {
            throw new IllegalArgumentException("Cannot have a game with more than 4 players");
        } else {
            for (int i = 0; i < this.players.size(); i++) {
                IPlayer player = this.players.get(i);

                // checks that penguin size is valid
                if (player.getPenguins().size() != (6 - this.players.size())) {
                    throw new IllegalArgumentException("Player '" + player.getColor().toString() + "' has the wrong number of penguins");
                }
                // checks that penguin colors are correct
                for (IPenguin penguin : player.getPenguins()) {
                    if (player.getColor().getRGB() != penguin.getColor().getRGB()) {
                        throw new IllegalArgumentException("Player has invalid Penguin colors");
                    }
                }
                // checks that there are no duplicate player colors
                for (int j = 0; j < this.players.size(); j++) {
                    IPlayer player2 = this.players.get(j);

                    if (i != j && player.getColor().getRGB() == player2.getColor().getRGB()) {
                        throw new IllegalArgumentException("2 players have the same color: " + player.getColor().toString());
                    }
                }
            }
        }
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
     * @param point Point
     * @return boolean
     */
    private boolean pointContainsPenguin(Point point) {
        for (IPlayer player : this.players) {
            for (IPenguin penguin : player.getPenguins()) {
                Point penguinPoint = penguin.getPosition();
                if (penguinPoint != null && (point.x == penguinPoint.x && point.y == penguinPoint.y)) {
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
    public void addPlayer(Player player) throws IllegalArgumentException {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        } else if (players.size() > 4) {
            throw new IllegalArgumentException("There can't be more than 4 players");
        }

        this.players.add(player);
        this.sortPlayers();
    }

    @Override
    public void placePenguin(IPenguin penguin, IPlayer player, Tile tile) throws IllegalArgumentException {
        if(penguin == null || player == null || tile == null) {
            throw new IllegalArgumentException("Enter a valid Penguin, Player, and Tile.");
        } else if (this.pointContainsPenguin(tile.getPosition())) {
            throw new IllegalArgumentException("Can't place a Penguin on another Penguin.");
        } else if (penguin.getPosition() != null) {
            throw new IllegalArgumentException("Can't place a Penguin already on the board.");
        }

        // checks if the player is in the game
        IPlayer validPlayer = null;
        for (IPlayer p: this.players) {
            if (player.equals(p)) {
                validPlayer = player.clone();
            }
        }

        // adds the penguin to the player if possible
        if (validPlayer == null) {
            throw new IllegalArgumentException("Player specified is not in the game");
        } else if (validPlayer.getColor().getRGB() != penguin.getColor().getRGB()) {
            throw new IllegalArgumentException("Penguin and Player doesn't have the same color");
        } else if (validPlayer.getPenguins().size() > (6 - this.players.size())) {
            throw new IllegalArgumentException("Cannot place another penguin, already have too many");
        } else {
            validPlayer.addPenguin(penguin);
        }

        // removes tile
        Tile removed = this.replaceTile(tile);

        // updates penguin
        penguin.addScore(removed.getFish());
        penguin.move(tile);
        this.turn++;
    }

    @Override
    public boolean move(IPlayer player, IPenguin penguin, Tile newTile, boolean pass) throws IllegalArgumentException {
        if (player == null || penguin == null || newTile == null) {
            throw new IllegalArgumentException("Enter a non-null Player, Penguin and Tile");
        }

        return this.move(player, penguin, newTile.getPosition(), pass);
    }

    @Override
    public boolean move(IPlayer player, IPenguin penguin, Point newPoint, boolean pass) throws IllegalArgumentException {
        if (player == null || penguin == null || newPoint == null) {
            throw new IllegalArgumentException("Enter a non-null Player, Penguin and Point");
        }

        this.validatePlayers();

        // if the player passes their turn
        if (pass) {
            this.turn++;
            return true;
        }

        // attempts to move
        if (this.isMoveLegal(penguin, player, newPoint)) {
            this.turn++;

            // removes tile
            Tile removed = this.replaceTile(newPoint);

            // updates penguin
            penguin.addScore(removed.getFish());
            penguin.move(newPoint);

            return true;
        }
        return false;
    }

    @Override
    public boolean isGameOver() {
        for (IPlayer player : this.players) {
            for (IPenguin penguin : player.getPenguins()) {
                for (Tile tile : this.getViableTiles(penguin.getPosition())) {
                    if (!this.pointContainsPenguin(tile.getPosition())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
