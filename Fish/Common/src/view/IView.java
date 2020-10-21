package view;

import java.util.List;

import javax.swing.*;

import controller.IController;
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
     * @param viablePaths current viable paths to display
     */
    void update(ImmutableGameStateModel immutableModel, List<List<Tile>> viablePaths);
}
