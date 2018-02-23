package com.cannawen.dota2timer.model;

public interface GameDisplayer {
    void showTime(int secondsElapsed);

    void notify(String eventString);
}
