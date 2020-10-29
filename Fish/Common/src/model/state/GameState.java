package model.state;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import java.util.Map;
import model.board.GameBoard;
import model.board.IGameBoard;
import model.board.Tile;
import util.ColorUtil;

/**
 * GameState represents a Fish Game, which more specifically is a GameBoard that has a
 * List of Players playing the game.
 * Implementation that represents the GameState for Fish.
 * A GameState should be able to change the GameBoard, show it's Players, add Players to the game,
 * let Players place Penguins, get the possible moves for a Penguin, and check if the game is over.
 */
public class GameState extends GameBoard implements IGameState {

    private List<IPlayer> players; // the players of the game

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

        if (this.newPlayers()) {
            this.sortPlayers();
        }
        this.validateInitialPlayers();
    }

    /**
     * Allows the creation of a GameState with a 2D Array of Tiles that keeps track
     * of the holes and values of the fish.
     * @param rows
     * @param columns
     * @param board
     * @param players
     */
    public GameState(int rows, int columns, Tile[][] board, List<IPlayer> players) {
        super(rows, columns, board);

        if (players == null) {
            throw new IllegalArgumentException("Players cannot be null");
        }

        this.players = new ArrayList<>(players);

        if (this.newPlayers()) {
            this.sortPlayers();
        }
        this.validateInitialPlayers();
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

        if (this.newPlayers()) {
            this.sortPlayers();
        }
        //this.sortPlayers();
        this.validateInitialPlayers();
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
                Point point = new Point(jsonPoint.get(1).getAsInt(), jsonPoint.get(0).getAsInt());

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
     * A checker so that players are only sorted by age once when they are initialized with no
     * penguins.
     * @return
     */
    private boolean newPlayers() {
        validateInitialPlayers();
        for (IPlayer player : this.players) {
          if(!player.getPenguins().isEmpty()) {
              return false;
          }
        }
        return true;
    }

    /**
     * Initial check for players.
     */
    private void validateInitialPlayers() {
        if (this.players.isEmpty() || this.players.size() <= 1) {
            throw new IllegalArgumentException("Cannot have a game with zero or one players");
        } else if (this.players.size() > 4) {
            throw new IllegalArgumentException("Cannot have a game with more than 4 players");
        } else {
            for (int i = 0; i < this.players.size(); i++) {
                IPlayer player = this.players.get(i);

                if (!player.getPenguins().isEmpty()) {
                    if (player.getPenguins().size() > (6 - this.players.size())) {
                        throw new IllegalArgumentException("Cannot have a player with more than 6 - N Penguins");
                    }
                    checkPlayerPenguins(player);
                }

                checkDuplicatePlayerColor(i, player);
            }
        }
    }

    /**
     * Abstracted method that checks if any Players have the same colors.  It throws an error if the Player
     * @param i int
     * @param player IPlayer.
     */
    private void checkDuplicatePlayerColor(int i, IPlayer player) {
        for (int j = 0; j < this.players.size(); j++) {
            IPlayer player2 = this.players.get(j);
            if (i != j && player.getColor().getRGB() == player2.getColor().getRGB()) {
                throw new IllegalArgumentException("2 players have the same color: " + player.getColor().toString());
            }
        }
    }

    /**
     * Checks if a Player's Penguins are owned by that player (have the same color)
     * and not placed on an empty tile.
     *
     * @param player IPlayer
     */
    private void checkPlayerPenguins(IPlayer player) {
        for (IPenguin penguin : player.getPenguins()) {
            if (player.getColor().getRGB() != penguin.getColor().getRGB()) {
                throw new IllegalArgumentException("Player has invalid Penguin colors");
            }

            Point penguinPoint = penguin.getPosition();
            if (this.getGameBoard()[penguinPoint.x][penguinPoint.y].isEmpty()) {
                throw new IllegalArgumentException("Player's penguin is placed on an empty tile");
            }
        }
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

                // checks that penguin colors are correct and that there are no duplicate player colors
                checkPlayerPenguins(player);

                // checks that there are no duplicate player colors
                checkDuplicatePlayerColor(i, player);
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

        // checks that the player has this penguin
        IPenguin validPenguin = null;
        for (IPenguin penguinTemp : playerTurn().getPenguins()) {
            if (penguin.equals(penguinTemp)) {
                validPenguin = penguinTemp;
            }
        }

        if (validPenguin == null) {
           throw new IllegalArgumentException("The player's penguin does not exist");
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

    /**
     * Inverts a Game Board representation.
     *
     * @param board the game board
     * @return inverted game board
     */
    private Tile[][] invertBoard(Tile[][] board) {
        Tile[][] newBoard = new Tile[board[0].length][board.length];

        for (Tile[] col : board) {
            for (Tile tile: col) {
                newBoard[tile.getPosition().y][tile.getPosition().x] = tile.clone();
            }
        }
        return newBoard;
    }

    @Override
    public List<IPlayer> getPlayers() {
        return new ArrayList<>(this.players);
    }

    @Override
    public IPlayer playerTurn() {
        return this.players.get(0);
    }

    @Override
    public LinkedHashMap<IPenguin, List<Tile>> getPossibleMoves(IPlayer player) {
        // creates a GameState where all penguins are instead holes.
        // this is so that getting viable tiles treats penguin locations as holes thus providing
        // the correct viable tiles.
        IGameState gameState = this.clone();
        for (IPlayer iPlayer : gameState.getPlayers()) {
            for (IPenguin iPenguin : iPlayer.getPenguins()) {
                gameState.replaceTile(iPenguin.getPosition());
            }
        }

        // generates viable tiles
        LinkedHashMap<IPenguin, List<Tile>> possibleMoves = new LinkedHashMap<>();
        for (IPenguin p : player.getPenguins()) {
            List<Tile> viableTiles = new ArrayList<>(gameState.getViableTiles(p.getPosition()));
            possibleMoves.put(p, viableTiles);
        }
        return possibleMoves;
    }

    @Override
    public List<Tile> getPenguinPlacementTiles() {
        List<Tile> tiles = new ArrayList<>();
        Tile[][] invertedBoard = this.invertBoard(this.getGameBoard());
        for (Tile[] rows : invertedBoard) {
            for (Tile tile : rows) {
                if (!tile.isEmpty() && !this.pointContainsPenguin(tile.getPosition())) {
                    tiles.add(tile);
                }
            }
        }

        return tiles;
    }

    @Override
    public IGameState placePenguin(IPlayer player, Tile tile) throws IllegalArgumentException {
        return this.placePenguin(player, tile.getPosition());
    }

    @Override
    public IGameState placePenguin(IPlayer player, Point point) throws IllegalArgumentException {
        if(player == null || point == null) {
            throw new IllegalArgumentException("Enter a valid Player, and Tile.");
        } else if (this.pointContainsPenguin(point)) {
            throw new IllegalArgumentException("Can't place a Penguin on another Penguin.");
        }

        IPenguin penguin = new Penguin(player.getColor(), point);

        // checks if the player is in the game and it's their turn
        IPlayer validPlayer = player.equals(this.playerTurn()) ? this.playerTurn() : null;

        // adds the penguin to the player if possible
        if (validPlayer == null) {
            throw new IllegalArgumentException("It is not this player's turn");
        } else if (validPlayer.getPenguins().size() + 1 > (6 - this.players.size())) {
            throw new IllegalArgumentException("Cannot place another penguin, already have too many");
        } else {
            validPlayer.addPenguin(penguin);
        }

        penguin.move(point);
        IPlayer movedPlayer = this.players.remove(0);
        this.players.add(movedPlayer);

        return this;
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

        if (!player.equals(this.playerTurn())) {
            throw new IllegalArgumentException("Not this Player's Turn");
        }

        this.validatePlayers();

        // if the player passes their turn
        if (pass) {
            IPlayer movedPlayer = this.players.remove(0);
            this.players.add(movedPlayer);
            return this;
        }

        // attempts to move
        if (this.isMoveLegal(penguin, player, newPoint)) {

            // removes tile
            Tile removed = this.replaceTile(penguin.getPosition());

            // updates player
            IPlayer movedPlayer = this.players.remove(0);
            movedPlayer.addScore(removed.getFish());

            // updates penguin
            // checks that the player has this penguin
            IPenguin validPenguin = null;
            for (IPenguin penguinTemp : movedPlayer.getPenguins()) {
                if (penguin.equals(penguinTemp)) {
                    validPenguin = penguinTemp;
                }
            }
            validPenguin.move(newPoint);
            this.players.add(movedPlayer);

            return this;
        }

        throw new IllegalArgumentException("Move is illegal");
    }

    @Override
    public boolean isCurrentPlayerStuck() {
        boolean isStuck = true;
        for (Map.Entry<IPenguin, List<Tile>> moves : this.getPossibleMoves(this.playerTurn()).entrySet()) {
            IPenguin penguin = moves.getKey().clone();
            List<Tile> tiles = moves.getValue();

                isStuck = isStuck && tiles.isEmpty();
        }
        return isStuck;
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


    @Override
    public boolean equals(Object o) {
        if (o instanceof GameState) {
            GameState other = (GameState) o;
            return Arrays.deepEquals(this.getGameBoard(), other.getGameBoard())
                && this.getColumns() == other.getColumns()
                && this.getRows() == other.getRows()
                && this.players.equals(other.players);
        }
        return false;
    }

    @Override
    public IGameState clone() {
        List<IPlayer> newPlayers = new ArrayList<>();
        for (IPlayer player : players) {
            newPlayers.add(player.clone());
        }
        return new GameState(this.getRows(), this.getColumns(), this.getGameBoard(), newPlayers);
    }

}
