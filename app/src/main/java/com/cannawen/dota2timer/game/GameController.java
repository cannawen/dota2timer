package com.cannawen.dota2timer.game;

import android.content.Context;
import android.util.Log;

import com.cannawen.dota2timer.configuration.Configuration;
import com.cannawen.dota2timer.configuration.Event;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class GameController {

    private GameDisplayer displayer;
    private int secondsElapsed;
    private Timer timer;
    private Configuration config;

    public GameController(Context context, GameDisplayer gameDisplayer) throws IOException {
        displayer = gameDisplayer;
        secondsElapsed = 0;
        InputStream inputStream = context.getAssets().open("configuration.yml");

        Yaml yaml = new Yaml();
        config = yaml.loadAs(inputStream, Configuration.class);
        Log.d("", config.toString());
    }

    public void start() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    displayer.showTime(secondsElapsed);

                    for (Event event : config.getEvents()) {
                        if (event.triggeredAt(secondsElapsed)) {
                            displayer.notify(event.getName());
                        }
                    }
                    secondsElapsed++;
                }
            }, 0, 1000);
        }
    }

    public void stop() {
        pause();
    }

    public void pause() {
        if (timer != null) {
            timer.cancel();
        }
        timer = null;
    }

    public boolean isPaused() {
        return timer == null;
    }

    public void resume() {
        start();
    }

    public void increaseTime() {
        secondsElapsed++;
        displayer.showTime(secondsElapsed);
    }

    public void decreaseTime() {
        secondsElapsed--;
        displayer.showTime(secondsElapsed);
    }
}
