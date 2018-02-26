package com.cannawen.dota2timer.timer;

import lombok.Setter;

public abstract class AbstractTimer {
    @Setter
    protected TimerListener listener;
}
