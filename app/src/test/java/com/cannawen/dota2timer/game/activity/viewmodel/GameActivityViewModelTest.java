package com.cannawen.dota2timer.game.activity.viewmodel;

import android.test.suitebuilder.annotation.SmallTest;

import com.cannawen.dota2timer.configuration.model.Configuration;
import com.cannawen.dota2timer.configuration.model.Event;
import com.cannawen.dota2timer.game.model.interfaces.GameState;
import com.cannawen.dota2timer.utility.TimeFormattingUtility;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SmallTest
public class GameActivityViewModelTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    GameActivityViewModel.GamePresenter presenter;
    TimeFormattingUtility timeUtility;
    GameState gameState;
    Configuration configuration;

    GameActivityViewModel viewModel;

    @Before
    public void setUp() {
        presenter = mock(GameActivityViewModel.GamePresenter.class);
        timeUtility = mock(TimeFormattingUtility.class);
        gameState = mock(GameState.class);
        configuration = mock(Configuration.class);

        viewModel = new GameActivityViewModel(presenter, timeUtility);
    }

    @Test
    public void gameUnstarted_shouldShowUnstartedGameView() {
        when(gameState.getGameTime()).thenReturn(0);
        when(timeUtility.timeString(anyInt())).thenReturn("00:00:00");
        when(gameState.getState()).thenReturn(GameState.State.UNSTARTED);

        viewModel.gameStateChanged(gameState);
        verify(presenter).showUnstartedGameView("00:00:00");
    }

    @Test
    public void gamePlaying_showPlayingGameView_noEvents() {
        when(gameState.getConfiguration()).thenReturn(configuration);
        when(gameState.getGameTime()).thenReturn(3601);
        when(timeUtility.timeString(anyInt())).thenReturn("01:00:01");
        when(gameState.getState()).thenReturn(GameState.State.PLAYING);

        viewModel.gameStateChanged(gameState);
        verify(presenter).showPlayingGameView("01:00:01", Collections.emptyList());
    }

    @Test
    public void gamePlaying_showPlayingGameView_eventTriggered() {
        Event triggeredEvent_A = mock(Event.class);
        when(triggeredEvent_A.getSpokenName()).thenReturn("A");
        when(triggeredEvent_A.triggeredAt(70)).thenReturn(true);

        Event nonTriggeredEvent = mock(Event.class);
        when(nonTriggeredEvent.triggeredAt(70)).thenReturn(false);

        Event triggeredEvent_B = mock(Event.class);
        when(triggeredEvent_B.getSpokenName()).thenReturn("B");
        when(triggeredEvent_B.triggeredAt(70)).thenReturn(true);

        when(gameState.getConfiguration()).thenReturn(configuration);

        when(configuration.getEvents()).thenReturn(Arrays.asList(
                triggeredEvent_A,
                nonTriggeredEvent,
                triggeredEvent_B
        ));

        when(gameState.getGameTime()).thenReturn(70);
        when(timeUtility.timeString(anyInt())).thenReturn("00:01:10");
        when(gameState.getState()).thenReturn(GameState.State.PLAYING);

        viewModel.gameStateChanged(gameState);
        verify(presenter).showPlayingGameView("00:01:10", Arrays.asList("A", "B"));
    }

    @Test
    public void gamePaused_showPausedGameView() {
        when(gameState.getGameTime()).thenReturn(0);
        when(timeUtility.timeString(anyInt())).thenReturn("00:00:00");
        when(gameState.getState()).thenReturn(GameState.State.PAUSED);

        viewModel.gameStateChanged(gameState);
        verify(presenter).showPausedGameView("00:00:00");
    }

    @Test
    public void gameFinished_showFinishedGameView() {
        when(gameState.getGameTime()).thenReturn(0);
        when(timeUtility.timeString(anyInt())).thenReturn("00:00:00");
        when(gameState.getState()).thenReturn(GameState.State.FINISHED);

        viewModel.gameStateChanged(gameState);
        verify(presenter).showFinishedGameView("00:00:00");
    }
}
