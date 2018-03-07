package com.cannawen.dota2timer.timer;

import lombok.Setter;

public abstract class AbstractTimer {
    @Setter
    TimerListener listener;

    public abstract void syncSecond();
}
