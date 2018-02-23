package com.cannawen.dota2timer.model;

public interface GameDisplayer {
    void timeUpdated(int totalSecondsElapsed);

    void warn(String warning);
}
