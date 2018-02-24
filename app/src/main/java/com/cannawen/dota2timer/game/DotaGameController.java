package com.cannawen.dota2timer.game;

import com.cannawen.dota2timer.configuration.Configuration;
import com.cannawen.dota2timer.configuration.Event;

import java.io.IOException;
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
    }

    @Override
    public void stop() {
        state = State.FINISHED;
    }

    @Override
    public void pause() {
        state = State.PAUSED;
    }

    @Override
    public void resume() {
        state = State.PLAYING;
    }

    @Override
    public void increaseTime() {
        secondsElapsed++;
        displayer.showTime(secondsElapsed);
    }

    @Override
    public void decreaseTime() {
        secondsElapsed--;
        displayer.showTime(secondsElapsed);
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
                displayer.showTime(secondsElapsed);

                for (Event event : config.getEvents()) {
                    if (event.triggeredAt(secondsElapsed)) {
                        displayer.notify(event.getName());
                    }
                }
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
        }
    }
}
