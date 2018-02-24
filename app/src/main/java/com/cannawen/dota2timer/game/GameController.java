package com.cannawen.dota2timer.game;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface GameController {
    void start();

    void stop();

    void pause();

    void resume();

    void increaseTime();

    void decreaseTime();

    @State
    int getState();

    @IntDef({State.UNSTARTED, State.PLAYING, State.PAUSED, State.FINISHED})
    @Retention(RetentionPolicy.SOURCE)
    @interface State {
        int UNSTARTED = 0;
        int PLAYING = 1;
        int PAUSED = 2;
        int FINISHED = 3;
    }
}
