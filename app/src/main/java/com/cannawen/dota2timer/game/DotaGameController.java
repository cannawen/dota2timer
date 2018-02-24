package com.cannawen.dota2timer.game;

import com.cannawen.dota2timer.configuration.Configuration;
import com.cannawen.dota2timer.configuration.Event;

import java.util.Timer;
import java.util.TimerTask;

public class DotaGameController implements GameController {

    private GameDisplayer displayer;
    private int secondsElapsed;
    private Timer timer;
    private Configuration config;

    private @State
    int state;

    public DotaGameController(Configuration config, GameDisplayer gameDisplayer) {
        this.config = config;
        this.displayer = gameDisplayer;

        initializeState();
    }

    @Override
    public void start() {
        state = State.PLAYING;
        updateDisplayer(false);
    }

    @Override
    public void stop() {
        state = State.FINISHED;
        updateDisplayer(false);
    }

    @Override
    public void pause() {
        state = State.PAUSED;
        updateDisplayer(false);
    }

    @Override
    public void resume() {
        state = State.PLAYING;
        updateDisplayer(false);
    }

    @Override
    public void increaseTime() {
        secondsElapsed++;
        updateDisplayer(false);
    }

    @Override
    public void decreaseTime() {
        secondsElapsed--;
        updateDisplayer(false);
    }

    @Override
    public @State
    int getState() {
        return state;
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
        updateDisplayer(true);
    }

    private void updateDisplayer(boolean triggerEvents) {
        displayer.showTime(secondsElapsed);

        for (Event event : config.getEvents()) {
            if (triggerEvents && event.triggeredAt(secondsElapsed)) {
                displayer.notify(event.getName());
            }
        }
    }
}
