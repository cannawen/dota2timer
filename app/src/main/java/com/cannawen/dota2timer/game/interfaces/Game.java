package com.cannawen.dota2timer.game.interfaces;

import com.cannawen.dota2timer.configuration.Configuration;

public interface Game {
    void start();

    void end();

    void pauseOrResume();

    void increaseTime();

    void decreaseTime();

    void setConfiguration(Configuration configuration);
}
