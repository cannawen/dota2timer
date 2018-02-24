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
    private GameDisplayer displayer;

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

                displayer.configurePlayingGameView(timeString(gameState.getGameTime()), eventNames);
                break;
            }
            case GameState.State.PAUSED: {
                displayer.configurePausedGameView(timeString(gameState.getGameTime()));
                break;
            }
            case GameState.State.UNSTARTED:
            case GameState.State.FINISHED:
            default: {
                displayer.showNonStartedGame();
                break;
            }
        }
    }

    private String timeString(int secondsElapsed) {
        String signString = "";
        if (secondsElapsed < 0) {
            signString = "-";
            secondsElapsed = secondsElapsed * -1;
        }

        int hours = secondsElapsed / 3600;
        final int minutes = (secondsElapsed % 3600) / 60;
        int seconds = secondsElapsed % 60;

        return String.format("%s%02d:%02d:%02d", signString, hours, minutes, seconds);
    }

    public interface GameDisplayer {
        void configurePlayingGameView(String time, List<String> events);

        void configurePausedGameView(String time);

        void showNonStartedGame();
    }

}
