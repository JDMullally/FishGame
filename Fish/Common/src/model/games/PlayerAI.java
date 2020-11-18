package model.games;

import model.state.IGameState;
import model.strategy.IStrategy;
import model.tree.Action;
import model.tree.PlayerInterface;

/**
 * This Class represents a in-house AI Player's interface for interacting with the game.
 * PlayerAI must be able to place their avatars on the initial board and take turns when requested
 * by a referee.
 */
public class PlayerAI implements PlayerInterface {

    private IStrategy strategy;
    private int turns;
    private int age;
    private String uid;

    /**
     * Constructor takes in a player's IStrategy
     *
     * @param strategy IStrategy
     */
    public PlayerAI(IStrategy strategy, int turns) {
        if (strategy == null) {
            throw new IllegalArgumentException("IStrategy cannot be null");
        }

        this.age = 1;
        this.uid = "AI Player";
        this.strategy = strategy;
        this.turns = turns;
    }

    public PlayerAI(IStrategy strategy, int turns, int age, String uid) {
        if (strategy == null) {
            throw new IllegalArgumentException("IStrategy cannot be null");
        }
        if (uid == null || uid.length() > 12) {
            throw new IllegalArgumentException("A Player cannot have a null name or a name with "
                + "more that 12 characters long");
        }

        this.age = age;
        this.uid = uid;
        this.strategy = strategy;
        this.turns = turns;
    }

    @Override
    public Action placePenguin(IGameState state) {
        return this.strategy.choosePlacePenguinAction(state);
    }

    @Override
    public Action movePenguin(IGameState state) {
        return this.strategy.chooseMoveAction(state, turns);
    }

    @Override
    public int getPlayerAge() {
        return this.age;
    }

    @Override
    public String getPlayerID() {
        return this.uid;
    }

    @Override
    public boolean tournamentHasStarted() {
        return true;
    }

    @Override
    public boolean gameHasStarted() {
        return true;
    }

    @Override
    public void kickedForCheating() {
        throw new IllegalStateException("Our AI should not cheat");
    }

    @Override
    public void gameResults(IGameResult result) {
        if (result.getWinners().contains(this)) {
            System.out.println("Hooray");
        }
    }

    @Override
    public boolean tournamentResults(boolean youWon) {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PlayerAI) {
            PlayerAI other = (PlayerAI) o;
            return this.strategy.equals(other.strategy)
                && this.age == other.age
                && this.uid.equals(other.uid);
        }
        return false;
    }
}
