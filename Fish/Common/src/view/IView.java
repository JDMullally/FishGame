package view;

import java.util.List;

import javax.swing.*;

import controller.IController;
import model.games.IGameResult;
import model.state.ImmutableGameStateModel;
import model.board.Tile;

/**
 * Represents the model for a visual view of the Fish Game.
 */
public interface IView {

    /**
     * Makes the view visible.
     */
    void makeVisible();

    /**
     * Starts a timer for the game
     *
     * @param timer the given timer
     */
    void start(Timer timer);

    /**
     * Sets the view's listener to this listener.
     *
     * @param listener the given listener
     */
    void setListener(IController listener);

    /**
     * Updates the view with the new immutable model and redraws the board
     *
     * @param immutableModel ImmutableGameStateModel
     */
    void update(ImmutableGameStateModel immutableModel);

    /**
     * Updates the view with the new immutable model where the game is over and
     * @param immutableModel ImmutableGameStateModel
     * @param result IGameResult
     */
    public void update(ImmutableGameStateModel immutableModel, IGameResult result);
}
