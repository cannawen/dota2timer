package com.cannawen.dota2timer.timer;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public class SecondTimer extends AbstractTimer {
    private Timer timer;

    private Handler handler = new Handler();

    public SecondTimer() {
        this.timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (listener != null) {
                    handler.post(() -> listener.tick());
                }
            }
        }, 0, 1000);
    }
}
