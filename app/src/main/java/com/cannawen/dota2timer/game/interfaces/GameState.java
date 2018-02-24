package com.cannawen.dota2timer.game.interfaces;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public interface GameState {
    int elapsedTime();

    List<String> events();

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
