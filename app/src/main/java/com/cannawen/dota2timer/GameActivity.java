package com.cannawen.dota2timer;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.TextView;

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
    }

    public void pauseGame(View view) {
        game.pause();
    }

    class MainGameDisplayer implements GameDisplayer {
        @Override
        public void timeUpdated(int totalSecondsElapsed) {
            int hours = totalSecondsElapsed / 3600;
            int minutes = (totalSecondsElapsed % 3600) / 60;
            int seconds = totalSecondsElapsed % 60;

            final String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((TextView) findViewById(R.id.header_time_view)).setText(timeString);
                }
            });
        }

        @Override
        public void warn(String warning) {
            tts.speak(warning, QUEUE_ADD, null, warning);
        }
    }
}
