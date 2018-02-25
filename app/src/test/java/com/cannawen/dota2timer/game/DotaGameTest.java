package com.cannawen.dota2timer.game;

import android.test.suitebuilder.annotation.SmallTest;

import com.cannawen.dota2timer.configuration.Configuration;
import com.cannawen.dota2timer.game.interfaces.GameState;
import com.cannawen.dota2timer.game.interfaces.GameStateChangeListener;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SmallTest
public class DotaGameTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    GameStateChangeListener listener;
    Configuration configuration;

    DotaGame game;

    @Before
    public void setUp() throws Exception {
        listener = mock(GameStateChangeListener.class);
        configuration = mock(Configuration.class);

        game = new DotaGame(configuration, listener);
    }

    @Test
    public void shouldBeInitialized() throws Exception {
        assertEquals(game.getGameTime(), -75);
        assertEquals(game.getState(), GameState.State.UNSTARTED);
    }

//    @Test
//    public void onStart_shouldUpdateListener_updateStateToStarted() throws Exception {
//        game.start();
//
//        ArgumentCaptor<GameState> captor = ArgumentCaptor.forClass(GameState.class);
//        verify(listener).gameStateChanged(captor.capture());
//        GameState newState = captor.getValue();
//
//        assertEquals(newState.getState(), GameState.State.PLAYING);
//    }
}
