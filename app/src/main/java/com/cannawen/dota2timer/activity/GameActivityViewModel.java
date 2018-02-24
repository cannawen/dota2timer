package com.cannawen.dota2timer.activity;

import android.content.res.Resources;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.cannawen.dota2timer.BR;
import com.cannawen.dota2timer.R;
import com.cannawen.dota2timer.databinding.ActivityGameBinding;
import com.cannawen.dota2timer.game.interfaces.GameState;
import com.cannawen.dota2timer.game.interfaces.GameStateChangeListener;

import lombok.Getter;

public class GameActivityViewModel extends BaseObservable implements GameStateChangeListener  {

    private Resources resources;

    @Bindable @Getter
    public boolean gameInProgress;
    @Bindable @Getter
    public String pauseOrResumeString;

    public GameActivityViewModel(Resources resources, ActivityGameBinding binding) {
        this.resources = resources;
        binding.setViewModel(this);
        binding.gameStartButton.setText(startGameString());
        binding.playPauseButton.setText(pauseOrResumeString);
        binding.resetButton.setText(resetString());
    }

    public String startGameString() {
        return resources.getString(R.string.game_action_start);
    }

    public boolean updateGameInProgress(GameState gameState) {
        switch (gameState.getState()) {
            case GameState.State.PLAYING:
            case GameState.State.PAUSED: {
                return true;
            }
            case GameState.State.UNSTARTED:
            case GameState.State.FINISHED:
            default: {
                return false;
            }
        }
    }

    public String updatePauseOrResumeString(GameState gameState) {
        switch (gameState.getState()) {
            case GameState.State.PLAYING: {
                return resources.getString(R.string.game_action_pause);
            }
            case GameState.State.PAUSED: {
                return resources.getString(R.string.game_action_resume);
            }
            case GameState.State.UNSTARTED:
            case GameState.State.FINISHED:
            default: {
                return "";
            }
        }
    }

    public String resetString() {
        return resources.getString(R.string.game_action_reset);
    }

    @Override
    public void gameStateChanged(final GameState gameState) {
        gameInProgress = updateGameInProgress(gameState);
        pauseOrResumeString = updatePauseOrResumeString(gameState);
        notifyPropertyChanged(BR._all);

//        switch (gameState.getState()) {
//            case GameState.State.PLAYING: {
//                Configuration config = gameState.getConfiguration();
//                List<String> eventNames = new ArrayList<>();
//                for (Event event : config.getEvents()) {
//                    if (event.triggeredAt(gameState.getGameTime())) {
//                        eventNames.add(event.getName());
//                    }
//                }
//
//                this.timeString = timeString(gameState);
//                this.eventNames = eventNames;
//                this.showPlayingGame = true;
//                break;
//            }
//            case GameState.State.PAUSED: {
//                this.timeString = timeString(gameState);
//                this.showPausedGame = true;
//                break;
//            }
//            case GameState.State.UNSTARTED:
//            case GameState.State.FINISHED:
//            default: {
//                this.showNonStartedGame = true;
//                break;
//            }
//        }
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
}
