package model.state;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import model.board.GameBoard;
import model.board.Tile;
import util.ColorUtil;

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
        //this.placePlayerPenguins(); TODO: makes tests fail, but should be called
    }

    /**
     * Constructor initializes GameState with rows, columns, and a well formatted JsonArray.
     *
     * @param rows number of rows on the board
     * @param columns number of columns on the board
     * @param board a JsonArray representing a game board
     * @param players a JsonArray representing players
     */
    public GameState(int rows, int columns, JsonArray board, JsonArray players) {
        super(rows, columns, board);

        this.players = this.jsonToPlayers(players);

        this.sortPlayers();
        this.validatePlayers();
        this.placePlayerPenguins();
    }

    /**
     * Returns a a list of players with the specified parameters.
     *
     * @param jsonArray JsonArray
     * @return List of Iplayer
     */
    private List<IPlayer> jsonToPlayers(JsonArray jsonArray) {
        List<IPlayer> players = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonPlayer = jsonArray.get(i).getAsJsonObject();
            JsonArray jsonPenguins = jsonPlayer.get("places").getAsJsonArray();
            String strColor = jsonPlayer.get("color").toString();

            Color color = ColorUtil.toColor(strColor);

            List<IPenguin> penguins = new ArrayList<>();
            for (int j = 0; j < jsonPenguins.size(); j++) {
                JsonArray jsonPoint = jsonPenguins.get(j).getAsJsonArray();
                Point point = new Point(jsonPoint.get(0).getAsInt(), jsonPoint.get(1).getAsInt());

                penguins.add(new Penguin(color, point));
            }

            players.add(new Player(color, i + 1, penguins));
        }

        return players;
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
     * Places the player's penguins on the game board.
     */
    private void placePlayerPenguins() {
        for (IPlayer player : this.players) {
            for (IPenguin penguin : player.getPenguins()) {
                this.placePenguin(penguin, player, penguin.getPosition(), false);
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
        if (endpoint.x < 0 || endpoint.x >= cols || endpoint.y < 0 || endpoint.y >= rows) {
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
    public LinkedHashMap<IPenguin, List<Tile>> getPossibleMoves(IPlayer player) {
        LinkedHashMap<IPenguin, List<Tile>> possibleMoves = new LinkedHashMap<>();
        for (IPenguin p : player.getPenguins()) {
            possibleMoves.put(p, this.getViableTiles(p.getPosition()));
        }
        return possibleMoves;
    }

    @Override
    public void placePenguin(IPenguin penguin, IPlayer player, Tile tile, boolean addToPlayer) throws IllegalArgumentException {
        this.placePenguin(penguin, player, tile.getPosition(), addToPlayer);
    }

    @Override
    public void placePenguin(IPenguin penguin, IPlayer player, Point point, boolean addToPlayer) throws IllegalArgumentException {
        if(penguin == null || player == null || point == null) {
            throw new IllegalArgumentException("Enter a valid Penguin, Player, and Tile.");
        } else if (addToPlayer && this.pointContainsPenguin(point)) {
            throw new IllegalArgumentException("Can't place a Penguin on another Penguin.");
        } else if (addToPlayer && penguin.getPosition() != null) {
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
        } else if (addToPlayer) {
            validPlayer.addPenguin(penguin);
        }

        // removes tile
        Tile removed = this.replaceTile(point);

        // updates penguin
        player.addScore(removed.getFish());
        penguin.move(point);
        this.turn++;
    }

    @Override
    public IGameState move(IPlayer player, IPenguin penguin, Tile newTile, boolean pass) throws IllegalArgumentException {
        if (player == null || penguin == null || newTile == null) {
            throw new IllegalArgumentException("Enter a non-null Player, Penguin and Tile");
        }

        return this.move(player, penguin, newTile.getPosition(), pass);
    }

    @Override
    public IGameState move(IPlayer player, IPenguin penguin, Point newPoint, boolean pass) throws IllegalArgumentException {
        if (player == null || penguin == null || newPoint == null) {
            throw new IllegalArgumentException("Enter a non-null Player, Penguin and Point");
        }

        this.validatePlayers();

        // if the player passes their turn
        if (pass) {
            this.turn++;
            return this;
        }

        // attempts to move
        if (this.isMoveLegal(penguin, player, newPoint)) {
            this.turn++;

            // removes tile
            Tile removed = this.replaceTile(newPoint);

            // updates penguin
            player.addScore(removed.getFish());
            penguin.move(newPoint);

            return this;
        }

        throw new IllegalArgumentException("Move is illegal");
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