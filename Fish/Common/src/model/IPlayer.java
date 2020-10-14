package model;

/**
 * Interface for Player class that allows them to interact with the GameState.
 */
public interface IPlayer extends Comparable<Player> {

    Tile makeMove(Penguin penguin, Tile tile, IGameState gameState);


}
