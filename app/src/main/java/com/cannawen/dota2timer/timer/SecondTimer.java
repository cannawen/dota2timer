package com.cannawen.dota2timer.timer;

import java.util.Timer;
import java.util.TimerTask;

public class SecondTimer {
    Timer timer;

    public SecondTimer(TimerListener listener) {
        this.timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                listener.tick();
            }
        }, 0, 1000);
    }

    public void cancel() {
        timer.cancel();
        timer = null;
    }
}
