package com.cannawen.dota2timer.game;

import com.cannawen.dota2timer.configuration.Configuration;
import com.cannawen.dota2timer.configuration.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DotaGameController implements GameController {

    private GameStateChangeListener displayer;
    private int secondsElapsed;
    private Timer timer;
    private Configuration config;

    private @State int state;

    public DotaGameController(Configuration config, GameStateChangeListener gameStateChangeListener) {
        this.config = config;
        this.displayer = gameStateChangeListener;

        initializeState();
    }

    @Override
    public void start() {
        state = State.PLAYING;
        updateDisplayer();
    }

    @Override
    public void stop() {
        state = State.FINISHED;
        updateDisplayer();
    }

    @Override
    public void pause() {
        state = State.PAUSED;
        updateDisplayer();
    }

    @Override
    public void resume() {
        state = State.PLAYING;
        updateDisplayer();
    }

    @Override
    public void increaseTime() {
        secondsElapsed++;
        updateDisplayer();
    }

    @Override
    public void decreaseTime() {
        secondsElapsed--;
        updateDisplayer();
    }

    private void initializeState() {
        secondsElapsed = -75;
        state = State.UNSTARTED;

        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    tick();
                }
            }, 0, 1000);
        }
    }

    private void tick() {
        switch (state) {
            case State.PLAYING: {
                secondsElapsed++;
                break;
            }
            case State.FINISHED: {
                timer.cancel();
                timer = null;
                break;
            }
            case State.PAUSED:
            case State.UNSTARTED:
            default:
                break;
        }
        updateDisplayer();
    }

    private void updateDisplayer() {
        displayer.gameStateChanged(this);
    }

    @Override
    public int elapsedTime() {
        return secondsElapsed;
    }

    @Override
    public List<String> events() {
        List<String> eventNames = new ArrayList<>();
        for (Event event : config.getEvents()) {
            if (event.triggeredAt(secondsElapsed)) {
                eventNames.add(event.getName());
            }
        }
        return eventNames;
    }

    @Override
    public @State
    int getState() {
        return state;
    }

}
