package com.cannawen.dota2timer.timer;

import java.util.Timer;
import java.util.TimerTask;

public class SecondTimer extends AbstractTimer {
    private Timer timer;

    public SecondTimer() {
        this.timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.tick();
                }
            }
        }, 0, 1000);
    }
}
