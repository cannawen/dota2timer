package com.cannawen.dota2timer.game.model;

import com.cannawen.dota2timer.configuration.model.Configuration;
import com.cannawen.dota2timer.configuration.model.Event;
import com.cannawen.dota2timer.game.model.interfaces.Game;
import com.cannawen.dota2timer.game.model.interfaces.GameState;
import com.cannawen.dota2timer.game.model.interfaces.GameStateChangeListener;

import java.util.Arrays;
import java.util.List;

public class DotaGame extends GameState implements Game {
    static final private int GAME_START_TIME = -75;
    static final private String VOICE_KEYWORD_ROSHAN = "roshan";

    protected GameStateChangeListener listener;

    public DotaGame(GameStateChangeListener listener) {
        this.listener = listener;

        initializeState();
    }

    @Override
    public List<String> voiceCommandKeywords() {
        return Arrays.asList(VOICE_KEYWORD_ROSHAN);
    }

    @Override
    public boolean hasStarted() {
        return state != State.UNSTARTED;
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
    public void reset() {
        initializeState();
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
    public void updateTime(int time) {
        gameTime = time;
        triggerListener();
    }

    @Override
    public void decreaseTime() {
        gameTime--;
        triggerListener();
    }

    @Override
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        triggerListener();
    }

    @Override
    public void note(String event) {
        switch (event) {
            case VOICE_KEYWORD_ROSHAN: {
                Event aegisReclaimed = Event.builder()
                        .name("Aegis reclaimed")
                        .time_initial(gameTime + 300)
                        .enabled(true)
                        .transitory(true)
                        .build();
                Event earlyEvent = Event.builder()
                        .name("Roshan early spawn")
                        .time_initial(gameTime + 480)
                        .enabled(true)
                        .transitory(true)
                        .build();
                Event lateEvent = Event.builder()
                        .name("Roshan late spawn")
                        .time_initial(gameTime + 660)
                        .enabled(true)
                        .transitory(true)
                        .build();
                configuration.createNewEvent(aegisReclaimed);
                configuration.createNewEvent(earlyEvent);
                configuration.createNewEvent(lateEvent);
            }
            break;
        }
        triggerListener();
    }

    private void initializeState() {
        gameTime = GAME_START_TIME;
        state = State.UNSTARTED;

        triggerListener();
    }

    @Override
    public void tick() {
        if (state == State.PLAYING) {
            gameTime++;
        }
        triggerListener();
    }

    private void triggerListener() {
        listener.gameStateChanged(this);
    }
}
