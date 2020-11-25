package model;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.games.IGameAction;
import model.games.IGameResult;
import model.games.IReferee;
import model.games.PlayerAI;
import model.games.Referee;
import model.humans.GameVisualizer;
import model.humans.IGameVisualizer;
import model.state.ImmutableGameState;
import model.state.ImmutableGameStateModel;
import model.strategy.Strategy;
import model.tree.PlayerInterface;
import org.junit.Before;
import org.junit.Test;


public class GameVisualizerTest {

    private IGameVisualizer visualizer;
    private IReferee referee;
    private PlayerInterface ai1, ai2;
    private ImmutableGameStateModel initState;
    private List<IGameAction> actions;
    private IGameResult result;

    @Before
    public void init() {
        this.ai1 = new PlayerAI(new Strategy(), 2);
        this.ai2 = new PlayerAI(new Strategy(), 2, 10, "Billy");
        List<PlayerInterface> players = Arrays.asList(this.ai1, this.ai2);
        this.referee = new Referee(players, 4,4,4);
        this.result = this.referee.runGame();
        this.actions = this.referee.getOngoingActions();
        ImmutableGameStateModel state = new ImmutableGameState(this.referee.getInitialGameState());
        this.initState = new ImmutableGameState(state.clone());
        this.visualizer = new GameVisualizer(state, this.actions, this.result, new ArrayList<>());
    }

    @Test (expected = IllegalArgumentException.class)
    public void constructorNullState() {
        this.visualizer = new GameVisualizer(null, this.actions, this.result, new ArrayList<>());
    }


    @Test (expected = IllegalArgumentException.class)
    public void constructorNullActions() {
        this.visualizer = new GameVisualizer(this.initState, null,
            this.result, new ArrayList<>());
    }


    @Test (expected = IllegalArgumentException.class)
    public void constructorNullResult() {
        this.visualizer = new GameVisualizer(this.initState, this.actions,
            null, new ArrayList<>());
    }

    @Test (expected = IllegalArgumentException.class)
    public void constructorNullObservers() {
        this.visualizer = new GameVisualizer(this.initState, null,
            this.result, null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void nextSceneNullAction() {
        IGameAction action = actions.get(0);
        ImmutableGameStateModel nextScene = this.visualizer.nextScene(null, this.initState);

        ImmutableGameStateModel nextGameState = this.initState.getNextGameState(action);

        assertEquals(nextGameState.clone(), nextScene.clone());
    }

    @Test (expected = IllegalArgumentException.class)
    public void nextSceneNullState() {
        IGameAction action = actions.get(0);
        ImmutableGameStateModel nextScene = this.visualizer.nextScene(action, null);

        ImmutableGameStateModel nextGameState = this.initState.getNextGameState(action);

        assertEquals(nextGameState.clone(), nextScene.clone());
    }

    @Test
    public void nextScene() {
        IGameAction action = actions.get(0);
        ImmutableGameStateModel nextScene = this.visualizer.nextScene(action, this.initState);

        ImmutableGameStateModel nextGameState = this.initState.getNextGameState(action);

        assertEquals(nextGameState.clone(), nextScene.clone());
    }
}
