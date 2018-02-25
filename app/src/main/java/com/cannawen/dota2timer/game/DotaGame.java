package com.cannawen.dota2timer.game;

import com.cannawen.dota2timer.configuration.Configuration;
import com.cannawen.dota2timer.game.interfaces.Game;
import com.cannawen.dota2timer.game.interfaces.GameState;
import com.cannawen.dota2timer.game.interfaces.GameStateChangeListener;

import java.util.Timer;
import java.util.TimerTask;

public class DotaGame extends GameState implements Game {
    static final private int GAME_START_TIME = -75;

    protected GameStateChangeListener listener;
    private Timer timer;

    public DotaGame(Configuration configuration, GameStateChangeListener listener) {
        super(configuration);
        this.listener = listener;

        initializeState();
    }

    @Override
    public void start() {
        state = State.PLAYING;
        triggerListener();
    }

    @Override
    public void end() {
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
        gameTime++;
        triggerListener();
    }

    @Override
    public void decreaseTime() {
        gameTime--;
        triggerListener();
    }

    private void initializeState() {
        gameTime = GAME_START_TIME;
        state = State.UNSTARTED;

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                tick();
            }
        }, 0, 1000);

        triggerListener();
    }

    private void tick() {
        switch (state) {
            case State.PLAYING: {
                gameTime++;
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
}
