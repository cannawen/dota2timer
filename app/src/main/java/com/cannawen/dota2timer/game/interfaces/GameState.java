package com.cannawen.dota2timer.game.interfaces;

import android.support.annotation.IntDef;

import com.cannawen.dota2timer.configuration.Configuration;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface GameState {
    int elapsedTime();

    Configuration configuration();

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
