package com.cannawen.dota2timer.game;

public interface GameController extends GameState {
    void start();

    void stop();

    void pause();

    void resume();

    void increaseTime();

    void decreaseTime();
}
