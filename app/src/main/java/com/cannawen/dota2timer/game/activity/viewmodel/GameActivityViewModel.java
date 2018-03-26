package com.cannawen.dota2timer.game.activity.viewmodel;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.cannawen.dota2timer.configuration.model.Event;
import com.cannawen.dota2timer.game.model.interfaces.GameState;
import com.cannawen.dota2timer.game.model.interfaces.GameStateChangeListener;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GameActivityViewModel implements GameStateChangeListener {
    private GamePresenter presenter;

    @Override
    public void gameStateChanged(final GameState gameState) {
        switch (gameState.getState()) {
            case GameState.State.PLAYING: {
                List<String> eventNames = Stream.of(gameState.getConfiguration().getEvents())
                        .filter(event -> event.triggeredAt(gameState.getGameTime()))
                        .map(Event::getSpokenName)
                        .collect(Collectors.toList());

                presenter.showPlayingGameView(timeString(gameState), eventNames);
                break;
            }
            case GameState.State.PAUSED: {
                presenter.showPausedGameView(timeString(gameState));
                break;
            }
            case GameState.State.FINISHED: {
                presenter.showFinishedGameView(timeString(gameState));
            }
            case GameState.State.UNSTARTED:
            default: {
                presenter.showUnstartedGameView(timeString(gameState));
                break;
            }
        }
    }

    private String timeString(GameState gameState) {
        int secondsElapsed = gameState.getGameTime();

        String signString = "";
        if (secondsElapsed < 0) {
            signString = "-";
            secondsElapsed = secondsElapsed * -1;
        }

        int hours = secondsElapsed / 3600;
        int minutes = (secondsElapsed % 3600) / 60;
        int seconds = secondsElapsed % 60;

        return String.format("%s%02d:%02d:%02d", signString, hours, minutes, seconds);
    }

    public interface GamePresenter {
        void showUnstartedGameView(String time);

        void showPlayingGameView(String time, List<String> eventStrings);

        void showPausedGameView(String time);

        void showFinishedGameView(String time);
    }
}
