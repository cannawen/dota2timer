package com.cannawen.dota2timer.game.model.interfaces;

import com.cannawen.dota2timer.configuration.model.Configuration;
import com.cannawen.dota2timer.timer.TimerListener;

import java.util.List;

public interface Game extends TimerListener {
    List<String> voiceCommandKeywords();

    boolean hasStarted();

    void start();

    void end();

    void reset();

    void pauseOrResume();

    void increaseTime();

    void decreaseTime();

    void setConfiguration(Configuration configuration);

    void updateTime(int time);

    void note(String event);
}
