package com.cannawen.dota2timer.game.activity.viewmodel;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.cannawen.dota2timer.configuration.model.Event;
import com.cannawen.dota2timer.game.model.interfaces.GameState;
import com.cannawen.dota2timer.game.model.interfaces.GameStateChangeListener;
import com.cannawen.dota2timer.utility.TimeFormattingUtility;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GameActivityViewModel implements GameStateChangeListener {
    private GamePresenter presenter;
    private TimeFormattingUtility timeUtility;

    @Override
    public void gameStateChanged(final GameState gameState) {
        String timeString = timeUtility.timeString(gameState.getGameTime());

        switch (gameState.getState()) {
            case GameState.State.PLAYING: {
                List<String> eventNames = Stream.of(gameState.getConfiguration().getEvents())
                        .filter(event -> event.triggeredAt(gameState.getGameTime()))
                        .map(Event::getSpokenName)
                        .collect(Collectors.toList());

                presenter.showPlayingGameView(timeString, eventNames);
                break;
            }
            case GameState.State.PAUSED: {
                presenter.showPausedGameView(timeString);
                break;
            }
            case GameState.State.FINISHED: {
                presenter.showFinishedGameView(timeString);
            }
            case GameState.State.UNSTARTED:
            default: {
                presenter.showUnstartedGameView(timeString);
                break;
            }
        }
    }

    public interface GamePresenter {
        void showUnstartedGameView(String time);

        void showPlayingGameView(String time, List<String> eventStrings);

        void showPausedGameView(String time);

        void showFinishedGameView(String time);
    }
}
