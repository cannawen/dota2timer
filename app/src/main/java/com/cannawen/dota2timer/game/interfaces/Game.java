package com.cannawen.dota2timer.game.interfaces;

public interface Game {
    void start();

    void stop();

    void pauseOrResume();

    void increaseTime();

    void decreaseTime();
}
