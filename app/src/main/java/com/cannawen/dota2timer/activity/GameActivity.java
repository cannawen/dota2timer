package com.cannawen.dota2timer.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cannawen.dota2timer.R;
import com.cannawen.dota2timer.model.Game;
import com.cannawen.dota2timer.model.GameDisplayer;

import java.io.IOException;

import static android.speech.tts.TextToSpeech.QUEUE_ADD;

public class GameActivity extends Activity {

    private Game game;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        try {
            game = new Game(getApplicationContext(), new MainGameDisplayer());
        } catch (IOException e) {
            e.printStackTrace();
        }
        tts = new TextToSpeech(getApplicationContext(), null);
    }

    public void startGame(View view) {
        game.start();
        findViewById(R.id.game_started_view).setVisibility(View.VISIBLE);
        findViewById(R.id.game_not_started_view).setVisibility(View.INVISIBLE);
    }

    public void playOrPauseGame(View view) {
        Button button = findViewById(R.id.play_pause_button);
        if (game.isPaused()) {
            game.resume();
            button.setText(R.string.game_action_pause);
        } else {
            game.pause();
            button.setText(R.string.game_action_resume);
        }
    }

    public void increaseTime(View view) {
        game.increaseTime();
    }

    public void decreaseTime(View view) {
        game.decreaseTime();
    }

    class MainGameDisplayer implements GameDisplayer {
        @Override
        @SuppressLint("DefaultLocale")
        public void timeUpdated(int totalSecondsElapsed) {
            int hours = totalSecondsElapsed / 3600;
            final int minutes = (totalSecondsElapsed % 3600) / 60;
            int seconds = totalSecondsElapsed % 60;

            final String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((TextView) findViewById(R.id.time_text)).setText(timeString);
                }
            });
        }

        @Override
        public void warn(String warning) {
            tts.speak(warning, QUEUE_ADD, null, warning);
        }
    }
}
