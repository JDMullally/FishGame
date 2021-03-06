package model.state;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.board.Canvas;
import model.board.ICanvas;
import model.board.Tile;
import model.games.IGameAction;

/**
 * Class implementation that represents the model for an immutable implementation of a GameState. No new methods are
 * required, as those from GameState are carried over. This interface is required however, as
 * to distinguish the difference between classes that implement a mutable vs immutable model.
 */
public class ImmutableGameState implements ImmutableGameStateModel {

    private final IGameState gameState;

    /**
     * Constructor takes in a mutable GameState and sets it to the private final gameState.
     *
     * @param gameState Mutable GameState
     */
    public ImmutableGameState(IGameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public ICanvas getCanvas() {
        return this.gameState.getCanvas();
    }

    @Override
    public int getRows() {
        return this.gameState.getRows();
    }

    @Override
    public int getColumns() {
        return this.gameState.getColumns();
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
    public Tile removeTile(Point point) {
        throw new UnsupportedOperationException("GameState is immutable.");
    }

    @Override
    public List<IPlayer> getPlayers() {
        return this.gameState.getPlayers();
    }

    @Override
    public IPlayer playerTurn() {
        return this.gameState.playerTurn();
    }

    @Override
    public LinkedHashMap<IPenguin, List<Tile>> getPossibleMoves(IPlayer player) {
        return this.gameState.getPossibleMoves(player);
    }

    @Override
    public List<Tile> getPenguinPlacementTiles() {
        return this.gameState.getPenguinPlacementTiles();
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
    public IGameState clone() {
        return this.gameState.clone();
    }

    @Override
    public IGameState placePenguin(IPlayer player, Tile tile) throws IllegalArgumentException {
        throw new UnsupportedOperationException("GameState is immutable.");
    }

    @Override
    public IGameState placePenguin(IPlayer player, Point point) throws IllegalArgumentException {
        throw new UnsupportedOperationException("GameState is immutable.");
    }

    @Override
    public IGameState move(IPlayer player, IPenguin penguin, Tile newTile, boolean pass) throws IllegalArgumentException {
        throw new UnsupportedOperationException("GameState is immutable.");
    }

    @Override
    public IGameState move(IPlayer player, IPenguin penguin, Point newPoint, boolean pass) throws IllegalArgumentException {
        throw new UnsupportedOperationException("GameState is immutable.");
    }

    @Override
    public IGameState removePlayer(IPlayer player) {
        throw new UnsupportedOperationException("GameState is immutable.");
    }

    @Override
    public boolean isCurrentPlayerStuck() {
        return this.gameState.isCurrentPlayerStuck();
    }

    @Override
    public boolean isGameReady() {
        return this.gameState.isGameReady();
    }

    @Override
    public ImmutableGameStateModel getNextGameState(IGameAction action) {
        return action.getNextState(this);
    }
}
