package com.cannawen.dota2timer;

import java.util.Timer;
import java.util.TimerTask;

public class Game {

    private GameDisplayer displayer;
    private int secondsElapsed;
    private Timer timer;

    public Game(GameDisplayer gameDisplayer) {
        displayer = gameDisplayer;
        secondsElapsed = 0;
    }

    public void start() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    displayer.timeUpdated(secondsElapsed);
                    secondsElapsed++;
                }
            }, 0, 1000);
        }
    }

    public void pause() {
        if (timer != null) {
            timer.cancel();
        }
        timer = null;
    }
}
