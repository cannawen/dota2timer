package com.cannawen.dota2timer.activity;

import com.cannawen.dota2timer.configuration.Configuration;
import com.cannawen.dota2timer.configuration.Event;
import com.cannawen.dota2timer.game.interfaces.GameState;
import com.cannawen.dota2timer.game.interfaces.GameStateChangeListener;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GameActivityViewModel implements GameStateChangeListener {
    private GamePresenter presenter;

    @Override
    public void gameStateChanged(final GameState gameState) {
        switch (gameState.getState()) {
            case GameState.State.PLAYING: {
                Configuration config = gameState.getConfiguration();
                List<String> eventNames = new ArrayList<>();
                for (Event event : config.getEvents()) {
                    if (event.triggeredAt(gameState.getGameTime())) {
                        eventNames.add(event.getName());
                    }
                }

                presenter.configurePlayingGameView(timeString(gameState), eventNames);
                break;
            }
            case GameState.State.PAUSED: {
                presenter.configurePausedGameView(timeString(gameState));
                break;
            }
            case GameState.State.UNSTARTED:
            case GameState.State.FINISHED:
            default: {
                presenter.showNonStartedGame();
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
        void configurePlayingGameView(String time, List<String> events);

        void configurePausedGameView(String time);

        void showNonStartedGame();
    }
}
