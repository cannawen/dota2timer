package com.cannawen.dota2timer.activity.game;

import android.test.suitebuilder.annotation.SmallTest;

import com.cannawen.dota2timer.configuration.Configuration;
import com.cannawen.dota2timer.configuration.Event;
import com.cannawen.dota2timer.game.interfaces.GameState;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SmallTest
public class GameActivityViewModelTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    GameActivityViewModel viewModel;

    GameActivityViewModel.GamePresenter presenter;
    GameState gameState;
    Configuration configuration;

    @Before
    public void setUp() throws Exception {
        presenter = mock(GameActivityViewModel.GamePresenter.class);
        gameState = mock(GameState.class);
        configuration = mock(Configuration.class);

        when(gameState.getConfiguration()).thenReturn(configuration);
        when(gameState.getGameTime()).thenReturn(0);
        when(gameState.getState()).thenReturn(GameState.State.UNSTARTED);

        viewModel = new GameActivityViewModel(presenter);
    }

    @Test
    public void gameUnstarted_shouldShowUnstartedGameView() throws Exception {
        viewModel.gameStateChanged(gameState);
        verify(presenter).showUnstartedGameView();
    }

    @Test
    public void gamePlaying_showPlayingGameView_noEvents() throws Exception {
        when(gameState.getState()).thenReturn(GameState.State.PLAYING);
        when(gameState.getGameTime()).thenReturn(3601);

        viewModel.gameStateChanged(gameState);
        verify(presenter).showPlayingGameView("01:00:01", Collections.emptyList());
    }

    @Test
    public void gamePlaying_showPlayingGameView_eventTriggered() throws Exception {
        Event triggeredEvent_A = mock(Event.class);
        when(triggeredEvent_A.getName()).thenReturn("A");
        when(triggeredEvent_A.triggeredAt(70)).thenReturn(true);

        Event nonTriggeredEvent = mock(Event.class);
        when(nonTriggeredEvent.triggeredAt(69)).thenReturn(true);
        when(nonTriggeredEvent.triggeredAt(70)).thenReturn(false);
        when(nonTriggeredEvent.triggeredAt(71)).thenReturn(true);

        Event triggeredEvent_B = mock(Event.class);
        when(triggeredEvent_B.getName()).thenReturn("B");
        when(triggeredEvent_B.triggeredAt(70)).thenReturn(true);

        when(configuration.getEvents()).thenReturn(Arrays.asList(
                triggeredEvent_A,
                nonTriggeredEvent,
                triggeredEvent_B
        ));

        when(gameState.getState()).thenReturn(GameState.State.PLAYING);
        when(gameState.getGameTime()).thenReturn(70);

        viewModel.gameStateChanged(gameState);
        verify(presenter).showPlayingGameView("00:01:10", Arrays.asList("A", "B"));
    }

    @Test
    public void gamePaused_showPausedGameView() throws Exception {
        when(gameState.getState()).thenReturn(GameState.State.PAUSED);

        viewModel.gameStateChanged(gameState);
        verify(presenter).showPausedGameView("00:00:00");
    }

    @Test
    public void gamePaused_showPausedGameView_negativeTime() throws Exception {
        when(gameState.getState()).thenReturn(GameState.State.PAUSED);
        when(gameState.getGameTime()).thenReturn(-75);

        viewModel.gameStateChanged(gameState);
        verify(presenter).showPausedGameView("-00:01:15");
    }

    @Test
    public void gameFinished_showFinishedGameView() throws Exception {
        when(gameState.getState()).thenReturn(GameState.State.FINISHED);

        viewModel.gameStateChanged(gameState);
        verify(presenter).showFinishedGameView();
    }
}
