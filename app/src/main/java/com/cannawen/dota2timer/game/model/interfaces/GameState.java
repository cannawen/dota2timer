package com.cannawen.dota2timer.game.model.interfaces;

import android.support.annotation.IntDef;

import com.cannawen.dota2timer.configuration.model.Configuration;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import lombok.Getter;
import lombok.Setter;

public abstract class GameState {
    @Getter
    @State
    protected int state;
    @Getter
    protected int gameTime;
    @Getter
    @Setter
    protected Configuration configuration;

    @IntDef({State.UNSTARTED, State.PLAYING, State.PAUSED, State.FINISHED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
        int UNSTARTED = 0;
        int PLAYING = 1;
        int PAUSED = 2;
        int FINISHED = 3;
    }
}
