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

    private @State
    int state;

    public DotaGameController(Context context, GameDisplayer gameDisplayer) throws IOException {
        displayer = gameDisplayer;
        secondsElapsed = -75;

        InputStream inputStream = context.getAssets().open("configuration.yml");

        config = new Yaml().loadAs(inputStream, Configuration.class);

        state = State.UNSTARTED;

        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                        tick();
                }
            }, 0, 1000);
        }
    }

    private void tick() {
        switch (state) {
            case State.PLAYING: {
                secondsElapsed++;
                displayer.showTime(secondsElapsed);

                for (Event event : config.getEvents()) {
                    if (event.triggeredAt(secondsElapsed)) {
                        displayer.notify(event.getName());
                    }
                }
                break;
            }
            case State.FINISHED: {
                timer.cancel();
                timer = null;
                break;
            }
            case State.PAUSED:
            case State.UNSTARTED:
            default:
        }
    }

    @Override
    public void start() {
        state = State.PLAYING;
    }

    @Override
    public void stop() {
        state = State.FINISHED;
    }

    @Override
    public void pause() {
        state = State.PAUSED;
    }

    @Override
    public void resume() {
        state = State.PLAYING;
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
    public @State
    int getState() {
        return state;
    }
}
