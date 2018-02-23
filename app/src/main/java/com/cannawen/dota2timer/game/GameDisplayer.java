package com.cannawen.dota2timer.game;

public interface GameDisplayer {
    void showTime(int secondsElapsed);

    void notify(String eventString);
}
