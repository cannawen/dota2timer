package com.cannawen.dota2timer.game;

import com.cannawen.dota2timer.configuration.Configuration;
import com.cannawen.dota2timer.configuration.Event;
import com.cannawen.dota2timer.game.interfaces.Game;
import com.cannawen.dota2timer.game.interfaces.GameState;
import com.cannawen.dota2timer.game.interfaces.GameStateChangeListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class DotaGame implements Game, GameState {
    protected GameStateChangeListener listener;

    private int secondsElapsed;
    private Timer timer;
    private Configuration config;

    private @State
    int state;

    public DotaGame(Configuration config, GameStateChangeListener listener) {
        this.config = config;
        this.listener = listener;

        initializeState();
    }

    @Override
    public void start() {
        state = State.PLAYING;
        triggerListener();
    }

    @Override
    public void stop() {
        state = State.FINISHED;
        triggerListener();
    }

    @Override
    public void pauseOrResume() {
        if (state == State.PAUSED) {
            state = State.PLAYING;
        } else if (state == State.PLAYING) {
            state = State.PAUSED;
        }
        triggerListener();
    }

    @Override
    public void increaseTime() {
        secondsElapsed++;
        triggerListener();
    }

    @Override
    public void decreaseTime() {
        secondsElapsed--;
        triggerListener();
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
        triggerListener();
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
        triggerListener();
    }

    private void triggerListener() {
        listener.gameStateChanged(this);
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
