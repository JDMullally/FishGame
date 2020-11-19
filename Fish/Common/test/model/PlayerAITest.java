package model;


import model.games.PlayerAI;
import model.strategy.Strategy;
import model.testStrategies.TimeoutStrategy;
import model.tree.PlayerInterface;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class PlayerAITest {

    PlayerInterface ai1;
    PlayerInterface ai2;

    @Before
    public void init() {
        this.ai1 = new PlayerAI(new Strategy(), 2);
        this.ai2 = new PlayerAI(new Strategy(), 2, 10, "Billy");
    }

    @Test (expected = IllegalArgumentException.class)
    public void testConstructorNullStrategy() {
        new PlayerAI(null, 2);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testConstructorNullStrategy4Args() {
        new PlayerAI(null, 2, 20, "asdf");
    }

    @Test (expected = IllegalArgumentException.class)
    public void testConstructorUIDNull() {
        new PlayerAI(new Strategy(), 2, 20, null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testConstructorUIDTooLong() {
        new PlayerAI(new Strategy(), 2, 20, "1111111111111");
    }

    @Test
    public void testGetPlayerAge() {
        assertEquals(1, this.ai1.getPlayerAge());
        assertEquals(10, this.ai2.getPlayerAge());
    }

    @Test
    public void getPlayerID() {
        assertEquals("AI Player", this.ai1.getPlayerID());
        assertEquals("Billy", this.ai2.getPlayerID());
    }

    @Test
    public void testTournamentHasStarted() {
        assertTrue(this.ai1.tournamentHasStarted());
        assertTrue(this.ai2.tournamentHasStarted());
    }

    @Test
    public void testGameHasStarted() {
        assertTrue(this.ai1.gameHasStarted());
        assertTrue(this.ai2.gameHasStarted());
    }

    @Test
    public void testTournamentResults() {
        assertTrue(this.ai1.tournamentResults(false));
        assertTrue(this.ai2.tournamentResults(true));
    }

    @Test
    public void testEquals() {
        assertEquals(new PlayerAI(new Strategy(), 2), this.ai1);
        assertNotEquals(new PlayerAI(new TimeoutStrategy(), 2), this.ai1);
        assertNotEquals(new PlayerAI(new Strategy(), 1), this.ai1);

        assertEquals(new PlayerAI(new Strategy(), 2, 10, "Billy"), this.ai2);
        assertNotEquals(new PlayerAI(new Strategy(), 2, 9, "Billy"), this.ai2);
        assertNotEquals(new PlayerAI(new Strategy(), 2, 10, "Jimmy"), this.ai2);
    }
}
