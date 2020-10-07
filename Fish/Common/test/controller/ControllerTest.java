package controller;


import java.awt.Point;
import java.util.ArrayList;
import model.GameBoard;
import model.IGameBoard;
import model.Tile;
import org.junit.Test;
import view.IView;
import view.VisualView;

import static org.junit.Assert.*;


public class ControllerTest {

    private IGameBoard model;
    private IView view;
    private Controller controller;

    private void init() {
        this.model =  new GameBoard(5,5, new ArrayList<>() ,4, 0);
        this.view = new VisualView(model.getGameBoard(), model.getCanvas());

        this.controller = new Controller();

    }

    @Test (expected = IllegalArgumentException.class)
    public void testControlVoidModel() {
        init();

        this.controller.control(null, this.view);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testControlVoidView() {
        init();

        this.controller.control(this.model, null);
    }
}
