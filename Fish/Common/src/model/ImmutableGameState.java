package model;

import com.google.gson.JsonObject;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class ImmutableGameState implements ImmutableGameStateModel {

    private final GameState gameState;

    /**
     * Constructor takes in a mutable GameState and sets it to the private final gameState.
     *
     * @param gameState Mutable GameState
     */
    public ImmutableGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public Canvas getCanvas() {
        return this.gameState.getCanvas();
    }

    @Override
    public int getBoardWidth() {
        return this.gameState.getBoardWidth();
    }

    @Override
    public int getBoardHeight() {
        return this.gameState.getBoardHeight();
    }

    @Override
    public Tile getTile(Point point) {
        return this.gameState.getTile(point);
    }

    @Override
    public List<List<Tile>> getViablePaths(Tile tile) {
        return this.gameState.getViablePaths(tile);
    }

    @Override
    public List<List<Tile>> getViablePaths(Point point) {
        return this.gameState.getViablePaths(point);
    }

    @Override
    public List<Tile> getViableTiles(Tile tile) {
        return this.gameState.getViableTiles(tile);
    }

    @Override
    public List<Tile> getViableTiles(Point point) {
        return this.gameState.getViableTiles(point);
    }

    @Override
    public Tile replaceTile(Tile tile) {
        throw new UnsupportedOperationException("GameState is immutable.");
    }

    @Override
    public Tile replaceTile(Point point) {
        throw new UnsupportedOperationException("GameState is immutable.");
    }

    @Override
    public List<IPlayer> getPlayers() {
        return this.gameState.getPlayers();
    }

    @Override
    public int getTurn() {
        return this.gameState.getTurn();
    }

    @Override
    public Map<Penguin, List<Tile>> getPossibleMoves(Player player) {
        return this.gameState.getPossibleMoves(player);
    }

    @Override
    public Tile[][] getGameBoard() {
        return this.gameState.getGameBoard();
    }

    @Override
    public boolean isGameOver() {
        return this.gameState.isGameOver();
    }

    @Override
    public Penguin placePenguin(Penguin penguin, Player player, Tile tile) {
        throw new UnsupportedOperationException("GameState is immutable.");
    }

    @Override
    public boolean move(Player player, Penguin penguin, Tile currentTile, Tile newTile) throws IllegalArgumentException {
        throw new UnsupportedOperationException("GameState is immutable.");
    }

    @Override
    public boolean move(Player player, Penguin penguin, Point currentPoint, Point newPoint) throws IllegalArgumentException {
        throw new UnsupportedOperationException("GameState is immutable.");
    }
}
