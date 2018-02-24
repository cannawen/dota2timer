package com.cannawen.dota2timer.game.interfaces;

public interface Game {
    void start();

    void end();

    void pauseOrResume();

    void increaseTime();

    void decreaseTime();
}
