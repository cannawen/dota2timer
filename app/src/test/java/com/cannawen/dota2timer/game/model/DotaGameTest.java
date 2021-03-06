package com.cannawen.dota2timer.game.model;

import android.test.suitebuilder.annotation.SmallTest;

import com.cannawen.dota2timer.configuration.model.Configuration;
import com.cannawen.dota2timer.game.model.interfaces.GameState;
import com.cannawen.dota2timer.game.model.interfaces.GameState.State;
import com.cannawen.dota2timer.game.model.interfaces.GameStateChangeListener;
import com.cannawen.dota2timer.game.model.DotaGame;
import com.cannawen.dota2timer.utility.TimeFormattingUtility;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;

@SmallTest
public class DotaGameTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    GameStateChangeListener listener;
    Configuration configuration;
    TimeFormattingUtility timeUtil;

    DotaGame game;

    @Before
    public void setUp() {
        listener = mock(GameStateChangeListener.class);
        configuration = mock(Configuration.class);

        game = new DotaGame(listener, timeUtil);
        game.setConfiguration(configuration);
    }

    @Test
    public void shouldBeInitialized() {
        assertEquals(game.getGameTime(), -75);
        assertEquals(game.getState(), State.UNSTARTED);
    }

    @Test
    public void onStart_shouldUpdateListener() {
        reset(listener);
        game.start();
        verifyListenerCalledWith(State.PLAYING, -75);
    }

    @Test
    public void onPause_shouldUpdateListener() {
        game.start();

        reset(listener);
        game.pauseOrResume();
        verifyListenerCalledWith(State.PAUSED, -75);
    }

    @Test
    public void onResume_shouldUpdateListener() {
        game.start();
        game.pauseOrResume();

        reset(listener);
        game.pauseOrResume();
        verifyListenerCalledWith(State.PLAYING, -75);
    }

    @Test
    public void onEnd_shouldUpdateListener() {
        game.start();

        reset(listener);
        game.end();
        verifyListenerCalledWith(State.FINISHED, -75);
    }

    @Test
    public void onTick_playing_shouldUpdateListener_updateTime() {
        game.start();

        reset(listener);
        game.tick();
        verifyListenerCalledWith(State.PLAYING, -74);
    }

    @Test
    public void onTick_paused_shouldUpdateListener_sameTime() {
        game.start();
        game.pauseOrResume();

        reset(listener);
        game.tick();
        verifyListenerCalledWith(State.PAUSED, -75);
    }

    @Test
    public void onTick_resumed_shouldUpdateListener_updateTime() {
        game.start();
        game.pauseOrResume();
        game.pauseOrResume();

        reset(listener);
        game.tick();
        verifyListenerCalledWith(State.PLAYING, -74);
    }

    @Test
    public void onIncreaseTime_playing_shouldUpdateListener() {
        game.start();

        reset(listener);
        game.increaseTime();
        verifyListenerCalledWith(State.PLAYING, -74);
    }

    @Test
    public void onIncreaseTime_paused_shouldUpdateListener() {
        game.start();
        game.pauseOrResume();

        reset(listener);
        game.increaseTime();
        verifyListenerCalledWith(State.PAUSED, -74);
    }

    @Test
    public void onDecreaseTime_playing_shouldUpdateListener() {
        game.start();

        reset(listener);
        game.decreaseTime();
        verifyListenerCalledWith(State.PLAYING, -76);
    }

    @Test
    public void onDecreaseTime_paused_shouldUpdateListener() {
        game.start();
        game.pauseOrResume();
        
        reset(listener);
        game.decreaseTime();
        verifyListenerCalledWith(State.PAUSED, -76);
    }

    @Test
    public void ableToMixActions() {
        verifyListenerCalledWith(State.UNSTARTED, -75, false);

        reset(listener);
        game.start();
        verifyListenerCalledWith(State.PLAYING, -75);

        reset(listener);
        game.tick();
        verifyListenerCalledWith(State.PLAYING, -74);

        reset(listener);
        game.increaseTime();
        verifyListenerCalledWith(State.PLAYING, -73);

        reset(listener);
        game.pauseOrResume();
        verifyListenerCalledWith(State.PAUSED, -73);

        reset(listener);
        game.tick();
        verifyListenerCalledWith(State.PAUSED, -73);

        reset(listener);
        game.increaseTime();
        verifyListenerCalledWith(State.PAUSED, -72);

        reset(listener);
        game.pauseOrResume();
        verifyListenerCalledWith(State.PLAYING, -72);

        reset(listener);
        game.tick();
        verifyListenerCalledWith(State.PLAYING, -71);

        reset(listener);
        game.end();
        verifyListenerCalledWith(State.FINISHED, -71);
    }

    @Test
    public void onSetConfiguration_shouldUpdateListener() {
        reset(listener);
        Configuration newConfiguration = mock(Configuration.class);
        game.setConfiguration(newConfiguration);

        ArgumentCaptor<GameState> captor = ArgumentCaptor.forClass(GameState.class);
        verify(listener).gameStateChanged(captor.capture());
        GameState newState = captor.getValue();
        assertEquals(newConfiguration, newState.getConfiguration());
    }
    
    private void verifyListenerCalledWith(@State int state, int time) {
        verifyListenerCalledWith(state, time, true);
    }

    private void verifyListenerCalledWith(@State int state, int time, boolean onlyOnce) {
        ArgumentCaptor<GameState> captor = ArgumentCaptor.forClass(GameState.class);
        if (onlyOnce) {
            verify(listener).gameStateChanged(captor.capture());
        } else {
            verify(listener, atLeastOnce()).gameStateChanged(captor.capture());
        }
        GameState newState = captor.getValue();

        assertEquals(configuration, newState.getConfiguration());
        assertEquals(time, newState.getGameTime());
        assertEquals(state, newState.getState());
    }
}
