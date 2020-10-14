package model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameState implements IGameState {

    IGameBoard board;
    List<IPlayer> players;

    /**
     * Constructor for GameState, also responsible for sorting players by age.
     * @param board GameBoard for Fish.
     * @param players List of Players playing Fish.
     */
    GameState(IGameBoard board, List<Player> players) {
        if (board != null && players != null) {
            Collections.sort(players);
            this.players = new ArrayList<>(players);
            this.board = board;
        } else {
            throw new IllegalArgumentException("Neither the GameBoard nor the List of Players can be null");
        }
    }


    @Override
    public List<IPlayer> getPlayers() {
        return new ArrayList<>(this.players);
    }

    @Override
    public Tile[][] getGameBoard() {
       return this.board.getGameBoard().clone();
    }

    //TODO
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

    //TODO write tests to confirm jsonBoard is correct
    @Override
    public JsonObject GameStateToJson() {
        JsonObject jsonGameState = new JsonObject();
        JsonArray jsonBoard = new JsonArray();
        Tile[][] board = this.invert(this.board.getGameBoard().clone());
        for (Tile[] row : board) {
            JsonArray jsonRow = new JsonArray();
            for (Tile tile: row) {
                jsonRow.add(tile.getFish());
            }
            jsonBoard.add(jsonRow.deepCopy());
        }
        jsonGameState.add("board", jsonBoard);

        JsonArray jsonPosition = new JsonArray();
        jsonPosition.add(0);
        jsonPosition.add(0);

        jsonGameState.add("position", jsonPosition);

        return jsonGameState;
    }

    private Tile[][] invert(Tile[][] board) {
        Tile[][] newBoard = new Tile[board.length][board.length];

        for (Tile[] col : board) {
            for (Tile tile: col) {
                newBoard[tile.getPosition().x][tile.getPosition().y] = tile.clone();
            }
        }
        return newBoard;
    }

    @Override
    public IGameState JsonToGameState(JsonObject gameState) {
        return null;
    }
}
