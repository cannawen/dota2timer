package com.cannawen.dota2timer.game.interfaces;

import com.cannawen.dota2timer.configuration.Configuration;
import com.cannawen.dota2timer.timer.TimerListener;

public interface Game extends TimerListener {
    boolean hasStarted();

    void start();

    void end();

    void reset();

    void pauseOrResume();

    void increaseTime();

    void decreaseTime();

    void setConfiguration(Configuration configuration);
}
