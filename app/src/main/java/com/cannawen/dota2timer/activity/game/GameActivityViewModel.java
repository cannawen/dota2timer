package com.cannawen.dota2timer.activity.game;

import com.cannawen.dota2timer.configuration.Event;
import com.cannawen.dota2timer.game.interfaces.GameState;
import com.cannawen.dota2timer.game.interfaces.GameStateChangeListener;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GameActivityViewModel implements GameStateChangeListener {
    private GamePresenter presenter;

    @Override
    public void gameStateChanged(final GameState gameState) {
        switch (gameState.getState()) {
            case GameState.State.PLAYING: {
                List<String> eventNames = gameState
                        .getConfiguration()
                        .getEvents()
                        .stream()
                        .filter(event -> event.triggeredAt(gameState.getGameTime()))
                        .map(Event::getName)
                        .collect(Collectors.toList());

                presenter.showPlayingGameView(timeString(gameState), eventNames);
                break;
            }
            case GameState.State.PAUSED: {
                presenter.showPausedGameView(timeString(gameState));
                break;
            }
            case GameState.State.FINISHED: {
                presenter.showFinishedGameView();
            }
            case GameState.State.UNSTARTED:
            default: {
                presenter.showUnstartedGameView();
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
        void showUnstartedGameView();

        void showPlayingGameView(String time, List<String> eventStrings);

        void showPausedGameView(String time);

        void showFinishedGameView();
    }
}
