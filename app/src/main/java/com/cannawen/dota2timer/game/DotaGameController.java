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

public class DotaGameController implements GameController {

    private GameDisplayer displayer;
    private int secondsElapsed;
    private Timer timer;
    private Configuration config;

    private @State int state;

    public DotaGameController(Context context, GameDisplayer gameDisplayer) throws IOException {
        displayer = gameDisplayer;
        secondsElapsed = -75;
        InputStream inputStream = context.getAssets().open("configuration.yml");

        Yaml yaml = new Yaml();
        config = yaml.loadAs(inputStream, Configuration.class);
        Log.d("", config.toString());

        state = State.UNSTARTED;
    }

    @Override
    public void start() {
        state = State.PLAYING;
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

    @Override
    public void stop() {
        pause();
        state = State.FINISHED;
    }

    @Override
    public void pause() {
        state = State.PAUSED;
        if (timer != null) {
            timer.cancel();
        }
        timer = null;
    }

    @Override
    public void resume() {
        start();
    }

    @Override
    public void increaseTime() {
        secondsElapsed++;
        displayer.showTime(secondsElapsed);
    }

    @Override
    public void decreaseTime() {
        secondsElapsed--;
        displayer.showTime(secondsElapsed);
    }

    @Override
    public @State int getState() {
        return state;
    }
}
