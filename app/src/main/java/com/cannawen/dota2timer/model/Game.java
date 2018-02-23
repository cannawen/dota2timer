package com.cannawen.dota2timer.model;

import android.content.Context;
import android.util.Log;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class Game {

    private GameDisplayer displayer;
    private int secondsElapsed;
    private Timer timer;
    private Configuration config;

    public Game(Context context, GameDisplayer gameDisplayer) throws IOException {
        displayer = gameDisplayer;
        secondsElapsed = 0;
        InputStream inputStream = context.getAssets().open("settings.yml");

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
                    displayer.timeUpdated(secondsElapsed);

                    for (Setting setting : config.getSettings()) {
                        if (setting.triggeredAt(secondsElapsed)) {
                            displayer.warn(setting.getName());
                        }
                    }
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
